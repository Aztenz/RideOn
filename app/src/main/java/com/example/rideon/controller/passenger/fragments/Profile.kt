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
import android.widget.Toast
import com.example.rideon.R
import com.example.rideon.controller.home.activity.GetStarted
import com.example.rideon.controller.passenger.Config
import com.example.rideon.controller.passenger.popups.Wallet
import com.example.rideon.model.data_classes.User
import com.example.rideon.model.database.firebase.AccountManager
import com.example.rideon.model.database.room.RoomAccountManager
import com.example.rideon.utilities.NetworkUtils
import com.google.android.material.bottomnavigation.BottomNavigationView

class Profile : Fragment() {
    private val roomAccountManager = RoomAccountManager.instance
    private lateinit var me: User
    private lateinit var networkUtil: NetworkUtils
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.p_fragment_profile,
            container, false)

        networkUtil = NetworkUtils.getInstance(requireContext())

        val bottomNavigationView: BottomNavigationView = requireActivity().findViewById(R.id.passenger_bottom_nav)


        val profileImage: ImageView = view.findViewById(R.id.image_view_profile)
        val nameTV: TextView = view.findViewById(R.id.text_view_name)
        val emailTV: TextView = view.findViewById(R.id.text_view_email)
        val pastOrdersBtn: Button = view.findViewById(R.id.button_po_fragment_profile)
        val myWalletBtn: Button = view.findViewById(R.id.button_wallet_fragment_profile)
        val logoutBtn: Button = view.findViewById(R.id.button_logout_fragment_profile)

        profileImage.setImageResource(R.drawable.account)

        //get user
        roomAccountManager.getLoggedInUser(
            fragment = this@Profile,
            onSuccess = {
                me = it
                nameTV.text = me.name
                emailTV.text = me.email
            },
            onFailure = { AccountManager
                .instance.logoutUser( onSuccess = {}, onFailure = {} ) } )

        // Observe network status
        networkUtil.isConnected.observe(viewLifecycleOwner) {
            if(!it){
                Toast.makeText(requireActivity(),
                    Config.NO_INTERNET,
                    Toast.LENGTH_LONG).show()
            } }


        pastOrdersBtn.setOnClickListener {
            bottomNavigationView.selectedItemId = R.id.navigation_order_history
        }

        myWalletBtn.setOnClickListener {
            val wallet = Wallet(
                networkUtils = networkUtil,
                passenger = me
            )
            wallet.show(childFragmentManager, wallet.tag)
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