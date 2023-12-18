package com.example.rideon.controller.driver.popups

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rideon.R
import com.example.rideon.controller.driver.adapters.OfferedRides
import com.example.rideon.controller.driver.fragments.Profile
import com.example.rideon.model.data_classes.Ride
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class OfferedRides(private val offeredRides: List<Ride>,
                   private val profileFragment: Profile) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.d_popup_offered_rides, container, false)

        val offeredRidesRecycler: RecyclerView = view.findViewById(R.id.recycler_offered_rides)
        val noOfferedRides: TextView = view.findViewById(R.id.text_view_no_rides)

        if (offeredRides.isEmpty()) {
            offeredRidesRecycler.visibility = View.GONE
            noOfferedRides.visibility = View.VISIBLE
            return view
        }

        offeredRidesRecycler.adapter = OfferedRides(offeredRides = offeredRides)


        return view
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        profileFragment.switchButtons()
    }
}
