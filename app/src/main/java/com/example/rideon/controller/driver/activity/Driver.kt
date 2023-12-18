package com.example.rideon.controller.driver.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.rideon.R
import com.example.rideon.databinding.DActivityDriverBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class Driver : AppCompatActivity() {
    private lateinit var binding: DActivityDriverBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DActivityDriverBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Check if the layout is inflated before finding the NavController
        binding.root.post {
            val navView: BottomNavigationView = binding.driverBottomNav
            val navController = findNavController(R.id.driver_fragment_holder)
            navView.setupWithNavController(navController)
        }
    }

}