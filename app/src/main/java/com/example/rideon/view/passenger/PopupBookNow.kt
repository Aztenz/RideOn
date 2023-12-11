package com.example.rideon.view.passenger

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import com.example.rideon.R

class PopupBookNow private constructor(private val context: Context) {

    private var popupWindow: PopupWindow? = null
    private var dismissListener: (() -> Unit)? = null

    fun setOnDismissListener(listener: () -> Unit) {
        this.dismissListener = listener
    }

    @SuppressLint("InflateParams")
    fun showPopup(anchorView: View) {
        val inflater = LayoutInflater.from(context)
        val popupView = inflater.inflate(R.layout.popup_book_now, null)

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

        val btnClosePopup = popupView.findViewById<Button>(R.id.button_cancel_popup_bn)
        btnClosePopup.setOnClickListener {
            popupWindow?.dismiss()
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
        private var instance: PopupBookNow? = null

        fun getInstance(context: Context): PopupBookNow {
            return instance ?: synchronized(this) {
                instance ?: PopupBookNow(context).also { instance = it }
            }
        }
    }
}