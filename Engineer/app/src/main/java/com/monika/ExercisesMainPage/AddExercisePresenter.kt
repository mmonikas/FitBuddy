package com.monika.ExercisesMainPage

import com.monika.Enums.UserDataType
import com.monika.Model.WorkoutComponents.Exercise
import com.monika.Services.DatabaseService

class AddExercisePresenter {

    fun saveExercise(exercise: Exercise) {
        DatabaseService.instance.saveNewDocument(exercise, UserDataType.EXERCISE)
    }

    fun isExerciseDataValid(exercise: Exercise): Boolean {
        return !exercise.name.isNullOrEmpty() && !exercise.category.isNullOrEmpty()
                && !exercise.description.isNullOrEmpty() && !exercise.equipment.isNullOrEmpty()
    }

    fun updateExercise(exercise: Exercise) {
        DatabaseService.instance.updateDocument(exercise, UserDataType.EXERCISE)
    }
}
