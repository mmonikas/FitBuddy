package com.monika.AlertDialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.core.view.isVisible
import com.google.android.material.textfield.TextInputLayout
import com.monika.ExercisesPage.WorkoutElementAddListener
import com.monika.Model.WorkoutComponents.Exercise
import com.monika.Model.WorkoutComponents.WorkoutElement
import com.monika.R
import com.monika.Services.Utils

class WorkoutElementAddingDialog(private val exercise: Exercise, context: Context, listener: WorkoutElementAddListener) : Dialog(context) {

    private var listener = listener
    private var workoutElement: WorkoutElement? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val layout = LayoutInflater.from(context).inflate(R.layout.dialog_workout_element, null)
        window?.setLayout(500, WindowManager.LayoutParams.WRAP_CONTENT)
        setContentView(layout)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val titleLabel = layout.findViewById<TextView>(R.id.addexerciseToWorkoutLabel)
        titleLabel?.text = context.resources.getString(R.string.addExerciseToWorkout)
        val exerciseName = layout.findViewById<TextView>(R.id.workoutExerciseItemAddName)
        exerciseName.text = exercise.name
        val image = layout.findViewById<ImageView>(R.id.workoutAddingElementCategoryImage)
        exercise.category?.let {
            image.setImageResource(Utils.getCategoryImage(it))
        }
        val okButton = layout.findViewById<Button>(R.id.workoutElement_confirmButton)
        val cancelButton = layout.findViewById<Button>(R.id.cancelWorkoutElement)

        val reps = layout.findViewById<EditText>(R.id.repsNumber)
        val repsLayout = layout.findViewById<TextInputLayout>(R.id.repsLayout)
        val sets = layout.findViewById<EditText>(R.id.setsNumber)
        val time = layout.findViewById<EditText>(R.id.timeInterval)
        val timeLayout = layout.findViewById<TextInputLayout>(R.id.timeIntervalLayout)
        val isTimeMode = layout.findViewById<CheckBox>(R.id.checkBox)
        isTimeMode.isChecked = false
        timeLayout.isVisible = false
        repsLayout.isVisible = true

        okButton.setOnClickListener {
            val time = time.text.toString()
            val reps = reps.text.toString()
            val sets = sets.text.toString()
            val isTime = isTimeMode.isChecked
            if ((!isTime && (reps.isNullOrBlank() || sets.isNullOrBlank())) ||
                (isTime && (time.isNullOrBlank() || sets.isNullOrBlank()))) {
                Toast.makeText(context, R.string.invalid_exercise_data, Toast.LENGTH_LONG).show()
            }
            else {
                workoutElement = WorkoutElement()
                workoutElement?.exercise = exercise
                workoutElement?.numOfSets = sets.toInt()
                workoutElement?.isTimeIntervalMode = isTimeMode.isChecked
                if (isTimeMode.isChecked) {
                    workoutElement?.timeInterval = time.toInt()
                } else {
                    workoutElement?.numOfReps = reps.toInt()
                }
                workoutElement?.let {
                    listener.onWorkoutElementDefined(it)
                }

            }
        }

        cancelButton.setOnClickListener {
            workoutElement = null
            dismiss()
        }

        isTimeMode.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                timeLayout.isVisible = true
                repsLayout.isVisible = false
            }
            else {
                timeLayout.isVisible = false
                repsLayout.isVisible = true
            }
        }

    }


}