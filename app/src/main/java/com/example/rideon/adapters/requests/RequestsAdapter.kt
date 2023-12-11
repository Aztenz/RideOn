package com.example.rideon.adapters.requests

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

class RequestsAdapter(
    private val requests: List<RoutesManager.Ride>)
    : RecyclerView.Adapter<RequestsAdapter.RequestsViewHolder>() {

    @SuppressLint("SimpleDateFormat")
    private val pattern = SimpleDateFormat("d MMM - h:MM a")

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RequestsViewHolder {
        val context: Context = parent.context
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val itemView: View =  inflater.inflate(
            R.layout.item_past_fragment_oh,
            parent,
            false)

        return RequestsViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return requests.size
    }

    override fun onBindViewHolder(
        holder: RequestsViewHolder,
        position: Int) {


    }


    inner class RequestsViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView){
        val pickupTV: TextView = itemView
            .findViewById(R.id.tv_pickup_item_request_fragment_request)
        val dropOffTV: TextView = itemView
            .findViewById(R.id.tv_drop_off_item_request_fragment_request)
        val dateTV: TextView = itemView
            .findViewById(R.id.tv_date_item_request_fragment_request)
        val priceTV: TextView = itemView
            .findViewById(R.id.tv_price_item_request_fragment_request)
        val emailTV: TextView = itemView
            .findViewById(R.id.tv_email_item_request_fragment_request)
        val acceptBtn: Button = itemView
            .findViewById(R.id.button_accept_item_request_fragment_request)
        val rejectBtn: Button = itemView
            .findViewById(R.id.button_reject_item_request_fragment_request)
    }
}