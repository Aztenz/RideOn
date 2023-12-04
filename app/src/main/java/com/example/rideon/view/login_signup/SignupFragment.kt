package com.example.rideon.view.login_signup

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.rideon.R
import com.example.rideon.model.database.UserManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignupFragment : Fragment() {
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_signup, container, false)

        //initiating firebase auth
        auth = Firebase.auth

        // Get View Elements
        val email: EditText = view.findViewById(R.id.edit_text_email_fragment_signup)
        val password: EditText = view.findViewById(R.id.edit_text_password_fragment_signup)
        val signup: Button = view.findViewById(R.id.button_signup_fragment_signup)
        val login: Button = view.findViewById(R.id.button_login_fragment_signup)

        signup.setOnClickListener {
            val userText: String = email.text.toString()
            val passText: String = password.text.toString()

            Log.d("myapp101", "User: $userText")
            Log.d("myapp101", "Pass: $passText")


            if (!(userText.endsWith('@' + "eng.asu.edu.eg"))) {
                Toast.makeText(requireActivity(),
                    "Email must end with @eng.asu.edu.eg",
                    Toast.LENGTH_SHORT,
                ).show()
                return@setOnClickListener
            }

            UserManager.getInstance().registerUser(
                userText,
                passText,
                "User1",
                onSuccess = {
                    Log.d("myapp101", "loginUser:success")
                    toLogin()
                    //val user = auth.currentUser
                },
                onFailure = {
                    Log.w("myapp101", "loginUser:failure", it)
                    Toast.makeText(requireActivity(),
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                },
            )
        }

        login.setOnClickListener {
            toLogin()
        }

        return view
    }

    private fun toLogin(){
        val loginFragment = LoginFragment()
        val transaction = requireActivity().supportFragmentManager.beginTransaction()

        // Replace Get Started Fragment with Login Fragment
        transaction.replace(R.id.get_started_activity_fragment_holder, loginFragment)

        // Commit the transaction
        transaction.commit()
    }
}