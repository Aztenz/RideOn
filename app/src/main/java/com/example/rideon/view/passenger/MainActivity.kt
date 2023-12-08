package com.example.rideon.view.passenger

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.rideon.R
import com.example.rideon.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Check if the layout is inflated before finding the NavController
        binding.root.post {
            val navView: BottomNavigationView = binding.bottomNavView
            val navController = findNavController(R.id.main_activity_fragment_holder)
            navView.setupWithNavController(navController)
        }
    }
}