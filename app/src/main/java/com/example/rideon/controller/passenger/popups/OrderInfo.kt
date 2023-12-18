package com.example.rideon.controller.passenger.popups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.rideon.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class OrderInfo : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.p_popup_order_info, container, false)



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val trackBtn: Button = view.findViewById(R.id.button_track_fragment_order_info)
        trackBtn.setOnClickListener {
            val orderStatus = OrderStatus()
            orderStatus.show(childFragmentManager, orderStatus.tag)
        }
    }
}
