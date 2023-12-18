package com.example.rideon.utilities

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

class Utils private constructor() {

    companion object {
        val instance: Utils by lazy { Utils() }
    }

    fun hideKeyboard(context: Context, view: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
