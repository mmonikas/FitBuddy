package com.monika.AlertDialogs

import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.monika.ExercisesMainPage.WorkoutElementAddListener
import com.monika.Model.WorkoutComponents.Exercise
import com.monika.Model.WorkoutComponents.WorkoutElement
import com.monika.R

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

        val okButton = layout.findViewById<Button>(R.id.workoutElement_confirmButton)
        val cancelButton = layout.findViewById<Button>(R.id.cancelWorkoutElement)

        val reps = layout.findViewById<EditText>(R.id.repsNumber)
        val sets = layout.findViewById<EditText>(R.id.setsNumber)

        okButton.setOnClickListener {
            val reps = reps.text.toString()
            val sets = sets.text.toString()
            if (reps.isNullOrBlank() || sets.isNullOrBlank()) {
                Toast.makeText(context, R.string.fillAlltheData, Toast.LENGTH_LONG).show()
            }
            else {
                workoutElement = WorkoutElement()
                workoutElement?.exercise = exercise
                workoutElement?.numOfSets = sets.toInt()
                workoutElement?.numOfReps = reps.toInt()
                workoutElement?.let {
                    listener.onWorkoutElementDefined(it)
                }

            }
        }

        cancelButton.setOnClickListener {
            workoutElement = null
            dismiss()
        }

    }


}