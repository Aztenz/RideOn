package com.example.rideon.controller.passenger.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rideon.R

class OrderHistory : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(
            R.layout.p_fragment_order_history,
            container, false)

        val activeRideRecycler: RecyclerView = view
            .findViewById(R.id.recycler_active_ride_fragment_oh)

        val pastRidesRecycler: RecyclerView = view
            .findViewById(R.id.recycler_past_ride_fragment_oh)

        //TODO: add data

        return view
    }
}