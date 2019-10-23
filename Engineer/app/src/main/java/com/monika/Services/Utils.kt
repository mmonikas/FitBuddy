package com.monika.Services

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.util.TypedValue



object Utils {

    fun hideSoftKeyBoard(context: Context, view: View) {
        try {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun getValueInDP(context: Context, value: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value.toFloat(), context.resources.displayMetrics)
            .toInt()
    }

    fun getValueInDP(context: Context, value: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.resources.displayMetrics)
    }

    // value in PX
    fun getValueInPixel(context: Context, value: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, value.toFloat(), context.resources.displayMetrics)
            .toInt()
    }

    fun getValueInPixel(context: Context, value: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, value, context.resources.displayMetrics)
    }
}