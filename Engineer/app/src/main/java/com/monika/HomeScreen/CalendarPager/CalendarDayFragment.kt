package com.monika.HomeScreen.CalendarPager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.monika.Model.CalendarDayTimestamp
import com.monika.Model.WorkoutPlan.Workout
import com.monika.R
import com.monika.WorkoutsMainPage.PlannedWorkoutsAdapter
import com.monika.WorkoutsMainPage.WorkoutsPlannerListener
import kotlinx.android.synthetic.main.fragment_collection_calendar_day.*
import java.text.SimpleDateFormat
import java.util.*


class CalendarDayFragment : Fragment(), WorkoutsPlannerListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private val presenter = CalendarDayFragmentPresenter()
    private lateinit var timestamp: CalendarDayTimestamp

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                               savedInstanceState: Bundle? ): View? {
        // Inflate the layout for this fragment
        if (arguments != null) {
            //   workouts = arguments?.get("workouts") as ArrayList<Workout>
//            val workouts = arguments?.get("workouts") as ArrayList<PlannedWorkout>
//            presenter.workouts = workouts
            timestamp = arguments?.get("timestamp") as CalendarDayTimestamp
        }
        return inflater.inflate(R.layout.fragment_collection_calendar_day, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        calendarDay_progressView.visibility = View.VISIBLE
        timestamp.timestamp?.let { timestamp ->
            presenter.fetchPlannedWorkouts(timestamp) {
                plannedWorkouts ->
                viewManager = LinearLayoutManager(context)
                val formatter = SimpleDateFormat("dd.MM.yyyy")
                val formattedDate = formatter.parse(timestamp)
                viewAdapter = PlannedWorkoutsAdapter(view.context, plannedWorkouts, this, formattedDate)
                recyclerView = view.findViewById<RecyclerView>(R.id.calendarDay_recyclerView).apply {
                    // use this setting to improve performance if you know that changes
                    // in content do not change the layout size of the RecyclerView
                    setHasFixedSize(true)
                    // use a linear layout manager
                    layoutManager = viewManager

                    // specify an viewAdapter (see also next example)
                    adapter = viewAdapter
                }
                view.findViewById<LinearLayout>(R.id.calendarDay_progressView).visibility = View.GONE
            }
        }
//        arguments?.takeIf { it.containsKey("workoutElements") }?.apply {
//            val exercises = get("workoutElements") as ArrayList<Exercise>
//            exercises.let {
    }

    override fun datesChoosenFor(workout: Workout, dates: ArrayList<String>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}