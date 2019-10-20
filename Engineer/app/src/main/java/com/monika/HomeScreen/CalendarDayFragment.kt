package com.monika.HomeScreen

import android.os.Bundle
import android.os.WorkSource
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.monika.Model.WorkoutPlan.Workout
import com.monika.R
import kotlinx.android.synthetic.main.fragment_collection_calendar_day.*
import kotlinx.android.synthetic.main.nav_header_main.*


class CalendarDayFragment : Fragment() {

    val ARG_OBJECT = "object"
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var workoutsForDay = ArrayList<Workout>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                               savedInstanceState: Bundle? ): View? {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_collection_calendar_day, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey("exerciseName") }?.apply {
            calendar_dayName.text = getString("exerciseName")
//            workoutsForDay = get("workoutsForDay") as ArrayList<Workout>
            val workout = Workout(userID = "jej", name = getString("exerciseName"))
            val works = ArrayList<Workout>()
            works.add(workout)
            workoutsForDay = works
            viewManager = LinearLayoutManager(context)
            viewAdapter = CalendarDayWorkoutsAdapter(workoutsForDay)

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
}