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
            "biceps" -> (R.drawable.icons8biceps100)
            "shoulders" -> (R.drawable.icons8shoulders100)
            "back" -> (R.drawable.icons8neck100)
            "hamstrings" -> (R.drawable.icons8hamstrings100)
            "calves" ->(R.drawable.icons8calves100)
            "legs" -> (R.drawable.icons8leg100)
            "glutes" -> (R.drawable.icons8glutes100)
            "ABS" -> (R.drawable.icons8prelum100)
            "triceps" -> (R.drawable.icons8triceps100)
            "chest" -> (R.drawable.icons8chest100)
            "cardio" -> (R.drawable.icons8weightlifting100)
            "bodyweight" -> (R.drawable.icons8weightlifting100)
            else -> (R.drawable.icons8weightlifting100)
        }
    }
}