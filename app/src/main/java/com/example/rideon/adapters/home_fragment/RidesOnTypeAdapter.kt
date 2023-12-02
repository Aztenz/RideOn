package com.example.rideon.adapters.home_fragment

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rideon.R
import com.example.rideon.model.Database
import java.text.SimpleDateFormat
import java.util.Date


class RidesOnTypeAdapter(availableRides: List<Database.AvailableRide>) : RecyclerView.Adapter<RidesOnTypeAdapter.RidesTypeViewHolder>() {

    private var availableRides: List<Database.AvailableRide>

    @SuppressLint("SimpleDateFormat")
    private val pattern = SimpleDateFormat("d MMM - h:MM a")
    init {
        this.availableRides = availableRides
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RidesTypeViewHolder {
        val context: Context = parent.context
        val inflater: LayoutInflater = LayoutInflater.from(context)

        // Inflate the custom layout
        val itemView: View =  inflater.inflate(R.layout.item_ar_fragment_home, parent, false)

        // Return a new ViewHolder
        return RidesTypeViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return availableRides.size
    }

    override fun onBindViewHolder(holder: RidesTypeViewHolder, position: Int) {
        // Get the data model based on position
        val pickup: String = availableRides[position].pickup
        val dropOff: String = availableRides[position].dropOff
        val time: Date = availableRides[position].time



        // Set item views based on your views and data model
        holder.pickupTV.text = pickup
        holder.dropOffTV.text = dropOff
        holder.timeTV.text = pattern.format(time)
    }

    // RidesTypeViewHolder is now an inner class
    inner class RidesTypeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pickupTV: TextView = itemView.findViewById(R.id.tv_pickup_item_ar_fragment_home)
        val dropOffTV: TextView = itemView.findViewById(R.id.tv_drop_off_item_ar_fragment_home)
        val timeTV: TextView = itemView.findViewById(R.id.tv_date_item_ar_fragment_home)
    }
}