package com.example.rideon.controller.passenger.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.rideon.R
import com.example.rideon.controller.passenger.Config
import com.example.rideon.controller.passenger.adapters.RidesType
import com.example.rideon.model.data_classes.User
import com.example.rideon.model.database.firebase.AccountManager
import com.example.rideon.model.database.room.RoomAccountManager
import com.example.rideon.utilities.NetworkUtils
import com.google.android.material.bottomnavigation.BottomNavigationView

class Home : Fragment() {
    private val roomAccountManager = RoomAccountManager.instance
    private lateinit var me: User
    private lateinit var networkUtil: NetworkUtils

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(
            R.layout.p_fragment_home,
            container, false)
        networkUtil = NetworkUtils.getInstance(requireContext())

        //Handle Welcome message, Email Text view and account button
        val emailTV: TextView = view.findViewById(R.id.text_view_email)
        val nameTV: TextView = view.findViewById(R.id.text_view_name)

        //get user
        roomAccountManager.getLoggedInUser(
            fragment = this@Home,
            onSuccess = {
                me = it
                nameTV.text = me.name.split(" ")[0]
                emailTV.text = me.email

                //Handle Recyclers
                val rideTypesRecycler: RecyclerView = view
                    .findViewById(R.id.recycler_rides_type)

                rideTypesRecycler.adapter = RidesType(
                    availableRidesRecycler = view.findViewById(R.id.recycler_available_rides),
                    fragment = this,
                    passenger = me)

            },
            onFailure = { AccountManager
                .instance.logoutUser( onSuccess = {}, onFailure = {} ) } )

        // Observe network status
        networkUtil.isConnected.observe(viewLifecycleOwner) {
            if(!it)
                Toast.makeText(requireActivity(),
                    Config.NO_INTERNET,
                    Toast.LENGTH_LONG).show()
        }

        val bottomNavigationView: BottomNavigationView = requireActivity()
            .findViewById(R.id.passenger_bottom_nav)



        val accountBtn: ImageButton = view.findViewById(R.id.button_account)
        accountBtn.setOnClickListener {
            bottomNavigationView.selectedItemId = R.id.navigation_p_profile}


        return view
    }
}