package com.example.rideon.view.login_signup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.rideon.R
import com.example.rideon.databinding.ActivityGetStartedBinding

class GetStartedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGetStartedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGetStartedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            // If the activity is created for the first time, add FragmentA
            val getStartedFragment = GetStartedFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.get_started_activity_fragment_holder, getStartedFragment)
                .commit()
        }
    }
}