package com.example.rideon.adapters.order_history_fragment

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

class PastOrderAdapter(
    private val pastRides: List<RoutesManager.Ride>,
    private val popupContext: Context?)
    : RecyclerView.Adapter<PastOrderAdapter.PastOrderViewHolder>() {

    private lateinit var popupBookNow: PopupBookNow

    @SuppressLint("SimpleDateFormat")
    private val pattern = SimpleDateFormat("d MMM - h:MM a")

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PastOrderViewHolder {
        val context: Context = parent.context
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val itemView: View =  inflater.inflate(
            R.layout.item_past_fragment_oh,
            parent,
            false)
        popupBookNow = popupContext?.let { PopupBookNow.getInstance(it) }!!
        return PastOrderViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return pastRides.size
    }

    override fun onBindViewHolder(
        holder: PastOrderViewHolder,
        position: Int) {
        // Get the data model based on position
        val pickup: String = pastRides[position].pickup
        val dropOff: String = pastRides[position].dropOff
        val time: Date = pastRides[position].time

        // Set item views based on your views and data model
        holder.pickupTV.text = pickup
        holder.dropOffTV.text = dropOff
        holder.timeTV.text = pattern.format(time)
        holder.detailsBtn.setOnClickListener {
            holder.detailsBtn.isEnabled = false
            popupBookNow.showPopup(it)
            popupBookNow.setOnDismissListener {
                holder.detailsBtn.isEnabled = true
            }
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