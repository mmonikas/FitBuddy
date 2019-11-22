package com.monika.ExercisesMainPage

import com.monika.Enums.FirebaseRequestResult
import com.monika.Enums.UserDataType
import com.monika.Model.WorkoutComponents.Exercise
import com.monika.Services.DatabaseService

class AddExercisePresenter {

    fun saveExercise(exercise: Exercise, completion: (result: FirebaseRequestResult) -> Unit) {
        DatabaseService.instance.saveNewDocument(exercise, UserDataType.EXERCISE) {
                result, _ ->
            completion(result)
        }
    }

    fun isExerciseDataValid(exercise: Exercise): Boolean {
        return !exercise.name.isNullOrEmpty() && !exercise.category.isNullOrEmpty()
                && !exercise.description.isNullOrEmpty() && !exercise.equipment.isNullOrEmpty()
    }

    fun updateExercise(exercise: Exercise, completion: (result: FirebaseRequestResult) -> Unit) {
        DatabaseService.instance.updateDocument(exercise, UserDataType.EXERCISE) {
            result -> completion(result)
        }
    }
}
