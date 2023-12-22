package com.example.rideon.controller.driver.popups

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.rideon.R
import com.example.rideon.controller.driver.fragments.Profile
import com.example.rideon.model.database.firebase.DriverManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class Wallet(
    private val balance: Double,
    private val profileFragment: Profile
) : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.d_popup_wallet, container, false)
        val balanceTV: TextView = view.findViewById(R.id.text_view_balance)

        balanceTV.text = balance.toString()

        return view
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        profileFragment.switchButtons()
    }
}
