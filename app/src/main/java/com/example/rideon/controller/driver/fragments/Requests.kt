package com.example.rideon.controller.driver.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.rideon.R
import com.example.rideon.controller.driver.Config.DRIVER_NOT_FOUND
import com.example.rideon.controller.driver.adapters.Requests
import com.example.rideon.model.database.firebase.DriverManager
import com.example.rideon.model.database.room.RoomAccountManager

class Requests : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(
                R.layout.d_fragment_requests,
                container,
            false)

        val requestsRecycler: RecyclerView = view
            .findViewById(R.id.recycler_requests_fragment_requests)

        //get the driver
        RoomAccountManager.instance.getLoggedInUser(
            fragment = this@Requests,
            onSuccess = { user ->
                        DriverManager.instance.getBookingRequests(
                            driverId = user.userId,
                            onSuccess = {},
                            onFailure = {}
                        )

                DriverManager.instance.getBookingRequests(
                    driverId = user.userId,
                    onSuccess = {
                        requestsRecycler.adapter = Requests(this@Requests, it.toMutableList())
                    },
                    onFailure = {}
                )
            },
            onFailure = {
                Toast.makeText(requireActivity(),
                DRIVER_NOT_FOUND,
                Toast.LENGTH_SHORT).show()
            }
        )
        return view
    }
}