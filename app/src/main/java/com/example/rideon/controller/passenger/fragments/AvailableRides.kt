package com.example.rideon.controller.passenger.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rideon.R
import com.example.rideon.controller.passenger.adapters.AvailableRides
import com.example.rideon.model.data_classes.Ride

import com.example.rideon.model.database.firebase.PassengerManager
import com.example.rideon.model.database.room.RoomAccountManager


class AvailableRides : Fragment() {

    private lateinit var availableRidesRecycler: RecyclerView
    private lateinit var roomDBManager: RoomAccountManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(
            R.layout.p_fragment_available_routes,
            container, false)
        val noRidesTV: TextView = view.findViewById(R.id.text_view_no_rides)

        availableRidesRecycler = view.findViewById(R.id.recycler_available_routes_fragment_ar)

        roomDBManager = RoomAccountManager.instance

        roomDBManager.getLoggedInUser(
            fragment = this@AvailableRides,
            onSuccess = { me ->
                PassengerManager.instance.getAvailableRides(
                    me.userId,
                    onSuccess = {
                        if(it.isNotEmpty()) {
                            noRidesTV.visibility = View.GONE
                            availableRidesRecycler.visibility = View.VISIBLE
                            availableRidesRecycler.adapter =
                                AvailableRides(it as MutableList<Ride>, this.context, me)
                        } else {
                            noRidesTV.visibility = View.VISIBLE
                            availableRidesRecycler.visibility = View.GONE
                        }
                    },
                    onFailure = {}
                )
            },
            onFailure = {}
        )


        return view
    }
}