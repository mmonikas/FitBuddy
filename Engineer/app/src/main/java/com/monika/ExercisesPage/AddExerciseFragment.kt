package com.monika.ExercisesPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.monika.AlertDialogs.CategoryChoiceDialog
import com.monika.Enums.FirebaseRequestResult
import com.monika.MainActivity.MainActivity
import com.monika.Model.WorkoutComponents.Category
import com.monika.Model.WorkoutComponents.Exercise
import com.monika.R
import com.monika.Services.Utils
import kotlinx.android.synthetic.main.fragment_add_exercise.*

class AddExerciseFragment : Fragment(), SelectionListener {

    private val presenter = AddExercisePresenter()
    private var categoryDialog: CategoryChoiceDialog? = null
    private var exercise: Exercise = Exercise()
    private lateinit var exerciseForDetails: Exercise
    private var isEditingMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            exerciseForDetails = arguments?.get("exerciseForDetails") as Exercise
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_exercise, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null && exerciseForDetails.name != null) {
            setDetailsData()
        }
        else {
            showCategoryDialog()
        }
        setSaveButtonListener()
        setCategoryButtonListener()
    }

    private fun showCategoryDialog() {
        //  showing dialog
        categoryDialog = CategoryChoiceDialog(context!!, this)
        categoryDialog?.show()
    }

    override fun onClickCallback(category: Category) {
        exercise.category = category.name
        val category = exercise.category
        category?.let {
            add_exercise_chosenCategory.text = exercise.category?.toUpperCase()
            add_exercise_chosenCategoryImage.setImageResource(Utils.getCategoryImage(category))
        }
        categoryDialog?.dismiss()
    }

    override fun onConfirmCallback() {
        val category = exercise.category
        category?.let {
            add_exercise_chosenCategory.text = exercise.category?.toUpperCase()
            add_exercise_chosenCategoryImage.setImageResource(Utils.getCategoryImage(category))
        }
        categoryDialog?.dismiss()
    }

    private fun setSaveButtonListener() {
        saveButton.setOnClickListener {
            collectInputData()
            if (presenter.isExerciseDataValid(exercise)) {
                if (isEditingMode) {
                    presenter.updateExercise(exercise) {
                        result ->
                        when (result) {
                            FirebaseRequestResult.SUCCESS -> {
                                (activity as MainActivity).showToast(R.string.exerciseEdited)
                                findNavController().popBackStack()
                            }
                            FirebaseRequestResult.FAILURE -> {
                                (activity as MainActivity).showToast(R.string.operationError)
                                findNavController().popBackStack()
                            }
                            else -> {}
                        }

                        //Navigation.findNavController(it).popBackStack(R.id.exercisesListFragment, true)
                    }
                }
                else {
                    presenter.saveExercise(exercise) { result ->
                        when (result) {
                            FirebaseRequestResult.SUCCESS -> {
                                (activity as MainActivity).showToast(R.string.exerciseAdded)
                                findNavController().popBackStack()
                            }
                            FirebaseRequestResult.FAILURE -> {
                                (activity as MainActivity).showToast(R.string.operationError)
                                findNavController().popBackStack()
                            }
                            else -> {}
                        }

                        //Navigation.findNavController(it).popBackStack(R.id.exercisesListFragment, true)
                    }
                }

            }
            else {
                (activity as MainActivity).showToast(R.string.invalid_exercise_data)
            }
        }
    }

    private fun collectInputData() {
        exercise.name = add_exercise_name.text.toString()
        exercise.description = add_exercise_description.text.toString()
        exercise.category = add_exercise_chosenCategory.text.toString().toLowerCase()
        exercise.equipment = add_exercise_equipment.text.toString()
    }

    private fun setDetailsData() {
        titleLabel.text = getString(R.string.editExerciseLabel)
        exercise = exerciseForDetails
        add_exercise_name.setText(exerciseForDetails.name)
        add_exercise_description.setText(exerciseForDetails.description)
        add_exercise_equipment.setText(exerciseForDetails.equipment)
        exerciseForDetails.category?.let {
            add_exercise_chosenCategoryImage.setImageResource(Utils.getCategoryImage(it))
            add_exercise_chosenCategory.text = it.toUpperCase()
        }
        isEditingMode = true
    }

    private fun setCategoryButtonListener() {
        add_exercise_chosenCategory.setOnClickListener {
            showCategoryDialog()
        }
    }

}