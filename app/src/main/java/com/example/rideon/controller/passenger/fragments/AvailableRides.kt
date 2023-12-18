package com.example.rideon.controller.passenger.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rideon.R
import com.example.rideon.controller.passenger.adapters.AvailableRides

import com.example.rideon.model.database.firebase.PassengerManager
import com.example.rideon.model.database.room.RoomAccountManager
import com.google.firebase.firestore.auth.User

class AvailableRides : Fragment() {

    private lateinit var availableRidesRecycler: RecyclerView
    private lateinit var roomDBManager: RoomAccountManager
    private lateinit var me: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(
            R.layout.p_fragment_available_routes,
            container, false)
        availableRidesRecycler = view.findViewById(R.id.recycler_available_routes_fragment_ar)

        roomDBManager = RoomAccountManager.instance

        roomDBManager.getLoggedInUser(
            fragment = this@AvailableRides,
            onSuccess = { me ->
                PassengerManager.instance.getAvailableRides(
                    onSuccess = {
                        availableRidesRecycler.adapter =
                            AvailableRides(it, this.context, me)
                    },
                    onFailure = {}
                )
            },
            onFailure = {}
        )






        return view
    }
}