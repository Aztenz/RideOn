package com.example.rideon.controller.passenger.fragments

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.rideon.R
import com.example.rideon.controller.passenger.Config
import com.example.rideon.model.data_classes.User
import com.example.rideon.model.database.firebase.PassengerManager
import com.example.rideon.model.database.room.RoomAccountManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileSettings : Fragment() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var roomAccountManager: RoomAccountManager
    private lateinit var me: User
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(
            R.layout.p_fragment_profile_settings,
            container,
            false)

        roomAccountManager = RoomAccountManager.instance

        bottomNavigationView = requireActivity().findViewById(R.id.passenger_bottom_nav)

        val editBtn: ImageButton = view.findViewById(R.id.button_edit_fragment_ps)
        val name: EditText = view.findViewById(R.id.edit_name_fragment_ps)
        val phone: EditText = view.findViewById(R.id.edit_phone_fragment_ps)
        val email: EditText = view.findViewById(R.id.edit_email_fragment_ps)
        val confirm: Button = view.findViewById(R.id.button_bn_popup_bn)
        val cancel: Button = view.findViewById(R.id.button_cancel_popup_bn)

        roomAccountManager.getLoggedInUser(
            fragment = this,
            onSuccess = {me = it},
            onFailure = {}
            )


        editBtn.setOnClickListener {
            confirm.isEnabled = !confirm.isEnabled
            cancel.isEnabled = !cancel.isEnabled
            name.isEnabled = !name.isEnabled
            phone.isEnabled = !phone.isEnabled
            email.isEnabled = !email.isEnabled
        }

        confirm.setOnClickListener {
            val emailText = email.text
            val phoneNum = phone.text
            val nameText = name.text
            if(emailText.isNullOrBlank() || phoneNum.isNullOrBlank() || nameText.isNullOrBlank()) {
                Toast.makeText(this.requireContext(),
                    Config.REQUIRED_FIELDS,
                    Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val updated = User(
                userId = me.userId,
                email = emailText.toString(),
                name = nameText.toString(),
                walletBalance = me.walletBalance,
                userType = "passengers")
            PassengerManager.instance.updatePassengerData(updated)
            roomAccountManager.updateUser(updated)
        }

        cancel.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }



        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        bottomNavigationView.visibility = View.VISIBLE
    }
}