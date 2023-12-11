package com.example.rideon.adapters.home_fragment

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
import com.example.rideon.model.database.RoutesManager
import com.example.rideon.view.passenger.FragmentPassengerHome

class RidesTypeAdapter(
    private val availableRidesRecycler : RecyclerView,
    private val fragment : FragmentPassengerHome
) :
    RecyclerView.Adapter<RidesTypeAdapter.RidesTypeViewHolder>() {

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
            R.layout.item_rt_fragment_home, parent
            ,false)
        noRides = fragment
            .requireView()
            .findViewById(R.id.tv_no_rides_fragment_home)

        return RidesTypeViewHolder(itemView)
    }

    override fun getItemCount() : Int {
        return names.size
    }

    @SuppressLint("NotifyDataSetChanged", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder : RidesTypeViewHolder,
                                  @SuppressLint("RecyclerView") position : Int) {

        RoutesManager.getInstance().getAvailableRidesOnType(
            rideType = 1,
            onSuccess = { rides ->
                availableRidesRecycler.adapter = RidesOnTypeAdapter(rides,
                    fragment.context
                )
            },
            onFailure = { exception ->
                Toast.makeText(fragment.context, "Error! ${exception}.",
                    Toast.LENGTH_SHORT).show()
            }
        )
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
        RoutesManager.getInstance().getAvailableRidesOnType(
            rideType = position,
            onSuccess = { rides ->
                if(rides.isEmpty()){
                    availableRidesRecycler.visibility = View.GONE
                    noRides.visibility = View.VISIBLE
                    return@getAvailableRidesOnType
                } else {
                    noRides.visibility = View.GONE
                    availableRidesRecycler.visibility = View.VISIBLE
                }
                val adapter = RidesOnTypeAdapter(rides, fragment.context)
                availableRidesRecycler.adapter = adapter
                adapter.notifyDataSetChanged()
            },
            onFailure = { exception ->
                Toast.makeText(fragment.context, "Error! ${exception}.", Toast.LENGTH_SHORT).show()
            }
        )
    }

    inner class RidesTypeViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val imgBtn : ImageButton = itemView.findViewById(R.id.button_rt_item_rt_fragment_home)
        val textView : TextView = itemView.findViewById(R.id.tv_rt_name_item_rt_fragment_home)
        val frame : FrameLayout = itemView.findViewById(R.id.frame_rt_item_rt_fragment_home)
    }
}
