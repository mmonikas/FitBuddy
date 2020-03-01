package com.monika.HomePage

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.monika.MainActivity.MainActivity
import com.monika.Model.WorkoutComponents.Exercise
import com.monika.R
import com.monika.SignInSignUp.LoginFragmentPresenter
import kotlinx.android.synthetic.main.fragment_home.*
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment(), CompactCalendarView.CompactCalendarViewListener {

    val presenter = LoginFragmentPresenter()
    private lateinit var compactCalendarView : CompactCalendarView
    private val dateFormat = SimpleDateFormat("d MMMM yyyy", /*Locale.getDefault()*/Locale.ENGLISH)
    private var isExpanded = false
    private var choosenDate: Date = Calendar.getInstance().time

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            val exercises = arguments?.get("workoutElements") as ArrayList<Exercise>
            presenter.exercises = exercises
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       // setUpDatabase()
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onStart() {
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser == null) {
            val navBuilder = NavOptions.Builder()
            val navOptions = navBuilder.setEnterAnim(R.anim.slide_out_top).setPopUpTo(R.id.nav_graph, true)
            findNavController().navigate(R.id.loginFragment, null, navOptions.build(), null)
//            findNavController().navigate(R.id.loginFragment)
        } else {
            if (arguments == null) {
                presenter.fetchUserExercises { result ->
                    if (result.isNotEmpty()) {
                        presenter.exercises = result
                        setTabLayout()
//                    val bundle = bundleOf("workouts" to workoutListResult)
//                    Navigation.findNavController(view!!).navigate(R.id.homeFragment, bundle, null)
                    } else {
                        //loginFragment_progress.visibility = View.GONE
                        //TODO zrob to cos bo nie dziala obviously
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayout()
//        if (presenter.exercises.isNotEmpty()) {
//            setTabLayout()
//        }
        compactCalendarView = compactcalendar_view

        // Force English
        compactCalendarView.setLocale(TimeZone.getDefault(), /*Locale.getDefault()*/Locale.ENGLISH)

        compactCalendarView.setShouldDrawDaysHeader(true)
        compactCalendarView.setListener(this)
        compactCalendarView.setUseThreeLetterAbbreviation(true)

        compactCalendarView.visibility = View.GONE
        date_picker_button.visibility = View.GONE
        setCurrentDate(Date())


//        date_picker_button.setOnClickListener {
//            if (isExpanded) {
//                val rotation = 0.toFloat()
//                ViewCompat.animate(date_picker_arrow).rotation(rotation).start()
//            }
//            else {
//                val rotation = 180.toFloat()
//                ViewCompat.animate(date_picker_arrow).rotation(rotation).start()
//            }
//
//            isExpanded = !isExpanded
//
////            app_bar_layout.
//        }

        fullCalendar.setOnClickListener {
           toggleFullCalendar()
        }

    }

    private fun setCurrentDate(date: Date) {
        setSubtitle(dateFormat.format(date))
        if (compactCalendarView != null) {
            compactCalendarView.setCurrentDate(date)
        }
    }

    private fun setSubtitle(subtitle: String) {
        val datePickerTextView = date_picker_text_view

        if (datePickerTextView != null) {
            datePickerTextView.text = subtitle
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setTabLayout()
    }
    private fun setLayout() {
        (activity as MainActivity).supportActionBar?.show()
    }

    private fun setTabLayout() {
        val tabLayout = view?.findViewById<TabLayout>(R.id.home_tab_layout)
        tabLayout?.setupWithViewPager(home_pager)
        tabLayout?.isTabIndicatorFullWidth = true
//        tabLayout?.minimumWidth =
//        calendarDaysCollectionPagerAdapter = HomeFragmentPagerAdapter(childFragmentManager)

        val adapter = HomeFragmentPagerAdapter(childFragmentManager, choosenDate)
        //        //adapter.exercises = presenter.exercises
        home_pager?.adapter = adapter
        home_pager?.offscreenPageLimit = 1
        //(activity as MainActivity).hideProgressView()
    }

    override fun onDayClick(dateClicked: Date?) {
        setSubtitle(dateFormat.format(dateClicked))
        setViewPager(dateClicked)
        toggleFullCalendar()
    }

    override fun onMonthScroll(firstDayOfNewMonth: Date?) {
        setSubtitle(dateFormat.format(firstDayOfNewMonth))
    }

    private fun toggleFullCalendar() {
        if (compactCalendarView.isVisible) {
            compactCalendarView.visibility = View.GONE
            date_picker_button.visibility = View.GONE
            val rotation = 0.toFloat()
            ViewCompat.animate(date_picker_arrow).rotation(rotation).start()
        }
        else {
            compactCalendarView.visibility = View.VISIBLE
            date_picker_button.visibility = View.VISIBLE
            val rotation = 180.toFloat()
            ViewCompat.animate(date_picker_arrow).rotation(rotation).start()
        }
    }

    private fun setViewPager(date: Date?) {
        date?.let {
            choosenDate = it
            home_pager.adapter = HomeFragmentPagerAdapter(childFragmentManager, choosenDate)
            //        //adapter.exercises = presenter.exercises
        }

    }
}

