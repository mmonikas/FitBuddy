package com.monika.ExercisesMainPage

import com.monika.Model.WorkoutComponents.Category
import com.monika.Model.WorkoutComponents.Exercise

interface SelectionListener {
    fun onClickCallback(category: Category)
    fun onConfirmCallback()
}

interface ExerciseSelectionListener {
    fun onClickCallback(exercise: Exercise)
    fun onConfirmCallback()
}