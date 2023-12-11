package com.example.rideon.adapters.home_fragment

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rideon.R
import com.example.rideon.model.database.RoutesManager
import com.example.rideon.view.passenger.PopupBookNow
import java.text.SimpleDateFormat
import java.util.Date


class RidesOnTypeAdapter(
    private val rides: List<RoutesManager.Ride>
    ,private val popupContext: Context?) :
    RecyclerView.Adapter<RidesOnTypeAdapter.RidesTypeViewHolder>() {

    private lateinit var popupBookNow: PopupBookNow

    @SuppressLint("SimpleDateFormat")
    private val pattern = SimpleDateFormat("d MMM - h:MM a")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RidesTypeViewHolder {
        val context: Context = parent.context
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val itemView: View =  inflater.inflate(
            R.layout.item_ar_fragment_home,
            parent,
            false)


        popupBookNow = popupContext?.let { PopupBookNow.getInstance(it) }!!

        return RidesTypeViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return rides.size
    }

    override fun onBindViewHolder(holder: RidesTypeViewHolder, position: Int) {
        // Get the data model based on position
        val pickup: String = rides[position].pickup
        val dropOff: String = rides[position].dropOff
        val time: Date = rides[position].time
        val price: Double = rides[position].price

        // Set item views based on your views and data model
        holder.pickupTV.text = pickup
        holder.dropOffTV.text = dropOff
        holder.timeTV.text = pattern.format(time)
        holder.bookNowBtn.setOnClickListener {
            holder.bookNowBtn.isEnabled = false
            popupBookNow.showPopup(it)
            popupBookNow.setOnDismissListener {
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
        val bookNowBtn: Button = itemView.findViewById(R.id.button_accept_item_request_fragment_request)
    }
}