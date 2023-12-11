package com.example.rideon.view.driver

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.rideon.R

class FragmentDProfile : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(
            R.layout.fragment_d_profile,
            container, false)

        val profileIV: ImageView = view.findViewById(R.id.image_view_profile)
        val nameTV: TextView = view.findViewById(R.id.text_view_name)
        val currentRidesBtn: Button = view.findViewById(R.id.button_current_rides)
        val pastOrdersBtn: Button = view.findViewById(R.id.button_past_orders)
        val myWalletBtn: Button = view.findViewById(R.id.button_wallet)
        val logoutBtn: Button = view.findViewById(R.id.button_logout)


        profileIV.setImageResource(R.drawable.account)
        nameTV.text = "Omar Mohamed"

        currentRidesBtn.setOnClickListener {

        }

        pastOrdersBtn.setOnClickListener {

        }

        myWalletBtn.setOnClickListener {

        }

        logoutBtn.setOnClickListener {

        }

        return view
    }
}