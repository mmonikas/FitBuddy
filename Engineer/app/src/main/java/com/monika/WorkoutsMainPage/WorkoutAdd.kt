package com.monika.WorkoutsMainPage

import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.monika.AlertDialogs.ExerciseChoiceDialog
import com.monika.AlertDialogs.WorkoutElementAddingDialog
import com.monika.Enums.FirebaseRequestResult
import com.monika.ExercisesMainPage.*
import com.monika.MainActivity.MainActivity
import com.monika.Model.WorkoutComponents.Exercise
import com.monika.Model.WorkoutComponents.WorkoutElement
import com.monika.Model.WorkoutPlan.Workout
import com.monika.R
import com.monika.Services.DataValidator
import kotlinx.android.synthetic.main.fragment_workout_add.*
import java.util.*

class WorkoutAdd : Fragment(), AddAnotherListener, ExerciseSelectionListener, WorkoutElementAddListener {

    private val presenter = WorkoutAddPresenter()
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: WorkoutElementItemsAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var chooseExerciseDialog: ExerciseChoiceDialog
    private lateinit var workoutElementCreatingDialog: WorkoutElementAddingDialog
    private var isEditingMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            presenter.workoutToEdit = arguments?.get("workoutToEdit") as Workout
        }
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
        if (arguments != null && presenter.workoutToEdit.name != null) {
            setDetailsData()
        }
        setRecyclerView()
        setSaveButtonListener()
    }

    private fun setDetailsData() {
        presenter.workoutToEdit.exercises?.let {
            items ->
            presenter.workoutElements = items
        }
        presenter.workoutToEdit.name?.let { name ->
            workoutNameEditText.setText(name)
        }
        isEditingMode = true
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

    private fun setSaveButtonListener() {
        saveButton.setOnClickListener {
            val workoutToSave = getCollectedData()
            val isDataValid = DataValidator.instance.isDataForNewWorkoutValid(workoutToSave)
            if (isDataValid) {
                if (isEditingMode) {
                    presenter.updateEditedWorkout(workoutToSave) {
                        result ->
                        when (result) {
                            FirebaseRequestResult.SUCCESS -> {
                                (activity as MainActivity).showToast(R.string.workoutUpdated)
                            }
                            FirebaseRequestResult.FAILURE -> {
                                (activity as MainActivity).showToast(R.string.operationError)
                            }
                            FirebaseRequestResult.COMPLETED -> {

                            }
                        }
                        view?.let { view ->
                            Navigation.findNavController(view).popBackStack()
                        }
                    }
                }
                else {
                    presenter.saveNewWorkout(workoutToSave) { result ->
                        when (result) {
                            FirebaseRequestResult.SUCCESS -> {
                                (activity as MainActivity).showToast(R.string.workoutAdded)
                            }
                            FirebaseRequestResult.FAILURE -> {
                                (activity as MainActivity).showToast(R.string.operationError)
                            }
                            FirebaseRequestResult.COMPLETED -> {
                            }
                        }
                        view?.let { view ->
                            Navigation.findNavController(view).popBackStack()
                        }
                    }
                }
            }
            else {
                showInvalidDataInfo()
            }
        }
    }

    private fun getCollectedData() : Workout {
        val workout = Workout()
        workout.name = workoutNameEditText.text.toString()
        workout.exercises = presenter.workoutElements
        if (isEditingMode) {
            workout.docReference = presenter.workoutToEdit.docReference
            workout.userId = presenter.workoutToEdit.userId
            workout.initDate = presenter.workoutToEdit.initDate
        }
        else {
            workout.initDate = Date()
        }
        return workout
    }

    private fun showInvalidDataInfo() {
        (activity as MainActivity).showToast(R.string.workoutAddInvalidData)
    }

}// Required empty public constructor
