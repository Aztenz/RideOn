package com.example.rideon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.rideon.adapters.RidesTypeAdapter

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val rideTypesRecycler: RecyclerView = view.findViewById(R.id.recycler_rides_type_fragment_home)
        rideTypesRecycler.adapter = RidesTypeAdapter()
        return view
    }
}