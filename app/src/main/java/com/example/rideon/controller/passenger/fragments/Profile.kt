package com.example.rideon.controller.passenger.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import com.example.rideon.R
import com.example.rideon.controller.home.activity.GetStarted
import com.example.rideon.controller.passenger.popups.Wallet
import com.example.rideon.model.database.firebase.AccountManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class Profile : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.p_fragment_profile,
            container, false)
        val bottomNavigationView: BottomNavigationView = requireActivity().findViewById(R.id.passenger_bottom_nav)
        val navController = requireActivity().findNavController(R.id.passenger_fragment_holder)


        val profileImage: ImageView = view.findViewById(R.id.iv_profile_image_fragment_profile)
        val nameTV: TextView = view.findViewById(R.id.tv_name_fragment_profile)
        val pastOrdersBtn: Button = view.findViewById(R.id.button_po_fragment_profile)
        val myWalletBtn: Button = view.findViewById(R.id.button_wallet_fragment_profile)
        val settingBtn: Button = view.findViewById(R.id.button_settings_fragment_profile)
        val logoutBtn: Button = view.findViewById(R.id.button_logout_fragment_profile)


        profileImage.setImageResource(R.drawable.account)
        nameTV.text = "Omar Mohamed"


        pastOrdersBtn.setOnClickListener {
            bottomNavigationView.selectedItemId = R.id.navigation_order_history
        }

        myWalletBtn.setOnClickListener {
            val wallet = Wallet()
            wallet.show(childFragmentManager, wallet.tag)
        }

        settingBtn.setOnClickListener {
            bottomNavigationView.visibility = View.GONE
            navController.navigate(R.id.navigate_profile_to_profile_settings)
        }

        logoutBtn.setOnClickListener {
            AccountManager.instance.logoutUser(
                onSuccess = {
                    val intent = Intent(requireActivity(), GetStarted::class.java)
                    startActivity(intent)
                },
                onFailure = {}
            )
        }

        return view
    }
}