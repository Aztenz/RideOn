package com.example.rideon.view.passenger

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.rideon.R
import com.example.rideon.adapters.available_rides_fragment.AvailableRidesAdapter
import com.example.rideon.model.database.RoutesManager

class FragmentAvailableRoutes : Fragment() {

    private lateinit var availableRidesRecycler: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(
            R.layout.fragment_available_routes,
            container, false)

        availableRidesRecycler = view.findViewById(R.id.recycler_available_routes_fragment_ar)

        RoutesManager.getInstance().getAvailableRides({ availableRides ->
            if (availableRides.isEmpty()) {
                // Handle the case when there are no available rides
                // Show a message or perform any necessary action
            } else {
                // Set the adapter when the data is available
                val adapter = AvailableRidesAdapter(availableRides, this.context)
                availableRidesRecycler.adapter = adapter
            }
        }, { exception ->
            Toast
                .makeText(context, "Failed to get data: $exception", Toast.LENGTH_SHORT)
                .show()
        })

        return view
    }
}