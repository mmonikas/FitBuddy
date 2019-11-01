package com.monika.HomeScreen.CalendarPager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.monika.Model.WorkoutComponents.Exercise
import com.monika.R


class CalendarDayFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private val presenter = CalendarDayFragmentPresenter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                               savedInstanceState: Bundle? ): View? {
        // Inflate the layout for this fragment
//        if (arguments != null) {
//            //   workouts = arguments?.get("workouts") as ArrayList<Workout>
//            val workouts = arguments?.get("workouts") as ArrayList<Workout>
//            presenter.workouts = workouts
//        }
        return inflater.inflate(R.layout.fragment_collection_calendar_day, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val options = presenter.getOptionsForDataFetching()
//        arguments?.takeIf { it.containsKey("workoutElements") }?.apply {
//            val exercises = get("workoutElements") as ArrayList<Exercise>
//            exercises.let {
                viewManager = LinearLayoutManager(context)
                viewAdapter = ExercisesListAdapter(options)
                recyclerView = view.findViewById<RecyclerView>(R.id.calendarDay_recyclerView).apply {
                    // use this setting to improve performance if you know that changes
                    // in content do not change the layout size of the RecyclerView
                    setHasFixedSize(true)

                    // use a linear layout manager
                    layoutManager = viewManager

                    // specify an viewAdapter (see also next example)
                    adapter = viewAdapter
                }
            }
}