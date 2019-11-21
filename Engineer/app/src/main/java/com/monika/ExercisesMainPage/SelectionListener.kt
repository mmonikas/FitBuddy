package com.monika.ExercisesMainPage

import com.monika.Model.WorkoutComponents.Category
import com.monika.Model.WorkoutComponents.Exercise
import com.monika.Model.WorkoutComponents.WorkoutElement

interface SelectionListener {
    fun onClickCallback(category: Category)
    fun onConfirmCallback()
}

interface ExerciseSelectionListener {
    fun onClickCallback(exercise: Exercise)
    fun onConfirmCallback()
}

interface AddAnotherListener {
    fun onClickCallback()
}

interface WorkoutElementAddListener {
    fun onWorkoutElementDefined(workoutElement: WorkoutElement)
}