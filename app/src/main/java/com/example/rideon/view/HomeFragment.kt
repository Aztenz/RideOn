package com.example.rideon.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.rideon.R
import com.example.rideon.adapters.home_fragment.RidesTypeAdapter

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val rideTypesRecycler: RecyclerView = view
            .findViewById(R.id.recycler_rides_type_fragment_home)

        rideTypesRecycler.adapter = RidesTypeAdapter(
            view.findViewById(R.id.recycler_available_rides_fragment_home))
        return view
    }
}