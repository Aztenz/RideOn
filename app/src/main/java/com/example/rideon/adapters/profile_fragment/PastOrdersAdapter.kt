package com.example.rideon.adapters.profile_fragment

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
import java.text.SimpleDateFormat

class PastOrdersAdapter(
    private val offeredRides: List<RoutesManager.Ride>)
    : RecyclerView.Adapter<PastOrdersAdapter.PastOrdersViewHolder>() {

    @SuppressLint("SimpleDateFormat")
    private val pattern = SimpleDateFormat("d MMM - h:MM a")

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PastOrdersAdapter.PastOrdersViewHolder {
        val context: Context = parent.context
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val itemView: View =  inflater.inflate(
            R.layout.item_past_order,
            parent,
            false)

        return PastOrdersViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return offeredRides.size
    }

    override fun onBindViewHolder(
        holder: PastOrdersViewHolder,
        position: Int) {

        holder.pickupTV.text = offeredRides[position].pickup
        holder.dropOffTV.text = offeredRides[position].dropOff
        holder.timeTV.text = pattern.format(offeredRides[position].time)
        holder.statusTV.text = "Complete"

    }


    inner class PastOrdersViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView){

        val pickupTV: TextView = itemView.findViewById(R.id.text_view_pickup)
        val dropOffTV: TextView = itemView.findViewById(R.id.text_view_drop_off)
        val timeTV: TextView = itemView.findViewById(R.id.text_view_date)
        val statusTV: TextView = itemView.findViewById(R.id.text_view_status)
    }
}