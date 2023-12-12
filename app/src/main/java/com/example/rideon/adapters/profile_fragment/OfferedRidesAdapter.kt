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

class OfferedRidesAdapter(
    private val offeredRides: List<RoutesManager.Ride>)
    : RecyclerView.Adapter<OfferedRidesAdapter.OfferedRidesViewHolder>() {

    @SuppressLint("SimpleDateFormat")
    private val pattern = SimpleDateFormat("d MMM - h:MM a")

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OfferedRidesViewHolder {
        val context: Context = parent.context
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val itemView: View =  inflater.inflate(
            R.layout.item_offered_rides,
            parent,
            false)

        return OfferedRidesViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return offeredRides.size
    }

    override fun onBindViewHolder(
        holder: OfferedRidesViewHolder,
        position: Int) {


    }


    inner class OfferedRidesViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView){
        val pickupTV: TextView = itemView
            .findViewById(R.id.text_view_pickup)
        val dropOffTV: TextView = itemView
            .findViewById(R.id.text_view_drop_off)
        val dateTV: TextView = itemView
            .findViewById(R.id.text_view_date)
        val priceTV: TextView = itemView
            .findViewById(R.id.text_view_price)
        val cancelBtn: Button = itemView
            .findViewById(R.id.button_cancel_trip)
    }
}