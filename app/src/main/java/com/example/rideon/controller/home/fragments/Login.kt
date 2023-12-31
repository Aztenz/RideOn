package com.example.rideon.controller.home.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RadioGroup
import android.widget.Toast
import com.example.rideon.R
import com.example.rideon.model.database.firebase.AccountManager
import com.example.rideon.controller.driver.activity.Driver
import com.example.rideon.controller.home.Config
import com.example.rideon.utilities.Utils
import com.example.rideon.controller.passenger.activity.ActivityPassenger
import com.example.rideon.model.data_classes.User
import com.example.rideon.model.database.room.RoomAccountManager
import com.example.rideon.utilities.NetworkUtils
import com.google.android.material.textfield.TextInputLayout


class Login : Fragment() {

    private val accountManager = AccountManager.instance
    private val roomAccountManager = RoomAccountManager.instance
    private lateinit var networkUtil: NetworkUtils
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.gs_fragment_login, container, false)
        val login: Button = view.findViewById(R.id.button_login)
        val signup: Button = view.findViewById(R.id.button_signup)
        val progressBar: ProgressBar = view.findViewById(R.id.progress_bar)
        val layout: LinearLayout = view.findViewById(R.id.login_layout)
        networkUtil = NetworkUtils.getInstance(requireContext())

        //handle if the user is already logged in the system
        roomAccountManager.getUserCountFromRoom(
            fragment = this@Login,
            onSuccess = { isUserLogged ->
                handleAlreadyLoggedInUser(
                    isUserLogged = isUserLogged>0,
                    loginBtn = login,
                    onFailure = {
                        progressBar.visibility = View.GONE
                        layout.visibility = View.VISIBLE
                        // Observe network status
                        networkUtil.isConnected.observe(viewLifecycleOwner) { isConnected ->
                            if (isConnected) {
                                login.isEnabled = true
                                signup.isEnabled = true
                            } else {
                                login.isEnabled = false
                                signup.isEnabled = false
                                Toast.makeText(requireActivity(),
                                    "no internet connection",
                                    Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                )
            }
        )

        val email: EditText = view.findViewById(R.id.edit_email)
        val password: EditText = view.findViewById(R.id.edit_password)

        val emailInput: TextInputLayout = view.findViewById(R.id.input_email)
        val passInput: TextInputLayout = view.findViewById(R.id.input_password)
        val radioGroup: RadioGroup = view.findViewById(R.id.radio_group_user_type)
        val bypassPassengerBtn: Button = view.findViewById(R.id.button_bypass_passenger)
        val bypassDriverBtn: Button = view.findViewById(R.id.button_bypass_driver)

        bypassDriverBtn.setOnClickListener {
            radioGroup.check(R.id.radio_driver)
            bypass(1) }

        bypassPassengerBtn.setOnClickListener {
            radioGroup.check(R.id.radio_passenger)
            bypass(2)
        }


        //ensure that passenger is selected by default
        radioGroup.check(R.id.radio_passenger)



        login.setOnClickListener {
            login.isEnabled = false
            Utils.instance.hideKeyboard(requireActivity(), view)

            val userText: String = email.text.toString().trim()
            val passText: String = password.text.toString().trim()

            val userEmpty = userText.isEmpty()
            val passEmpty = passText.isEmpty()

            val isPassengerChecked = radioGroup.checkedRadioButtonId == R.id.radio_passenger
            val isDriverChecked = radioGroup.checkedRadioButtonId == R.id.radio_driver

            val chosenRole = when {
                isPassengerChecked -> Config.UserRole.PASSENGER
                isDriverChecked -> Config.UserRole.DRIVER
                else -> Config.UserRole.PASSENGER
            }

            emailInput.error = if (userEmpty) Config.REQUIRED_FIELD else null
            passInput.error = if (passEmpty) Config.REQUIRED_FIELD else null

            if (userEmpty || passEmpty) {
                login.isEnabled = true
                return@setOnClickListener
            }

            accountManager.loginUser(
                email = userText,
                password = passText,
                role = chosenRole,
                onSuccess = { user ->
                    grantAccess(
                        user = user,
                        onFailure = {
                            handleLoginFailure(
                                errorMessage = it,
                                loginBtn = login
                            )
                        }
                    )
                },
                onFailure = {
                    handleLoginFailure(
                        errorMessage = it,
                        loginBtn = login
                    )
                }
            )
        }

        signup.setOnClickListener {
            signup.isEnabled = false
            val signupFragment = Signup()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()

            // Replace Get Started Fragment with Login Fragment
            transaction.replace(R.id.get_started_fragment_holder, signupFragment)

            // Commit the transaction
            transaction.commit()
        }

        return view
    }

    private fun handleLoginFailure(
        errorMessage: String,
        loginBtn: Button
    ) {
        loginBtn.isEnabled = true
        Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_SHORT).show()
        accountManager.logoutUser(
            onSuccess = {},
            onFailure = {}
        )
    }

    private fun handleAlreadyLoggedInUser(
        isUserLogged: Boolean,
        loginBtn: Button,
        onFailure: () -> Unit
    ) {
        if (isUserLogged) {
            roomAccountManager.getLoggedInUser(
                fragment = this,
                onFailure = {
                    handleLoginFailure(
                        errorMessage = it,
                        loginBtn = loginBtn)
                },
                onSuccess = { it ->
                    grantAccess(
                        user = it,
                        onFailure = {
                            handleLoginFailure(
                                errorMessage = it,
                                loginBtn = loginBtn)
                        }
                    )
                })
            return
        }
        onFailure()
    }

    private fun grantAccess(
        user: User,
        onFailure: (String) -> Unit
    ) {
        try {
            val intent = when (user.userType) {
                Config.ROLE_PASSENGER -> {
                    Intent(requireActivity(), ActivityPassenger::class.java)
                }
                Config.ROLE_DRIVER -> {
                    Intent(requireActivity(), Driver::class.java)
                }
                else -> throw IllegalArgumentException(Config.INVALID_USER)
            }

            intent.putExtra(Config.INTENT_UID, user.userId)
            intent.putExtra(Config.INTENT_EMAIL, user.email)
            intent.putExtra(Config.INTENT_NAME, user.name)
            intent.putExtra(Config.INTENT_WALLET_BALANCE, user.walletBalance)
            startActivity(intent)
            requireActivity().finish()

        } catch (e: Exception) {
            onFailure.invoke(e.message.toString())
        }
    }

    private fun bypass(type: Int){
        val role = when(type) {
            1 -> Config.UserRole.DRIVER
            else -> Config.UserRole.PASSENGER
        }
        accountManager.loginUser(
            "test@test.com",
            "test123",
            role,
            onSuccess = { grantAccess(it, onFailure = {}) },
            onFailure = {}
        )
    }
}
