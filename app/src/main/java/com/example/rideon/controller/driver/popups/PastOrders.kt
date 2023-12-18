package com.example.rideon.controller.driver.popups

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rideon.R
import com.example.rideon.controller.driver.adapters.PastOrders
import com.example.rideon.controller.driver.fragments.Profile
import com.example.rideon.model.data_classes.Ride
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PastOrders(
    private val pastOrders: List<Ride>,
    private val profileFragment: Profile
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.d_popup_past_orders, container, false)

        val pastOrdersRecycler: RecyclerView = view.findViewById(R.id.recycler_past_orders)
        val noPastRides: TextView = view.findViewById(R.id.text_view_no_rides)

        if (pastOrders.isEmpty()) {
            pastOrdersRecycler.visibility = View.GONE
            noPastRides.visibility = View.VISIBLE
            return view
        }

        pastOrdersRecycler.adapter = PastOrders(pastOrders = pastOrders)

        return view
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        profileFragment.switchButtons()
    }
}
