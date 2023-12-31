package com.example.rideon.controller.passenger.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.rideon.R
import com.example.rideon.controller.passenger.fragments.Home
import com.example.rideon.model.data_classes.Ride
import com.example.rideon.model.data_classes.User
import com.example.rideon.model.database.firebase.PassengerManager

class RidesType(
    private val availableRidesRecycler : RecyclerView,
    private val fragment : Home,
    private val passenger: User
) :
    RecyclerView.Adapter<RidesType.RidesTypeViewHolder>() {

    private lateinit var noRides : TextView

    private var images = arrayOf(
        R.drawable.motorcycle,
        R.drawable.taxi,
        R.drawable.van,
        R.drawable.bus)

    private var names = arrayOf(
        "Bikes",
        "Cars",
        "Vans",
        "Busses",
    )

    private var selectedItemPosition : Int = 0// Initialize with the position of the first item


    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : RidesTypeViewHolder {
        val context : Context = parent.context
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val itemView : View = inflater.inflate(
            R.layout.p_item_ride_type, parent
            ,false)
        noRides = fragment
            .requireView()
            .findViewById(R.id.text_view_no_rides)

        return RidesTypeViewHolder(itemView)
    }

    override fun getItemCount() : Int {
        return names.size
    }

    @SuppressLint("NotifyDataSetChanged", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder : RidesTypeViewHolder,
                                  @SuppressLint("RecyclerView") position : Int) {

        //TODO: add data
        itemOnClick(selectedItemPosition)

        val name : String = names[position]
        val image : Int = images[position]

        holder.textView.text = name
        holder.imgBtn.setImageResource(image)

        val isSelected = position == selectedItemPosition

        if (isSelected) {
            holder.frame.background =
                fragment.context?.getDrawable(R.drawable.border_round_clicked)
        } else {
            holder.frame.background = fragment.context?.getDrawable(R.drawable.border_round)
        }

        holder.imgBtn.setOnClickListener{
            selectedItemPosition = position
            notifyDataSetChanged()
            itemOnClick(position)
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun itemOnClick(position : Int) {
        PassengerManager.instance.getAvailableRidesByType(
            passengerId = passenger.userId,
            vehicleType = position,
            onSuccess = { rides ->
                if(rides.isEmpty()){
                    availableRidesRecycler.visibility = View.INVISIBLE
                    noRides.visibility = View.VISIBLE
                    return@getAvailableRidesByType
                } else {
                    noRides.visibility = View.INVISIBLE
                    availableRidesRecycler.visibility = View.VISIBLE
                }
                val adapter = AvailableRideOnType(
                    rides = rides as MutableList<Ride>, popupContext =  fragment.context, passenger = passenger)

                availableRidesRecycler.adapter = adapter
                adapter.notifyDataSetChanged()
            },
            onFailure = {
                Toast.makeText(
                    fragment.context,
                    "Unexpected error please check your network",
                    Toast.LENGTH_SHORT).show()
            }
        )
    }

    inner class RidesTypeViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val imgBtn : ImageButton = itemView.findViewById(R.id.button_rt_item_rt_fragment_home)
        val textView : TextView = itemView.findViewById(R.id.tv_rt_name_item_rt_fragment_home)
        val frame : FrameLayout = itemView.findViewById(R.id.frame_rt_item_rt_fragment_home)
    }
}
