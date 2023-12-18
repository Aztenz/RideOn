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
import java.text.SimpleDateFormat
import java.util.Date

class PastOrders(
    private val pastRides: List<Ride>,
    private val popupContext: Context?)
    : RecyclerView.Adapter<PastOrders.PastOrderViewHolder>() {

    private lateinit var bookNow: BookNow

    @SuppressLint("SimpleDateFormat")
    private val pattern = SimpleDateFormat("d MMM - h:MM a")

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PastOrderViewHolder {
        val context: Context = parent.context
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val itemView: View =  inflater.inflate(
            R.layout.p_item_past_order,
            parent,
            false)
        bookNow = popupContext?.let { BookNow.getInstance(it) }!!
        return PastOrderViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return pastRides.size
    }

    override fun onBindViewHolder(
        holder: PastOrderViewHolder,
        position: Int) {
        // Get the data model based on position
        val pickup: String = pastRides[position].origin
        val dropOff: String = pastRides[position].destination
        val time: Date = pastRides[position].date

        // Set item views based on your views and data model
        holder.pickupTV.text = pickup
        holder.dropOffTV.text = dropOff
        holder.timeTV.text = pattern.format(time)
        holder.detailsBtn.setOnClickListener {
//            holder.detailsBtn.isEnabled = false
//            popupBookNow.showPopup(it)
//            popupBookNow.setOnDismissListener {
//                holder.detailsBtn.isEnabled = true
//            }
        }

    }


    inner class PastOrderViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView){
        val pickupTV: TextView = itemView.findViewById(R.id.tv_pickup_item_past_fragment_oh)
        val dropOffTV: TextView = itemView.findViewById(R.id.tv_drop_off_item_past_fragment_oh)
        val timeTV: TextView = itemView.findViewById(R.id.tv_date_item_past_fragment_oh)
        val detailsBtn: Button = itemView.findViewById(R.id.button_details_item_past_fragment_oh)
    }
}