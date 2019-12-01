package com.monika.HomeScreen.CalendarPager

import android.os.Bundle
import android.text.format.DateUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import com.monika.Model.CalendarDayTimestamp
import com.monika.Model.WorkoutComponents.Exercise
import java.sql.Time
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList


class HomeFragmentPagerAdapter(fm: FragmentManager, private val startDate: Date) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    val ARG_OBJECT = "object"
//    var exercises = ArrayList<Exercise>()
    lateinit var date: Date
    private val formatter = SimpleDateFormat("dd.MM.yyyy")
    private val localeFormat = SimpleDateFormat("EEEE", Locale.forLanguageTag("en-us"))

    override fun getCount(): Int {
        return 14
    }

    override fun getItem(position: Int): Fragment {
        val fragment = CalendarDayFragment()
        fragment.arguments = Bundle().apply {
            // Our object is just an integer :-P
//            putInt(ARG_OBJECT, position + 1)
//            if (exercises.isNotEmpty()) {
//                putSerializable("workoutElements", exercises)
//            }
            date = getDateFor(position)
            val formatter = SimpleDateFormat("dd.MM.yyyy")
            val formattedDate = formatter.format(date)
            val timestamp = CalendarDayTimestamp()
            timestamp.timestamp = formattedDate
            putSerializable("timestamp", timestamp)
        }
        return fragment
    }

    private fun getDateFor(position: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.time = startDate
        calendar.add(Calendar.DATE, position)
        return calendar.time
    }

    override fun getPageTitle(position: Int): CharSequence {
        val calendar = Calendar.getInstance()
        val now = Date()
        calendar.time = startDate
        calendar.add(Calendar.DATE, position)
        val date = calendar.time
        val formattedDate = formatter.format(date)
        val dayOfWeek = localeFormat.format(date)
        return when {
            (formattedDate == formatter.format(now)) -> "Today ($formattedDate)"
            (date.time - now.time) == 1.toLong() -> "Tomorrow ($formattedDate)"
            (now.time - date.time) == 1.toLong() -> "Yesterday ($formattedDate)"
             //-> "Tomorrow ($formattedDate)"
            else -> "$dayOfWeek $formattedDate"
        }
    }
}
