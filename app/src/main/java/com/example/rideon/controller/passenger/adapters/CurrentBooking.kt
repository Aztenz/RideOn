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

class CurrentBooking(
    private val activeRide: Ride,
    private val popupContext: Context?
) : RecyclerView.Adapter<CurrentBooking.CurrentActiveOrderViewHolder>() {

    private lateinit var bookNow: BookNow

    @SuppressLint("SimpleDateFormat")
    private val pattern = SimpleDateFormat("d MMM - h:MM a")

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CurrentActiveOrderViewHolder {
        val context: Context = parent.context
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val itemView: View =  inflater.inflate(
            R.layout.p_item_active_booking,
            parent,
            false)
        bookNow = popupContext?.let { BookNow.getInstance(it) }!!
        return CurrentActiveOrderViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(
        holder: CurrentActiveOrderViewHolder,
        position: Int) {

        holder.pickupTV.text = activeRide.origin
        holder.dropOffTV.text = activeRide.destination
        holder.timeTV.text = pattern.format(activeRide.date)

        holder.trackBtn.setOnClickListener {
            holder.trackBtn.isEnabled = false
//            popupBookNow.showPopup(it)
//            popupBookNow.setOnDismissListener {
//                holder.trackBtn.isEnabled = true
//            }
        }
        holder.cancelBtn.setOnClickListener {
//            holder.cancelBtn.isEnabled = false
//            popupBookNow.showPopup(it)
//            popupBookNow.setOnDismissListener {
//                holder.cancelBtn.isEnabled = true
//            }
        }
    }


    inner class CurrentActiveOrderViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView){
        val pickupTV: TextView = itemView.findViewById(R.id.tv_pickup_item_ca_fragment_oh)
        val dropOffTV: TextView = itemView.findViewById(R.id.tv_drop_off_item_ca_fragment_oh)
        val timeTV: TextView = itemView.findViewById(R.id.tv_date_item_ca_fragment_oh)
        val trackBtn: Button = itemView.findViewById(R.id.button_track_item_ca_fragmnet_oh)
        val cancelBtn: Button = itemView.findViewById(R.id.button_Cancel_item_ca_fragmnet_oh)
    }
}