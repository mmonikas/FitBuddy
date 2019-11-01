package com.monika.ExercisesMainPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.monika.AlertDialogs.CategoryChoiceDialog
import com.monika.Model.WorkoutComponents.Category
import com.monika.Model.WorkoutComponents.Exercise
import com.monika.R
import com.monika.Services.Utils
import kotlinx.android.synthetic.main.dialog_category_choice.*
import kotlinx.android.synthetic.main.dialog_category_choice.view.*
import kotlinx.android.synthetic.main.fragment_add_exercise.*

class AddExerciseFragment : Fragment(), SelectionListener {

    private val presenter = AddExercisePresenter()
    private var categoryDialog: CategoryChoiceDialog? = null
    private var exercise: Exercise = Exercise()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        if (arguments != null) {
//            val workouts = arguments?.get("workouts") as ArrayList<Workout>
//            presenter.workoutsList = workouts
//        }
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
        showCategoryDialog()
    }

    private fun showCategoryDialog() {
        //  showing dialog
        categoryDialog = CategoryChoiceDialog(context!!, this)
        categoryDialog?.show()
    }

    override fun onClickCallback(category: Category) {
        exercise.category = category.name
    }

    override fun onConfirmCallback() {
        val category = exercise.category
        category?.let {
            add_exercise_chosenCategory.text = exercise.category?.toUpperCase()
            add_exercise_chosenCategoryImage.setImageResource(Utils.getCategoryImage(category))
        }
    }

}