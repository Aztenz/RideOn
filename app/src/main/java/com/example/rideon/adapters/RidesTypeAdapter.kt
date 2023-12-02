package com.example.rideon.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rideon.R

class RidesTypeAdapter : RecyclerView.Adapter<RidesTypeAdapter.RidesTypeViewHolder>() {

    private var images = arrayOf(R.drawable.motorcycle, R.drawable.taxi, R.drawable.van, R.drawable.bus)
    private var names = arrayOf("Bikes", "Cars", "Vans", "Busses")
    private lateinit var fragment: ViewGroup
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RidesTypeViewHolder {
        val context: Context = parent.context
        val inflater: LayoutInflater = LayoutInflater.from(context)
        fragment = parent

        // Inflate the custom layout
        val itemView: View = inflater.inflate(R.layout.item_rt_fragment_home, parent, false)

        // Return a new ViewHolder
        return RidesTypeViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return names.size
    }

    override fun onBindViewHolder(holder: RidesTypeViewHolder, position: Int) {
        // Get the data model based on position
        val name: String = names[position]
        val image: Int = images[position]

        // Set item views based on your views and data model
        holder.textView.text = name
        holder.imgBtn.setImageResource(image)
        holder.imgBtn.setOnClickListener {
            var ridesOnTypeRecycler: RecyclerView =
                fragment
                    .findViewById(R.id.recycler_available_rides_fragment_home)

//            ridesOnTypeRecycler.adapter =
        }
    }

    // RidesTypeViewHolder is now an inner class
    inner class RidesTypeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgBtn: ImageButton = itemView.findViewById(R.id.button_rt_item_rt_fragment_home)
        val textView: TextView = itemView.findViewById(R.id.tv_rt_name_item_rt_fragment_home)
    }
}