package com.example.rideon.controller.passenger.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rideon.R
import com.example.rideon.model.data_classes.Ride
import com.example.rideon.controller.passenger.popups.BookNow
import com.example.rideon.model.data_classes.User
import java.text.SimpleDateFormat
import java.util.Date

class AvailableRides(
    private val rides: MutableList<Ride>,
    private val popupContext: Context?,
    private val passenger: User
)
    : RecyclerView.Adapter<AvailableRides.AvailableRidesViewHolder>() {

    private lateinit var bookNow: BookNow

    @SuppressLint("SimpleDateFormat")
    private val pattern = SimpleDateFormat("d MMM - h:MM a")

    private var images = arrayOf(
        R.drawable.motorcycle,
        R.drawable.taxi,
        R.drawable.van,
        R.drawable.bus)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvailableRidesViewHolder {
        val context: Context = parent.context
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val itemView: View =  inflater.inflate(
            R.layout.p_item_available_ride,
            parent,
            false)


        bookNow = popupContext?.let { BookNow.getInstance(it) }!!

        return AvailableRidesViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return rides.size
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: AvailableRidesViewHolder, position: Int) {
        // Get the data model based on position
        val pickup: String = rides[position].origin
        val dropOff: String = rides[position].destination
        val time: Date = rides[position].date
        val price: Double = rides[position].price


        // Set item views based on your views and data model
        holder.pickupTV.text = pickup
        holder.dropOffTV.text = dropOff
        holder.timeTV.text = pattern.format(time)
        holder.priceTV.text = price.toString()
        holder.rideTypeIcon.setImageResource(images[rides[position].vehicleType])

        val balanceAfterRide = passenger.walletBalance - rides[position].price

        if(balanceAfterRide<0)
            holder.bookNowBtn.isEnabled = false

        holder.bookNowBtn.isEnabled = rides[position].availableSeats != 0
        holder.bookNowBtn.setOnClickListener {
            holder.bookNowBtn.isEnabled = false
            bookNow.showPopup(
                it,
                passenger,
                rides[position],
                onBooked = {
                    rides.removeAll(elements = rides)
                    notifyDataSetChanged()
                }
            )
            bookNow.setOnDismissListener {
                holder.bookNowBtn.isEnabled = true
            }
        }
    }


    inner class AvailableRidesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rideTypeIcon: ImageView = itemView.findViewById(R.id.iv_rt_item_ar_fragment_ar)
        val pickupTV: TextView = itemView.findViewById(R.id.tv_pickup_item_ar_fragment_ar)
        val dropOffTV: TextView = itemView.findViewById(R.id.tv_drop_off_item_ar_fragment_ar)
        val timeTV: TextView = itemView.findViewById(R.id.tv_date_item_ar_fragment_ar)
        val priceTV: TextView = itemView.findViewById(R.id.tv_price_item_ar_fragment_ar)
        val bookNowBtn: Button = itemView.findViewById(R.id.button_book_now_item_ar_fragment_ar)
    }
}