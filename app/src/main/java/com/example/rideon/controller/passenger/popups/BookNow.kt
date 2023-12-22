package com.example.rideon.controller.passenger.popups

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView
import com.example.rideon.R
import com.example.rideon.model.data_classes.Ride
import com.example.rideon.model.data_classes.User
import com.example.rideon.model.database.firebase.PassengerManager

class BookNow private constructor(private val context: Context) {

    private var popupWindow: PopupWindow? = null
    private var dismissListener: (() -> Unit)? = null

    fun setOnDismissListener(listener: () -> Unit) {
        this.dismissListener = listener
    }

    @SuppressLint("InflateParams")
    fun showPopup(
        anchorView: View,
        passenger: User,
        ride: Ride,
        onBooked: () -> Unit
    ) {
        val inflater = LayoutInflater.from(context)
        val popupView = inflater.inflate(R.layout.p_popup_book_now, null)

        val darkBackground = createDarkBackground()
        showDarkBackground(darkBackground)

        popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        popupWindow?.elevation = 10.0F

        popupWindow?.setOnDismissListener {
            removeDarkBackground(darkBackground)
            dismissListener?.invoke()
        }

        val closBtn = popupView.findViewById<Button>(R.id.button_cancel_popup_bn)
        val bookNowBtn: Button = popupView.findViewById(R.id.button_bn_popup_bn)
        val newBalanceTV: TextView = popupView.findViewById(R.id.tv_new_balance_popup_bn)

        newBalanceTV.text = (passenger.walletBalance - ride.price).toString()

        closBtn.setOnClickListener {
            popupWindow?.dismiss()
        }

        bookNowBtn.setOnClickListener {
            bookNowBtn.isEnabled = false
            PassengerManager.instance.bookRide(passenger, ride, onBooked = {onBooked.invoke()})
        }



        popupWindow?.showAtLocation(anchorView, Gravity.CENTER, 0, 0)
    }

    private fun createDarkBackground(): View {
        val darkBackground = View(context)
        darkBackground.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        darkBackground.setBackgroundColor(context.getColor(R.color.semi_transparent_dark))
        return darkBackground
    }

    private fun showDarkBackground(darkBackground: View) {
        (getActivityRootView() as ViewGroup).addView(darkBackground)
    }

    private fun removeDarkBackground(darkBackground: View) {
        (getActivityRootView() as ViewGroup).removeView(darkBackground)
    }

    private fun getActivityRootView(): View {
        return (context as Activity).findViewById(android.R.id.content)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: BookNow? = null

        fun getInstance(context: Context): BookNow {
            return instance ?: synchronized(this) {
                instance ?: BookNow(context).also { instance = it }
            }
        }
    }
}