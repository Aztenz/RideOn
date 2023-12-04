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

class RidesTypeAdapter(private val availableRidesRecycler: RecyclerView):
    RecyclerView.Adapter<RidesTypeAdapter.RidesTypeViewHolder>() {

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

    private lateinit var fragment: Context
    private var selectedItemPosition: Int = 0  // Initialize with the position of the first item

    init {
        // Invoke the logic for the first item when the adapter is created
        RoutesManager.getInstance().getAvailableRidesOnType(
            rideType = 1,
            onSuccess = { rides ->
                availableRidesRecycler.adapter = RidesOnTypeAdapter(rides)
            },
            onFailure = { exception ->
                Toast.makeText(fragment, "Error! ${exception}.", Toast.LENGTH_SHORT).show()
            }
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RidesTypeViewHolder {
        val context: Context = parent.context
        fragment = context
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val itemView: View = inflater.inflate(
            R.layout.item_rt_fragment_home, parent
            ,false)

        return RidesTypeViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return names.size
    }

    @SuppressLint("NotifyDataSetChanged", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: RidesTypeViewHolder,
                                  @SuppressLint("RecyclerView") position: Int) {
        val name: String = names[position]
        val image: Int = images[position]

        holder.textView.text = name
        holder.imgBtn.setImageResource(image)

        val isSelected = position == selectedItemPosition

        if (isSelected) {
            holder.frame.background = fragment.getDrawable(R.drawable.border_round_clicked)
        } else {
            holder.frame.background = fragment.getDrawable(R.drawable.border_round)
        }

        holder.imgBtn.setOnClickListener {
            selectedItemPosition = position
            notifyDataSetChanged()

            // Your existing logic here
            RoutesManager.getInstance().getAvailableRidesOnType(
                rideType = 1,
                onSuccess = { rides ->
                    availableRidesRecycler.adapter = RidesOnTypeAdapter(rides)
                },
                onFailure = { exception ->
                    Toast.makeText(fragment, "Error! ${exception}.", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    inner class RidesTypeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgBtn: ImageButton = itemView.findViewById(R.id.button_rt_item_rt_fragment_home)
        val textView: TextView = itemView.findViewById(R.id.tv_rt_name_item_rt_fragment_home)
        val frame: FrameLayout = itemView.findViewById(R.id.frame_rt_item_rt_fragment_home)
    }
}
