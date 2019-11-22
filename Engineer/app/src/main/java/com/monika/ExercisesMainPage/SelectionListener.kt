package com.monika.ExercisesMainPage

import com.monika.Model.WorkoutComponents.Category
import com.monika.Model.WorkoutComponents.Exercise
import com.monika.Model.WorkoutComponents.MyDocument
import com.monika.Model.WorkoutComponents.WorkoutElement
import com.monika.Model.WorkoutPlan.Workout
import java.util.ArrayList

interface SelectionListener {
    fun onClickCallback(category: Category)
    fun onConfirmCallback()
}

interface ExerciseSelectionListener {
    fun onClickCallback(exercise: Exercise)
}


interface  WorkoutsPlannerListener {
    fun datesChoosenFor(workout: Workout, dates: ArrayList<String>)
}

interface AddAnotherListener {
    fun onClickCallback()
}

interface WorkoutElementAddListener {
    fun onWorkoutElementDefined(workoutElement: WorkoutElement)
}

interface ConfirmationListener {
    fun onConfirmCallback(position: Int?)
    fun onCancelCallback()
}