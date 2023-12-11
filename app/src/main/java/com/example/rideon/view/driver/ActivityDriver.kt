package com.example.rideon.view.driver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.rideon.R
import com.example.rideon.databinding.ActivityDriverBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class ActivityDriver : AppCompatActivity() {
    private lateinit var binding: ActivityDriverBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDriverBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Check if the layout is inflated before finding the NavController
        binding.root.post {
            val navView: BottomNavigationView = binding.driverBottomNav
            val navController = findNavController(R.id.driver_fragment_holder)
            navView.setupWithNavController(navController)
        }
    }
}