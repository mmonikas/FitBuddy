package com.monika.WorkoutsMainPage

import android.content.Context
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.monika.AlertDialogs.ExerciseChoiceDialog
import com.monika.AlertDialogs.WorkoutElementAddingDialog
import com.monika.ExercisesMainPage.*
import com.monika.MainActivity.MainActivity
import com.monika.Model.WorkoutComponents.Exercise
import com.monika.Model.WorkoutComponents.WorkoutElement
import com.monika.R

class WorkoutAdd : Fragment(), AddAnotherListener, ExerciseSelectionListener, WorkoutElementAddListener {

    private val presenter = WorkoutAddPresenter()
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: WorkoutElementItemsAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var chooseExerciseDialog: ExerciseChoiceDialog
    private lateinit var workoutElementCreatingDialog: WorkoutElementAddingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_workout_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
    }

    private fun setRecyclerView() {
        viewManager = LinearLayoutManager(context)
        viewAdapter = WorkoutElementItemsAdapter(this, true, presenter.workoutElements)
        recyclerView = view!!.findViewById<RecyclerView>(R.id.workoutAddRecyclerView).apply {
            layoutManager = viewManager
            adapter = viewAdapter
            (activity as MainActivity).enableBottomNavigation()
        }
        val swipeController = SwipeController(object : SwipeControllerActions() {
            override fun onRightClicked(position: Int) {
                presenter.workoutElements.removeAt(position - 1)
               // presenter.workoutElements.remove(viewAdapter.getItemAt(position))
                refreshAdapter()

            }
        }, context = context!!, isEditPossible = false)

        val itemTouchHelper = ItemTouchHelper(swipeController)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                swipeController.onDraw(c)
            }
        })

        //(activity as MainActivity).hideProgressView()

    }

    override fun onClickCallback() {
        val exercises = presenter.getExercisesList()
        if (exercises.isNotEmpty()) {
            chooseExerciseDialog = ExerciseChoiceDialog(exercises, context!!, this)
            chooseExerciseDialog.show()

        }
        else {
            (activity as MainActivity).showProgressView()
            presenter.getAllExercises {
                result ->
                chooseExerciseDialog = ExerciseChoiceDialog(result, context!!, this)
                chooseExerciseDialog.show()
                (activity as MainActivity).hideProgressView()
            }
        }
    }

    override fun onClickCallback(exercise: Exercise) {
        presenter.exercises.add(exercise)
        workoutElementCreatingDialog = WorkoutElementAddingDialog(exercise, context!!, this)
        workoutElementCreatingDialog.show()
    }

    override fun onConfirmCallback() {
    }

    override fun onWorkoutElementDefined(workoutElement: WorkoutElement) {
        presenter.workoutElements.add(workoutElement)
        refreshAdapter()
        workoutElementCreatingDialog.dismiss()
        chooseExerciseDialog.dismiss()
    }

    private fun refreshAdapter() {
        viewAdapter = WorkoutElementItemsAdapter(this, true, presenter.workoutElements)
        recyclerView.adapter = viewAdapter
    }


}// Required empty public constructor
