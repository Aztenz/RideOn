package com.example.rideon.controller.home.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import com.example.rideon.R
import com.example.rideon.controller.home.Config
import com.example.rideon.model.database.firebase.AccountManager
import com.example.rideon.utilities.Utils
import com.google.android.material.textfield.TextInputLayout

class Signup : Fragment() {
    object Constants {
        const val REQUIRED_FIELD = "this field is required"
        const val EMAIL_DOMAIN = "@eng.asu.edu.eg"
        const val EMAIL_DOMAIN_CHECK = "email must end with $EMAIL_DOMAIN"
        const val PASSWORD_LENGTH_ERROR = "password should be at least 6 characters"
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.gs_fragment_signup, container, false)


        val name: EditText = view.findViewById(R.id.edit_name)
        val email: EditText = view.findViewById(R.id.edit_email)
        val password: EditText = view.findViewById(R.id.edit_password)

        val nameInput: TextInputLayout = view.findViewById(R.id.input_name)
        val emailInput: TextInputLayout = view.findViewById(R.id.input_email)
        val passInput: TextInputLayout = view.findViewById(R.id.input_password)

        val signup: Button = view.findViewById(R.id.button_signup)
        val login: Button = view.findViewById(R.id.button_login)

        val passengerRadio: RadioButton  = view.findViewById(R.id.radio_passenger)
        val driverRadio: RadioButton  = view.findViewById(R.id.radio_driver)



        signup.setOnClickListener {
            signup.isEnabled = false
            Utils.instance.hideKeyboard(requireActivity() ,view)

            val nameText: String = name.text.toString().trim()
            val emailText: String = email.text.toString().trim()
            val passText: String = password.text.toString().trim()

            val nameEmpty = nameText.isEmpty()
            val userEmpty = emailText.isEmpty()
            val passEmpty = passText.isEmpty()

            nameInput.error = if (nameEmpty) Constants.REQUIRED_FIELD else null
            emailInput.error = if (userEmpty) Constants.REQUIRED_FIELD else null
            passInput.error = if (passEmpty) Constants.REQUIRED_FIELD else null

            if (nameEmpty || userEmpty || passEmpty) {
                signup.isEnabled = true
                return@setOnClickListener
            }

            if (!(emailText.endsWith(Constants.EMAIL_DOMAIN))) {
                emailInput.error = Constants.EMAIL_DOMAIN_CHECK
                signup.isEnabled = true
                return@setOnClickListener
            }

            if(passText.length<6){
                passInput.error = Constants.PASSWORD_LENGTH_ERROR
                signup.isEnabled = true
                return@setOnClickListener
            }

            AccountManager.instance.registerUser(
                displayName = nameText,
                email = emailText,
                password = passText,
                userType = when {
                    passengerRadio.isChecked -> "passengers"
                    driverRadio.isChecked -> "drivers"
                    else -> Config.ROLE_PASSENGER
                },
                onSuccess = { toLogin() },
                onFailure = {
                    Toast.makeText(
                        requireActivity(),
                        it.message,
                        Toast.LENGTH_SHORT,
                    ).show()
                    signup.isEnabled = true
                })
        }

        login.setOnClickListener {
            toLogin()
        }

        return view
    }

    private fun toLogin(){
        val loginFragment = Login()
        val transaction = requireActivity().supportFragmentManager.beginTransaction()

        // Replace Get Started Fragment with Login Fragment
        transaction.replace(R.id.get_started_fragment_holder, loginFragment)

        // Commit the transaction
        transaction.commit()
    }
}