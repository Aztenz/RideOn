package com.example.rideon.controller.home.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.rideon.R
import com.example.rideon.databinding.GsActivityGetStartedBinding
import com.example.rideon.controller.home.fragments.Home

class GetStarted : AppCompatActivity() {
    private lateinit var binding: GsActivityGetStartedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = GsActivityGetStartedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            // If the activity is created for the first time, add FragmentA
            val homeFragment = Home()
            supportFragmentManager.beginTransaction()
                .replace(R.id.get_started_fragment_holder, homeFragment)
                .commit()
        }
    }
}