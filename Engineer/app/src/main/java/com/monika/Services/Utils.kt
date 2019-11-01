package com.monika.Services

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.util.TypedValue
import android.widget.ImageView
import com.monika.R


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

    fun getCategoryImage(category: String) : Int {
        return when (category) {
            "Biceps" -> (R.drawable.icons8biceps100)
            "Barki" -> (R.drawable.icons8shoulders100)
            "Plecy" -> (R.drawable.icons8torso100)
            "Nogi" -> (R.drawable.icons8leg100)
            "Pośladki" -> (R.drawable.icons8glutes100)
            "Brzuch" -> (R.drawable.icons8prelum100)
            "Triceps" -> (R.drawable.icons8triceps100)
            "Klatka piersiowa" -> (R.drawable.icons8chest100)
            else -> (R.drawable.icons8weightlifting100)
        }
    }
}