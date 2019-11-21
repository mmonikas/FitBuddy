package com.monika.WorkoutsMainPage

import com.monika.Enums.UserDataType
import com.monika.Model.WorkoutComponents.Exercise
import com.monika.Model.WorkoutComponents.WorkoutElement
import com.monika.Model.WorkoutPlan.Workout
import com.monika.Services.DatabaseService

class WorkoutAddPresenter {

    private var exercisesList = ArrayList<Exercise>()
    var workoutElements = ArrayList<WorkoutElement>()
    var exercises = ArrayList<Exercise>()
    lateinit var workout: Workout


    fun getAllExercises(completion: (result: ArrayList<Exercise>) -> Unit) {
        DatabaseService.instance.fetchUserData(UserDataType.EXERCISE) {
                result ->
            if(result.isNotEmpty()) {
                exercisesList = result as ArrayList<Exercise>
                completion(exercisesList)
            }
        }
    }

    fun getExercisesList() : ArrayList<Exercise> {
        return exercisesList
    }

}