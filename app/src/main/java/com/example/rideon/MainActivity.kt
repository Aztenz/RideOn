package com.example.rideon

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.rideon.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navView: BottomNavigationView = binding.bottomNavView
        val navController = findNavController(R.id.main_activity_fragment_holder)
        navView.setupWithNavController(navController)


        if (savedInstanceState == null) {
            // If the activity is created for the first time, add FragmentA
            val homeFragment = HomeFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_activity_fragment_holder, homeFragment)
                .commit()
        }
    }
}