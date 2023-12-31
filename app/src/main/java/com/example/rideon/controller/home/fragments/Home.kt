package com.example.rideon.controller.home.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.rideon.R

class Home : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.gs_fragment_home, container, false)

        // Find the button by its ID
        val getStartedBtn: Button = view.findViewById(R.id.button_get_started)

        // Set a click listener for the button
        getStartedBtn.setOnClickListener {

            val loginFragment = Login()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()

            // Replace Get Started Fragment with Login Fragment
            transaction.replace(R.id.get_started_fragment_holder, loginFragment)

            // Add the transaction to the back stack
            transaction.addToBackStack(null)

            // Commit the transaction
            transaction.commit()
        }

        return view
    }
}