package com.example.rideon.controller.passenger.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rideon.R
import com.example.rideon.model.data_classes.Ride
import com.example.rideon.controller.passenger.popups.BookNow
import com.example.rideon.model.data_classes.User
import java.text.SimpleDateFormat
import java.util.Date


class AvailableRideOnType(
    private val rides: List<Ride>,
    private val popupContext: Context?,
    private val passenger: User
) :
    RecyclerView.Adapter<AvailableRideOnType.RidesTypeViewHolder>() {

    private lateinit var bookNow: BookNow

    @SuppressLint("SimpleDateFormat")
    private val pattern = SimpleDateFormat("d MMM - h:MM a")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RidesTypeViewHolder {
        val context: Context = parent.context
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val itemView: View =  inflater.inflate(
            R.layout.p_item_available_ride_on_type,
            parent,
            false)


        bookNow = popupContext?.let { BookNow.getInstance(it) }!!

        return RidesTypeViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return rides.size
    }

    override fun onBindViewHolder(holder: RidesTypeViewHolder, position: Int) {
        // Get the data model based on position
        val pickup: String = rides[position].origin
        val dropOff: String = rides[position].destination
        val time: Date = rides[position].date
        val price: Double = rides[position].price
        val seats: Int = rides[position].availableSeats

        // Set item views based on your views and data model
        holder.pickupTV.text = pickup
        holder.dropOffTV.text = dropOff
        holder.timeTV.text = pattern.format(time)
        holder.seatsTV.text = seats.toString()
        holder.priceTV.text = price.toString()

        if (rides[position].availableSeats==0)
            holder.bookNowBtn.isEnabled = false

        val balanceAfterRide = passenger.walletBalance - rides[position].price

        if(balanceAfterRide<0)
            holder.bookNowBtn.isEnabled = false

        holder.bookNowBtn.setOnClickListener {
            holder.bookNowBtn.isEnabled = false
            bookNow.showPopup(it, passenger, rides[position])
            bookNow.setOnDismissListener {
                holder.bookNowBtn.isEnabled = true
            }
        }
    }

    // RidesTypeViewHolder is now an inner class
    inner class RidesTypeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pickupTV: TextView = itemView.findViewById(R.id.tv_pickup_item_request_fragment_request)
        val dropOffTV: TextView = itemView.findViewById(R.id.tv_drop_off_item_request_fragment_request)
        val timeTV: TextView = itemView.findViewById(R.id.tv_date_item_request_fragment_request)
        val priceTV: TextView = itemView.findViewById(R.id.tv_price_item_request_fragment_request)
        val seatsTV: TextView = itemView.findViewById(R.id.text_view_seats)
        val bookNowBtn: Button = itemView.findViewById(R.id.button_accept_item_request_fragment_request)
    }
}