package com.example.rideon.view.login_signup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.rideon.view.passenger.MainActivity
import com.example.rideon.R
import com.example.rideon.model.database.UserManager


class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        // Get View Elements
        val email: EditText = view.findViewById(R.id.edit_text_email_fragment_login)
        val password: EditText = view.findViewById(R.id.edit_text_password_fragment_login)
        val login: Button = view.findViewById(R.id.button_login_fragment_login)
        val signup: Button = view.findViewById(R.id.button_signup_fragment_login)

        login.setOnClickListener {
            login.isEnabled = false
            val userText: String = email.text.toString()
            val passText: String = password.text.toString()

            if(userText.isEmpty() || passText.isEmpty()) {
                login.isEnabled = true
                return@setOnClickListener
            }


            Log.d("myapp101", "User: $userText")
            Log.d("myapp101", "Pass: $passText")

            UserManager.getInstance().loginUser(
                userText,
                passText,
                onSuccess = { uid ->
                    Log.d("myapp101", "createUser:success")
                    val intent = Intent(requireActivity(), MainActivity::class.java)
                    startActivity(intent)

                    val sharedPrefEditor = context?.getSharedPreferences(
                        getString(R.string.user_shared_pref),
                        Context.MODE_PRIVATE
                    )?.edit()

                    sharedPrefEditor?.putString(getString(R.string.user_email), uid)
                    sharedPrefEditor?.apply()

                    requireActivity().finish()
                },
                onFailure = {
                    // If sign in fails, display a message to the user.
                    Log.w("myapp101", "createUser:failure", it)
                    Toast.makeText(requireActivity(),
                        "Error! wrong credentials.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    login.isEnabled = true
                }
            )
        }

        signup.setOnClickListener {
            val signupFragment = SignupFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()

            // Replace Get Started Fragment with Login Fragment
            transaction.replace(R.id.get_started_activity_fragment_holder, signupFragment)

            // Commit the transaction
            transaction.commit()
        }

        return view
    }
}