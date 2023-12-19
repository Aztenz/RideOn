package com.example.rideon.controller.driver.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rideon.R
import com.example.rideon.controller.driver.Config
import com.example.rideon.model.data_classes.Ride

class PastOrders(
    private val pastOrders: List<Ride>)
    : RecyclerView.Adapter<PastOrders.PastOrdersViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PastOrdersViewHolder {
        val context: Context = parent.context
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val itemView: View =  inflater.inflate(
            R.layout.d_item_past_order,
            parent,
            false)

        return PastOrdersViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return pastOrders.size
    }

    override fun onBindViewHolder(
        holder: PastOrdersViewHolder,
        position: Int) {

        holder.pickupTV.text = pastOrders[position].origin
        holder.dropOffTV.text = pastOrders[position].destination
        holder.timeTV.text = Config.DATE_PATTERN.format(pastOrders[position].date)
        holder.statusTV.text = pastOrders[position].status

    }


    inner class PastOrdersViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView){

        val pickupTV: TextView = itemView.findViewById(R.id.text_view_pickup)
        val dropOffTV: TextView = itemView.findViewById(R.id.text_view_drop_off)
        val timeTV: TextView = itemView.findViewById(R.id.text_view_date)
        val statusTV: TextView = itemView.findViewById(R.id.text_view_status)
    }
}