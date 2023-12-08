package com.example.rideon.view.passenger


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rideon.R
import com.example.rideon.adapters.order_history_fragment.CurrentActiveOrderAdapter
import com.example.rideon.model.database.RoutesManager

class OrderHistoryFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(
            R.layout.fragment_order_history,
            container, false)

        val activeRideRecycler: RecyclerView = view
            .findViewById(R.id.recycler_active_ride_fragment_oh)

        RoutesManager.getInstance().getAvailableRides(
            onFailure = {},
            onSuccess = {
                activeRideRecycler.adapter = CurrentActiveOrderAdapter(it[0], this.context)
            })

        return view
    }
}