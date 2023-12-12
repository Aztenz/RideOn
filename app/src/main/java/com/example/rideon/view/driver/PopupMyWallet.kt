package com.example.rideon.view.driver

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rideon.R
import com.example.rideon.adapters.profile_fragment.OfferedRidesAdapter
import com.example.rideon.model.database.RoutesManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.Date

class PopupMyWallet : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.popup_driver_wallet, container, false)
        val balanceTV: TextView = view.findViewById(R.id.text_view_balance)
        balanceTV.text = "14"
        return view
    }
}
