package com.example.rideon.controller.passenger.popups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.rideon.R
import com.example.rideon.controller.passenger.Config
import com.example.rideon.model.data_classes.User
import com.example.rideon.model.database.firebase.PassengerManager
import com.example.rideon.model.database.room.RoomAccountManager
import com.example.rideon.utilities.NetworkUtils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class Wallet(
    private val networkUtils: NetworkUtils,
    private val passenger: User
) : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.p_popup_wallet, container, false)
        val addCreditsBtn: Button = view.findViewById(R.id.button_add_credits)
        val confirmBtn: Button = view.findViewById(R.id.button_confirm)
        val balanceTV: TextView = view.findViewById(R.id.text_view_balance)
        val amountToAddEdit: EditText = view.findViewById(R.id.edit_amount)

        balanceTV.text = passenger.walletBalance.toString()
        addCreditsBtn.setOnClickListener {
            addCreditsBtn.isEnabled = false
            confirmBtn.visibility = View.VISIBLE
            amountToAddEdit.visibility = View.VISIBLE
        }

        confirmBtn.setOnClickListener {
            val amountToAdd = amountToAddEdit.text
            if(amountToAdd.isNullOrBlank() || amountToAdd.toString().toDouble() < 0) {
                amountToAddEdit.error = "enter a valid amount"
                return@setOnClickListener
            }

            val newBalance = passenger.walletBalance + amountToAdd.toString().toDouble()
            PassengerManager.instance.updatePassengerWallet(
                userId = passenger.userId,
                newBalance = newBalance
                )
            RoomAccountManager.instance.updateUserWallet(passenger, newBalance)
            balanceTV.text = newBalance.toString()
        }


        // Observe network status
        networkUtils.isConnected.observe(viewLifecycleOwner) { isConnected ->
            if (isConnected) {
                if(!addCreditsBtn.isEnabled)
                    confirmBtn.isEnabled = true
            } else {
                confirmBtn.isEnabled = false
                Toast.makeText(requireActivity(),
                    Config.NO_INTERNET,
                    Toast.LENGTH_LONG).show()
            }
        }

        return view
    }
}