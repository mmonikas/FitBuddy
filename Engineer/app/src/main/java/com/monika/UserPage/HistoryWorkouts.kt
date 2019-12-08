package com.monika.UserPage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.monika.ExercisesPage.PlannedWorkoutsLogCompleted
import com.monika.MainActivity.MainActivity
import com.monika.Model.WorkoutPlan.PlannedWorkout
import com.monika.R
import com.monika.WorkoutsPage.PlannedWorkoutsAdapter
import java.util.*


class HistoryWorkouts : Fragment(), PlannedWorkoutsLogCompleted {

    private val presenter = HistoryWorkoutsPresenter()
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: PlannedWorkoutsAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        viewAdapter = PlannedWorkoutsAdapter(true, context!!, presenter.historyWorkouts, this, null)
        (activity as MainActivity).disableBottomNavigation()
        return inflater.inflate(R.layout.fragment_history_workouts, container, false)
    }


    override fun onStart() {
        super.onStart()
        getContent()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
    }

    private fun getContent() {
        (activity as MainActivity).showProgressView()
        (activity as MainActivity).disableBottomNavigation()
        presenter.fetchAllPlannedWorkouts { result ->
            if (result.isNotEmpty()) {
                presenter.historyWorkouts = result
                if (context != null) {
                    viewAdapter = PlannedWorkoutsAdapter(true, context!!, presenter.historyWorkouts, this, null)
                    recyclerView.adapter = viewAdapter
                    (activity as MainActivity).enableBottomNavigation()
                }
//                viewAdapter = WorkoutsListAdapter(context!!, presenter.workouts, this)
            }
//            viewAdapter = WorkoutsListAdapter(context!!, presenter.workouts, this)
//            setRecyclerView()
            activity?.let {
                (it as MainActivity).hideProgressView()
            }
        }
    }

    private fun setRecyclerView() {
        viewManager = LinearLayoutManager(context)
        viewAdapter = PlannedWorkoutsAdapter(true, context!!, presenter.historyWorkouts, this, null)
        recyclerView = view!!.findViewById<RecyclerView>(R.id.recyclerView).apply {
            layoutManager = viewManager
            adapter = viewAdapter
            (activity as MainActivity).enableBottomNavigation()
        }
    }


    override fun onCompletedChosen(plannedWorkout: PlannedWorkout, position: Int) {
    }

    override fun onAddWorkoutClicked(date: Date) {
    }

}
