package com.monika.WorkoutsMainPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.monika.Enums.FirebaseRequestResult
import com.monika.HomeScreen.MainActivity.MainActivity
import com.monika.Model.WorkoutPlan.Workout
import com.monika.R
import kotlinx.android.synthetic.main.fragment_workouts_list.*
import java.util.*


interface  WorkoutsPlannerListener {
    fun datesChoosenFor(workout: Workout, dates: ArrayList<Date>)
}

class WorkoutsList : Fragment(), WorkoutsPlannerListener {

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
        setFABListener()
        if (presenter.workoutsList.isNotEmpty()) {
            setRecyclerView()
        }
    }

    private fun setRecyclerView() {
       // var recyclerView = view?.findViewById<RecyclerView>(R.id.workoutListRecyclerView)
        val recyclerView = workoutListRecyclerView
        recyclerView?.apply {
            setHasFixedSize(true)
            // use a linear layout manager
            layoutManager = LinearLayoutManager(context)
            adapter = WorkoutsListAdapter(presenter.workoutsList, context, this@WorkoutsList)
        }

        (activity as MainActivity).hideProgressView()
    }

    private fun setFABListener() {
//        fab_addExercise.setOnClickListener {
//            Navigation.findNavController(it).navigate(R.id.add_exercise)
//        }
    }

    override fun datesChoosenFor(workout: Workout, dates: ArrayList<Date>) {
        (activity as MainActivity).showProgressView()
        presenter.planWorkoutForDates(workout = workout, dates = dates) {
            result ->
            (activity as MainActivity).hideProgressView()
            when (result) {
                FirebaseRequestResult.SUCCESS -> showSuccessSnackbar()
                FirebaseRequestResult.FAILURE -> showErrorSnackbar()
            }
        }
    }

    private fun showErrorSnackbar() {

    }

    private fun showSuccessSnackbar() {
        val snackbar = Snackbar
            .make(view!!, R.string.workoutsPlanned, Snackbar.LENGTH_LONG)
            .setAction(R.string.ok) {

            }
        snackbar.show()
    }

}

