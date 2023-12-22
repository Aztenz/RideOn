package com.example.rideon.controller.passenger.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.rideon.R
import com.example.rideon.controller.passenger.Config
import com.example.rideon.controller.passenger.adapters.PastOrders
import com.example.rideon.controller.passenger.popups.OrderInfo
import com.example.rideon.model.database.firebase.PassengerManager
import com.example.rideon.model.database.room.RoomAccountManager

class OrderHistory : Fragment() {
    private lateinit var roomAccountManager: RoomAccountManager
    private lateinit var passengerManager: PassengerManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(
            R.layout.p_fragment_order_history,
            container, false)

        roomAccountManager = RoomAccountManager.instance
        passengerManager = PassengerManager.instance


        val currentPickupTV: TextView = view.findViewById(R.id.text_view_current_pickup)
        val currentDropOffTV: TextView = view.findViewById(R.id.text_view_current_drop_off)
        val currentDateTV: TextView = view.findViewById(R.id.text_view_current_date)
        val currentDetailsBtn: Button = view.findViewById(R.id.button_current_details)
        val currentCancelBtn: Button = view.findViewById(R.id.button_current_cancel)
        val currentErrorTV: TextView = view.findViewById(R.id.text_view_no_current_order)
        val currentOrderLayout: LinearLayout = view.findViewById(R.id.layout_current_order)

        val pastErrorTV: TextView = view.findViewById(R.id.text_view_no_past_orders)
        val pastOrdersRecycler: RecyclerView = view.findViewById(R.id.recycler_past_orders)

        roomAccountManager.getLoggedInUser(
            fragment = this@OrderHistory,
            onSuccess = { me ->
                passengerManager.getPassengerBooking(
                    passenger = me,
                    onSuccess = { booking ->
                        currentPickupTV.text = booking.origin
                        currentDropOffTV.text = booking.destination
                        currentDateTV.text = Config.DATE_PATTERN.format(booking.rideDate)
                        currentCancelBtn.setOnClickListener {
                            passengerManager.cancelBooking(
                                bookingRequest = booking,
                                onSuccess = {
                                    currentErrorTV.visibility = View.VISIBLE
                                    currentOrderLayout.visibility = View.GONE
                                },
                                onFailure = { e->
                                    Toast.makeText(requireContext(),
                                        e.message.toString(),
                                        Toast.LENGTH_SHORT).show()
                                }
                            )
                        }

                        currentDetailsBtn.setOnClickListener {
                            passengerManager.getPassengerBookingDetails(
                                rideId = booking.rideId,
                                driverId = booking.driverId,
                                onSuccess = { ride, driverName ->
                                    val orderInfo = OrderInfo(
                                        ride = ride,
                                        status = booking.status,
                                        driverName = driverName,
                                        isCancelable = true,
                                        onCancelled = {
                                        currentCancelBtn.performClick()
                                    })
                                    orderInfo.show(this.childFragmentManager, orderInfo.tag)
                                }
                            )
                        }

                        currentErrorTV.visibility = View.GONE
                        currentOrderLayout.visibility = View.VISIBLE
                    },
                    onFailure = {
                        currentErrorTV.visibility = View.VISIBLE
                        currentOrderLayout.visibility = View.GONE
                    }
                )
                passengerManager.getPassengerPastRides(
                    passenger = me,
                    onSuccess = { rides, driverNames ->
                        pastErrorTV.visibility = View.GONE
                        pastOrdersRecycler.visibility = View.VISIBLE
                        pastOrdersRecycler.adapter = PastOrders(
                                    pastRides = rides,
                                    driverNames = driverNames,
                                    popupContext = requireContext(),
                                    fragment = this@OrderHistory)
                    },
                    onFailure = {
                        pastErrorTV.visibility = View.VISIBLE
                        pastOrdersRecycler.visibility = View.GONE
                    }
                )
            },
            onFailure = {})


        return view
    }
}