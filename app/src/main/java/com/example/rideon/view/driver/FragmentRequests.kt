package com.example.rideon.view.driver

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rideon.R

class FragmentRequests : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(
                R.layout.fragment_requests,
                container,
            false)

        val requestsRecycler: RecyclerView = view
            .findViewById(R.id.recycler_requests_fragment_requests)



        return view
    }
}