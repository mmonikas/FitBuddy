package com.monika.HomeScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.monika.Model.WorkoutPlan.Workout


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
            putString("exerciseName", workouts[0].name)
        }
        return fragment
    }

    override fun getPageTitle(position: Int): CharSequence {
        return "OBJECT ${(position + 1)}"
    }
}

