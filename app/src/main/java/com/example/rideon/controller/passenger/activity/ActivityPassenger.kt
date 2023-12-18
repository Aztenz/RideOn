package com.example.rideon.controller.passenger.activity

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.rideon.R
import com.example.rideon.databinding.PActivityPassengerBinding
import com.example.rideon.model.database.room.RoomAccountManager

class ActivityPassenger : AppCompatActivity() {

    private lateinit var binding: PActivityPassengerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PActivityPassengerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Check if the layout is inflated before finding the NavController
        binding.root.post {
            val navView: BottomNavigationView = binding.passengerBottomNav
            val navController = findNavController(R.id.passenger_fragment_holder)
            navView.setupWithNavController(navController)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        RoomAccountManager.instance.clearRoomDB()
    }
}