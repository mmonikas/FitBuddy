package com.monika.ExercisesMainPage

import com.monika.Model.WorkoutComponents.Category

interface SelectionListener {
    fun onClickCallback(category: Category)
    fun onConfirmCallback()
}
