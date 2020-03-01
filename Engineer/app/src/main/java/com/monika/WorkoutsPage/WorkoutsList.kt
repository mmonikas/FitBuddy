package com.monika.WorkoutsPage

import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.monika.AlertDialogs.ConfirmationDialog
import com.monika.Enums.FirebaseRequestResult
import com.monika.ExercisesPage.ConfirmationListener
import com.monika.ExercisesPage.SwipeController
import com.monika.ExercisesPage.SwipeControllerActions
import com.monika.ExercisesPage.WorkoutsPlannerListener
import com.monika.MainActivity.MainActivity
import com.monika.Model.WorkoutPlan.PlannedWorkout
import com.monika.Model.WorkoutPlan.Workout
import com.monika.R
import com.monika.Services.Utils
import kotlinx.android.synthetic.main.fragment_workouts_list.*
import java.util.*



class WorkoutsList : Fragment(), WorkoutsPlannerListener, ConfirmationListener {

    val presenter = WorkoutsListPresenter()
    private lateinit var confirmationDialog: ConfirmationDialog
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: WorkoutsListAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
        ): View? {
        viewAdapter = WorkoutsListAdapter(context!!, presenter.workouts, this)
        (activity as MainActivity).disableBottomNavigation()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_workouts_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFAB()
        setRecyclerView()
    }

    private fun getContent() {
        (activity as MainActivity).showProgressView()
        (activity as MainActivity).disableBottomNavigation()
        presenter.fetchUserWorkouts { result ->
            if (result.isNotEmpty()) {
                presenter.workouts = result
                if (context != null) {
                    viewAdapter = WorkoutsListAdapter(context!!, presenter.workouts, this)
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

    override fun onStart() {
        super.onStart()
        context?.let { it ->
            view?.let { view ->
                Utils.hideSoftKeyBoard(it, view)
            }
        }
        getContent()
    }

    private fun setRecyclerView() {
        viewManager = LinearLayoutManager(context)
        viewAdapter = WorkoutsListAdapter(context!!, presenter.workouts, this)
        recyclerView = view!!.findViewById<RecyclerView>(R.id.workoutListRecyclerView).apply {
            layoutManager = viewManager
            adapter = viewAdapter
            (activity as MainActivity).enableBottomNavigation()
        }
        val swipeController = SwipeController(object : SwipeControllerActions() {
            override fun onRightClicked(position: Int) {
                if (presenter.workouts[position].userId == null) {
                    showCantEditNorDeleteInfo()
                }
                else {
                    presenter.checkIfWorkoutCanBeDeleted(position) {
                        existsSomewhere ->
                        when (existsSomewhere) {
                            null -> {
                                showErrorInfo()
                            }
                            true -> {
                                showCantDeleteInfo()
                            }
                            else -> {
                                showDeleteConfirmationDialog(position)
                            }
                        }
                    }
                }
            }

            override fun onLeftClicked(position: Int) {
                if (presenter.workouts[position].userId == null) {
                    showCantEditNorDeleteInfo()
                }
                else {
                    val bundle = Bundle()
                    bundle.putSerializable("workoutToEdit", presenter.workouts[position])
                    findNavController().navigate(R.id.action_workoutsList_to_workoutAdd, bundle, null)
                }
            }
        }, context = context!!, isEditPossible = true)

        val itemTouchHelper = ItemTouchHelper(swipeController)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                swipeController.onDraw(c)
            }
        })

        //(activity as MainActivity).hideProgressView()
    }

    private fun setFAB() {
        fab_addWorkout.setOnClickListener {
            findNavController().navigate(R.id.workoutAdd)
        }
    }

    private fun showErrorSnackbar() {
        (activity as MainActivity).showToast(R.string.errorOccured)
    }

    private fun showSuccessSnackbar() {
        (activity as MainActivity).showToast(R.string.workoutsPlanned)
    }

    private fun showCantEditNorDeleteInfo() {
        (activity as MainActivity).showToast(R.string.cantEditNorDelete)
    }

    private fun showCantDeleteInfo() {
        val title = resources.getString(R.string.deleteWorkoutLabel)
        val contentText = resources.getString(R.string.deleteWorkoutContentCant)
        context?.let {
            confirmationDialog = ConfirmationDialog(it, title, contentText, this, null, null, false)
            confirmationDialog.show()
        }
    }

    private fun showDeleteConfirmationDialog(onPosition: Int) {
        val title = resources.getString(R.string.deleteWorkoutLabel)
        val contentText = resources.getString(R.string.deleteWorkoutContent)
        context?.let {
            confirmationDialog = ConfirmationDialog(it, title, contentText, this, onPosition, null, true)
            confirmationDialog.show()
        }
    }

    private fun showErrorInfo() {
        (activity as MainActivity).showToast(R.string.errorOccured)
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

    override fun onCancelCallback() {
        confirmationDialog.dismiss()
    }

    override fun onConfirmCallback(position: Int?, plannedWorkout: PlannedWorkout?) {
        if (position != null) {
            presenter.removeWorkoutAt(position) { result ->
                if (result == FirebaseRequestResult.SUCCESS) {
                    viewAdapter.notifyDataSetChanged()
                    (activity as MainActivity).showToast(R.string.removeWorkoutSuccess)
                } else if (result == FirebaseRequestResult.FAILURE) {
                    (activity as MainActivity).showToast(R.string.operationError)
                }
            }
        }
        confirmationDialog.dismiss()
    }

}

