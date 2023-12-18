package com.example.rideon.controller.driver.fragments

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
import com.example.rideon.controller.driver.popups.Wallet
import com.example.rideon.controller.driver.popups.OfferedRides
import com.example.rideon.controller.driver.popups.PastOrders
import com.example.rideon.controller.home.activity.GetStarted
import com.example.rideon.model.data_classes.User
import com.example.rideon.model.database.firebase.AccountManager
import com.example.rideon.model.database.firebase.DriverManager
import com.example.rideon.model.database.room.RoomAccountManager
import com.example.rideon.utilities.NetworkUtils

class Profile : Fragment() {

    private lateinit var myButtons: Array<Button>
    private val driverManager = DriverManager.instance
    private val roomAccountManager = RoomAccountManager.instance
    private lateinit var me: User
    private val networkUtil = NetworkUtils.getInstance(requireContext())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(
            R.layout.d_fragment_profile,
            container, false)

        val nameTV: TextView = view.findViewById(R.id.text_view_name)

        //get user
        roomAccountManager.getLoggedInUser(
            fragment = this@Profile,
            onSuccess = { me = it
                        nameTV.text = me.name },
            onFailure = { AccountManager
                .instance.logoutUser( onSuccess = {}, onFailure = {} ) } )


        val profileIV: ImageView = view.findViewById(R.id.image_view_profile)
        val currentRidesBtn: Button = view.findViewById(R.id.button_current_rides)
        val pastOrdersBtn: Button = view.findViewById(R.id.button_past_orders)
        val myWalletBtn: Button = view.findViewById(R.id.button_wallet)
        val logoutBtn: Button = view.findViewById(R.id.button_logout)

        // Observe network status
        networkUtil.isConnected.observe(viewLifecycleOwner) { isConnected ->
            if (isConnected) {
                currentRidesBtn.isEnabled = true
                pastOrdersBtn.isEnabled = true
            } else {
                currentRidesBtn.isEnabled = false
                pastOrdersBtn.isEnabled = false
                Toast.makeText(requireActivity(),
                    "no internet connection",
                    Toast.LENGTH_LONG).show()
            }
        }

        myButtons = arrayOf(
            currentRidesBtn,
            pastOrdersBtn,
            myWalletBtn,
            logoutBtn)

        profileIV.setImageResource(R.drawable.account)

        currentRidesBtn.setOnClickListener {
            switchButtons()

            driverManager.getDriverCurrentRides(
                driverId = me.userId,
                onSuccess = {
                    val popupOfferedRides = OfferedRides(
                        offeredRides = it,
                        profileFragment = this )
                    popupOfferedRides.show(childFragmentManager, popupOfferedRides.tag) },
                onFailure = {}
            )
        }

        pastOrdersBtn.setOnClickListener {
            switchButtons()
            driverManager.getDriverPastRides(
                driverId = me.userId,
                onSuccess = {
                    val popupPastOrders = PastOrders(
                        pastOrders = it,
                        profileFragment = this )
                    popupPastOrders.show(childFragmentManager, popupPastOrders.tag) },
                onFailure = {}
            )
        }

        myWalletBtn.setOnClickListener {
            switchButtons()
            val popupMyWallet = Wallet(
                userId = me.userId,
                balance = me.walletBalance,
                profileFragment = this)
            popupMyWallet.show(childFragmentManager, popupMyWallet.tag)
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

    fun switchButtons(){
        for (button in this.myButtons)
            button.isEnabled = !button.isEnabled
    }
}