package com.example.rideon.view.driver

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rideon.R
import com.example.rideon.adapters.profile_fragment.OfferedRidesAdapter
import com.example.rideon.model.database.RoutesManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.Date

class PopupOfferedRides : BottomSheetDialogFragment() {
    private val rideList = listOf(
        RoutesManager.Ride("LocationA", "LocationB", Date(), 20.0, 1, 0),
        RoutesManager.Ride("LocationC", "LocationD", Date(), 15.0, 2, 1),
        RoutesManager.Ride("LocationE", "LocationF", Date(), 25.0, 1, 1),
        RoutesManager.Ride("LocationG", "LocationH", Date(), 18.0, 2, 2),
        RoutesManager.Ride("LocationI", "LocationJ", Date(), 30.0, 1, 1),
        RoutesManager.Ride("LocationK", "LocationL", Date(), 22.0, 2, 1),
        RoutesManager.Ride("LocationM", "LocationN", Date(), 28.0, 1, 1),
        RoutesManager.Ride("LocationO", "LocationP", Date(), 14.0, 2, 2),
        RoutesManager.Ride("LocationQ", "LocationR", Date(), 35.0, 1, 4),
        RoutesManager.Ride("LocationS", "LocationT", Date(), 19.0, 2, 5)
    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.popup_current_offered_rides, container, false)
        val offeredRidesRecycler: RecyclerView = view.findViewById(R.id.recycler_offered_rides)
        offeredRidesRecycler.adapter = OfferedRidesAdapter(rideList)
        return view
    }
}
