package com.example.rideon.controller.passenger.popups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.rideon.R
import com.example.rideon.controller.passenger.Config
import com.example.rideon.model.data_classes.Ride
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class OrderInfo(
    private val ride: Ride,
    private val status: String,
    private val driverName: String,
    private val isCancelable: Boolean,
    private val onCancelled: () -> Unit
) : BottomSheetDialogFragment() {

    private val names = arrayOf("Motorcycle", "Car", "Van", "Bus")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.p_popup_order_info, container, false)
        val cancelBtn: Button = view.findViewById(R.id.button_cancel)
        val driverNameTV: TextView = view.findViewById(R.id.text_view_driver_name)
        val rideTypeTV: TextView = view.findViewById(R.id.text_view_ride_type)
        val pickupTV: TextView = view.findViewById(R.id.text_view_pickup)
        val dropOffTV: TextView = view.findViewById(R.id.text_view_drop_off)
        val dateTV: TextView = view.findViewById(R.id.text_view_date)
        val statusTV: TextView = view.findViewById(R.id.text_view_status)

        driverNameTV.text = driverName
        rideTypeTV.text = names[ride.vehicleType]
        pickupTV.text = ride.origin
        dropOffTV.text = ride.destination
        dateTV.text = Config.DATE_PATTERN.format(ride.date)
        statusTV.text = status

        if(!isCancelable) cancelBtn.visibility = View.GONE
        cancelBtn.setOnClickListener{
            onDismiss(this.dialog!!)
            onCancelled.invoke()
        }

        return view
    }
}
