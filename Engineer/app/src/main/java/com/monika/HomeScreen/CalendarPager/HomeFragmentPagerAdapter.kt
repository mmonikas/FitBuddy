package com.monika.HomeScreen.CalendarPager

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.monika.Model.WorkoutComponents.Exercise
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class HomeFragmentPagerAdapter(fm: FragmentManager) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    val ARG_OBJECT = "object"
    var exercises = ArrayList<Exercise>()

    override fun getCount(): Int {
        return 40
    }

    override fun getItem(position: Int): Fragment {
        val fragment = CalendarDayFragment()
        fragment.arguments = Bundle().apply {
            // Our object is just an integer :-P
            putInt(ARG_OBJECT, position + 1)
            if (exercises.isNotEmpty()) {
                putSerializable("exercises", exercises)
            }

        }
        return fragment
    }

    override fun getPageTitle(position: Int): CharSequence {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val date =  LocalDate.now().plusDays(position.toLong())
            var formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            var formattedDate = date.format(formatter)
            var localeFormat = DateTimeFormatter.ofPattern("EEEE", Locale.forLanguageTag("pl-PL"))
            val dayOfWeek = localeFormat.format(date)
            val difference = 1
            if (LocalDate.now().atStartOfDay().dayOfMonth == date.dayOfMonth
                && LocalDate.now().atStartOfDay().month == date.month
                && LocalDate.now().atStartOfDay().year == date.year) {
                "Dziś ($formattedDate)"
            }
            else if (date.dayOfMonth == LocalDate.now().atStartOfDay().dayOfMonth + 1
                && LocalDate.now().atStartOfDay().month == date.month
                && LocalDate.now().atStartOfDay().year == date.year) {
                "Jutro ($formattedDate)"
            }
            else if (LocalDate.now().atStartOfDay().dayOfMonth - 1 == date.dayOfMonth
                && LocalDate.now().atStartOfDay().month == date.month
                && LocalDate.now().atStartOfDay().year == date.year) {
                "Wczoraj ($formattedDate)"
            }
            else {
                "$dayOfWeek ($formattedDate)"
            }
        } else {
            "$position"
        }

    }
}

