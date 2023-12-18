package com.example.rideon.controller.passenger.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.rideon.R
import com.example.rideon.controller.passenger.adapters.RidesType

class Home : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(
            R.layout.p_fragment_home,
            container, false)

        //Handle Recyclers
        val rideTypesRecycler: RecyclerView = view
            .findViewById(R.id.recycler_rides_type_fragment_home)

        rideTypesRecycler.adapter = RidesType(
            view.findViewById(R.id.recycler_available_rides_fragment_home),
            this)


        //Handle Welcome message, Email Text view and account button
        val mailTV: TextView = view.findViewById(R.id.tv_email_fragment_home)
        val welcomeTV: TextView = view.findViewById(R.id.tv_welcome_fragment_home)
        val accountBtn: ImageButton = view.findViewById(R.id.button_account_fragment_home)
        val uid = context?.getSharedPreferences(
            getString(R.string.user_shared_pref),
            Context.MODE_PRIVATE)?.getString(getString(R.string.user_email),
            "").toString()

//        UserManager.getInstance().getUserData(uid){ data ->
//            val emailText: String = data?.get("email").toString()
//            val welcomeText = context?.getString(R.string.welcome) + " " + data?.get("displayName").toString()
//            welcomeTV.text =  welcomeText
//            mailTV.text = emailText
//
//            accountBtn.setOnClickListener {
//                val bottomSheetFragment = PopupOrderInfo()
//                bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
//            }
//        }
        return view
    }
}