package com.monika.HomeScreen

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.monika.Model.WorkoutPlan.Workout
import com.monika.R
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class HomeFragmentPagerAdapter(fm: FragmentManager) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    val ARG_OBJECT = "object"
    var workouts = ArrayList<Workout>()

    override fun getCount(): Int {
        return 10
    }

    override fun getItem(position: Int): Fragment {
        val fragment = CalendarDayFragment()
        fragment.arguments = Bundle().apply {
            // Our object is just an integer :-P
            putInt(ARG_OBJECT, position + 1)
            if (workouts.isNotEmpty()) {
                putString("exerciseName", workouts[0].name)
            }

        }
        return fragment
    }

    override fun getPageTitle(position: Int): CharSequence {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val date =  LocalDate.now().plusDays(position.toLong())
            var formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            var formattedDate = date.format(formatter)
            "$formattedDate"
        } else {
            "$position"
        }

    }
}

