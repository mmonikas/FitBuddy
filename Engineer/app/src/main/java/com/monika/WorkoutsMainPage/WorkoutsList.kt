package com.monika.WorkoutsMainPage

import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.monika.Enums.FirebaseRequestResult
import com.monika.ExercisesMainPage.SwipeController
import com.monika.ExercisesMainPage.SwipeControllerActions
import com.monika.HomeScreen.MainActivity.MainActivity
import com.monika.Model.WorkoutPlan.Workout
import com.monika.R
import kotlinx.android.synthetic.main.fragment_workouts_list.*
import java.util.*


interface  WorkoutsPlannerListener {
    fun datesChoosenFor(workout: Workout, dates: ArrayList<String>)
}

class WorkoutsList : Fragment(), WorkoutsPlannerListener {

    val presenter = WorkoutsListPresenter()

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: WorkoutsListAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val options = presenter.getOptionsForWorkoutsListListener()
//        viewAdapter = WorkoutsListAdapter(context = context!!, options = options, listener = this)
//        if (arguments != null) {
//            val workouts = arguments?.get("workouts") as ArrayList<Workout>
//            presenter.workoutsList = workouts
//
//            viewAdapter = WorkoutsListAdapter(context!!, workouts, this)
//        }
        getContent()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
        ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_workouts_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFAB()
    }
//
//    override fun onStart() {
//        super.onStart()
//        viewAdapter.startListening()
//    }
//
//    override fun onStop() {
//        super.onStop()
//        viewAdapter.stopListening()
//    }

    private fun getContent() {
        (activity as MainActivity).showProgressView()
        presenter.fetchUserWorkouts { result ->
            if (result.isNotEmpty()) {
                presenter.workouts = result
                viewAdapter = WorkoutsListAdapter(context!!, presenter.workouts, this)
            }
            viewAdapter = WorkoutsListAdapter(context!!, presenter.workouts, this)
            setRecyclerView()
        }


    }

    private fun setRecyclerView() {
        viewManager = LinearLayoutManager(context)
        recyclerView = view!!.findViewById<RecyclerView>(R.id.workoutListRecyclerView).apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
        val swipeController = SwipeController(object : SwipeControllerActions() {
            override fun onRightClicked(position: Int) {
               // presenter.removeItemAt(viewAdapter.getItem(position))
            }

            override fun onLeftClicked(position: Int) {
                val bundle = Bundle()
                bundle.putSerializable("workoutForDetails", presenter.workouts[position])
                findNavController().navigate(R.id.workoutAdd, bundle, null)
                //edit
            }
        }, context = context!!)

        val itemTouchHelper = ItemTouchHelper(swipeController)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                swipeController.onDraw(c)
            }
        })

        (activity as MainActivity).hideProgressView()
    }

    private fun setFAB() {
//        fab_addExercise.setOnClickListener {
//            Navigation.findNavController(it).navigate(R.id.add_exercise)
//        }
    }

    private fun showErrorSnackbar() {
        val snackbar = Snackbar
            .make(view!!, R.string.workoutsPlanningError, Snackbar.LENGTH_LONG)
            .setAction(R.string.ok) {

            }
        snackbar.view.setBackgroundColor(ContextCompat.getColor(context!!, R.color.accentOrange))
        snackbar.show()
    }

    private fun showSuccessSnackbar() {
        val snackbar = Snackbar
            .make(view!!, R.string.workoutsPlanned, Snackbar.LENGTH_LONG)
            .setAction(R.string.ok) {

            }
        snackbar.show()
    }

    override fun datesChoosenFor(workout: Workout, dates: ArrayList<String>) {
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

}

