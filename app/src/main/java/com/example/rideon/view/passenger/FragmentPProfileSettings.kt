package com.example.rideon.view.passenger

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
import androidx.annotation.RequiresApi
import com.example.rideon.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class FragmentPProfileSettings : Fragment() {
    private lateinit var bottomNavigationView: BottomNavigationView
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(
            R.layout.fragment_p_profile_settings,
            container,
            false)

        bottomNavigationView = requireActivity().findViewById(R.id.passenger_bottom_nav)

        val editBtn: ImageButton = view.findViewById(R.id.button_edit_fragment_ps)
        val name: EditText = view.findViewById(R.id.edit_name_fragment_ps)
        val phone: EditText = view.findViewById(R.id.edit_phone_fragment_ps)
        val email: EditText = view.findViewById(R.id.edit_email_fragment_ps)
        val confirm: Button = view.findViewById(R.id.button_bn_popup_bn)
        val cancel: Button = view.findViewById(R.id.button_cancel_popup_bn)


        editBtn.setOnClickListener {
            confirm.isEnabled = true
            cancel.isEnabled = true
            name.isEnabled = true
            phone.isEnabled = true
            email.isEnabled = true
        }

        confirm.setOnClickListener {

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