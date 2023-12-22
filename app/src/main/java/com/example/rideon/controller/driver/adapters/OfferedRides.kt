package com.example.rideon.controller.driver.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rideon.R
import com.example.rideon.controller.driver.Config
import com.example.rideon.model.data_classes.Ride
import com.example.rideon.model.database.firebase.DriverManager

class OfferedRides(
    private val offeredRides: MutableList<Ride>)
    : RecyclerView.Adapter<OfferedRides.OfferedRidesViewHolder>() {

    private val driverManager: DriverManager = DriverManager.instance

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OfferedRidesViewHolder {
        val context: Context = parent.context
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val itemView: View =  inflater.inflate(
            R.layout.d_item_offered_ride,
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

        holder.pickupTV.text = offeredRides[position].origin
        holder.dropOffTV.text = offeredRides[position].destination
        holder.dateTV.text = Config.DATE_PATTERN.format(offeredRides[position].date)
        holder.priceTV.text = offeredRides[position].price.toString()
        holder.seatsTV.text = offeredRides[position].availableSeats.toString()
        holder.cancelBtn.setOnClickListener {
            holder.cancelBtn.isEnabled = false
            driverManager.cancelOfferedRide(
                offeredRides[position].rideId,
                offeredRides[position].driverId,
                onSuccess = {},
                onFailure = {}
            )
        }

        holder.finishRideBtn.setOnClickListener {
            driverManager.finishRides(offeredRides[position].rideId,
                onSuccess = {
                    offeredRides.removeAt(position)
                    notifyItemRemoved(position)
                },
                onFailure = {
                    val x =5
                })
        }
    }


    inner class OfferedRidesViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView){
        val pickupTV: TextView = itemView
            .findViewById(R.id.text_view_pickup)
        val dropOffTV: TextView = itemView
            .findViewById(R.id.text_view_drop_off)
        val dateTV: TextView = itemView
            .findViewById(R.id.text_view_date)
        val seatsTV: TextView = itemView
            .findViewById(R.id.text_view_seats)
        val priceTV: TextView = itemView
            .findViewById(R.id.text_view_price)
        val cancelBtn: Button = itemView
            .findViewById(R.id.button_cancel_trip)

        val finishRideBtn: Button = itemView
            .findViewById(R.id.button_finish_ride)
    }
}