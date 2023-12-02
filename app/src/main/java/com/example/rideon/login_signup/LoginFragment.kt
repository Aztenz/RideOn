package com.example.rideon.login_signup

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
import com.example.rideon.MainActivity
import com.example.rideon.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        //initiating firebase auth
        auth = Firebase.auth

        // Get View Elements
        val email: EditText = view.findViewById(R.id.edit_text_email_fragment_login)
        val password: EditText = view.findViewById(R.id.edit_text_password_fragment_login)
        val login: Button = view.findViewById(R.id.button_login_fragment_login)
        val signup: Button = view.findViewById(R.id.button_signup_fragment_login)

        login.setOnClickListener {
            val userText: String = email.text.toString()
            val passText: String = password.text.toString()

            Log.d("myapp101", "User: $userText")
            Log.d("myapp101", "Pass: $passText")

            auth.signInWithEmailAndPassword(userText, passText)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("myapp101", "createUser:success")

                        val intent = Intent(requireActivity(), MainActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("myapp101", "createUser:failure", task.exception)
                        Toast.makeText(requireActivity(),
                            "Error! wrong credentials.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
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