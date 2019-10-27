package com.monika.WorkoutsMainPage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.monika.HomeScreen.CalendarPager.ExercisesListAdapter
import com.monika.HomeScreen.MainActivity.MainActivity
import com.monika.Model.WorkoutPlan.Workout
import com.monika.R
import kotlinx.android.synthetic.main.fragment_workouts_list.*

class WorkoutsList : Fragment() {

    val presenter = WorkoutsListPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            val workouts = arguments?.get("workouts") as ArrayList<Workout>
            presenter.workoutsList = workouts
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_workouts_list, container, false)
    }

    override fun onStart() {
        super.onStart()
//        if (FirebaseAuth.getInstance().currentUser != null) {
//            if (arguments == null) {
//                (activity as MainActivity).showProgressView()
//                presenter.fetchUserWorkouts { result ->
//                    if (result.isNotEmpty()) {
//                        setRecyclerView()
//                    } else {
//                        //TODO zrob to cos bo nie dziala obviously
//                    }
//                }
//            }
//        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayout()
        if (presenter.workoutsList.isNotEmpty()) {
            setRecyclerView()
        }
    }

    private fun setLayout() {
        (activity as MainActivity).supportActionBar?.show()
    }

    private fun setRecyclerView() {
        var recyclerView = view?.findViewById<RecyclerView>(R.id.workoutListRecyclerView)
        recyclerView?.apply {
            setHasFixedSize(true)
            // use a linear layout manager
            layoutManager = LinearLayoutManager(context)
            adapter = WorkoutsListAdapter(presenter.workoutsList)
        }

        (activity as MainActivity).hideProgressView()
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).supportActionBar?.hide()
    }
}

