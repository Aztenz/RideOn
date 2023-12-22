package com.example.rideon.controller.driver.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.rideon.R
import com.example.rideon.controller.driver.Config
import com.example.rideon.model.data_classes.BookingRequest
import com.example.rideon.model.data_classes.User
import com.example.rideon.model.database.firebase.DriverManager
import com.example.rideon.model.database.room.RoomAccountManager

class Requests(
    private val fragment: Fragment,
    private val rideBookings: MutableList<BookingRequest>,
    private val user: User)
    : RecyclerView.Adapter<Requests.RequestsViewHolder>() {

    private val driverManager = DriverManager.instance

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RequestsViewHolder {
        val context: Context = parent.context
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val itemView: View =  inflater.inflate(
            R.layout.d_item_request,
            parent,
            false)

        return RequestsViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return rideBookings.size
    }

    override fun onBindViewHolder(
        holder: RequestsViewHolder,
        position: Int) {
        Log.d(Config.LOG_TAG, rideBookings[position].requestId)
        holder.pickupTV.text = rideBookings[position].origin
        holder.dropOffTV.text = rideBookings[position].destination
        holder.dateTV.text = Config.DATE_PATTERN.format(rideBookings[position].rideDate)
        holder.emailTV.text = rideBookings[position].passengerEmail

        holder.acceptBtn.setOnClickListener {
            driverManager.acceptBookings(
                requestId = rideBookings[position].requestId,
                rideId  = rideBookings[position].rideId,
                onSuccess = {
                    rideBookings.removeAt(position)
                    notifyItemRemoved(position)
                    RoomAccountManager.instance.updateUserWallet(user, it)
                },
                onFailure = {
                    Toast.makeText(
                        fragment.requireActivity(),
                        it.message,
                        Toast.LENGTH_SHORT).show()}
            )
        }

        holder.rejectBtn.setOnClickListener {
            driverManager.cancelBooking(rideBookings[position].requestId)
            driverManager.getRideData(
                rideId = rideBookings[position].rideId,
                onSuccess = {
                    it.status = Config.RIDE_STATUS_REJECTED
                    driverManager.addRideToHistory(
                        userId = rideBookings[position].passengerId,
                        userType = Config.ROLE_TYPE_PASSENGER,
                        ride = it,
                        onSuccess = {
                            rideBookings.removeAt(position)
                            notifyItemRemoved(position) },
                        onFailure = {})
                },
                onFailure = {
                    Toast.makeText(
                        fragment.requireActivity(),
                        it.message,
                        Toast.LENGTH_SHORT).show()}
            )
        }
    }


    inner class RequestsViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView){
        val pickupTV: TextView = itemView.findViewById(R.id.text_view_pickup)
        val dropOffTV: TextView = itemView.findViewById(R.id.text_view_drop_off)
        val dateTV: TextView = itemView.findViewById(R.id.text_view_date)
        val emailTV: TextView = itemView.findViewById(R.id.text_view_email)
        val acceptBtn: Button = itemView.findViewById(R.id.button_accept)
        val rejectBtn: Button = itemView.findViewById(R.id.button_reject)
    }
}