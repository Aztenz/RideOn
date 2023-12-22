package com.example.rideon.controller.passenger.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.rideon.R
import com.example.rideon.model.data_classes.Ride
import com.example.rideon.controller.passenger.popups.BookNow
import com.example.rideon.controller.passenger.popups.OrderInfo
import java.text.SimpleDateFormat
import java.util.Date

class PastOrders(
    private val pastRides: List<Ride>,
    private val driverNames: List<String>,
    private val popupContext: Context?,
    private val fragment: Fragment
    )
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
            val orderInfo = OrderInfo(
                ride = pastRides[position],
                status = pastRides[position].status,
                driverName = driverNames[position],
                isCancelable = false,
                onCancelled = {}
            )
            orderInfo.show(fragment.childFragmentManager, orderInfo.tag)
        }
    }


    inner class PastOrderViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView){
        val pickupTV: TextView = itemView.findViewById(R.id.text_view_pickup)
        val dropOffTV: TextView = itemView.findViewById(R.id.text_view_drop_off)
        val timeTV: TextView = itemView.findViewById(R.id.text_view_date)
        val detailsBtn: Button = itemView.findViewById(R.id.button_details)
    }
}