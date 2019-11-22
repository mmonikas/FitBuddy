package com.monika.WorkoutsMainPage

import com.monika.Enums.FirebaseRequestResult
import com.monika.Enums.UserDataType
import com.monika.Model.WorkoutComponents.Exercise
import com.monika.Model.WorkoutComponents.WorkoutElement
import com.monika.Model.WorkoutPlan.FirebaseWorkout
import com.monika.Model.WorkoutPlan.FirebaseWorkoutElement
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

    fun saveNewWorkout(workout: Workout, completion: (result: FirebaseRequestResult) -> Unit) {
        val firebaseWorkoutToSave = FirebaseWorkout()
        val workoutToSaveExercises = ArrayList<String>()
        val firebaseWorkoutElement = FirebaseWorkoutElement()
        workout.exercises?.forEach { workoutElementToSave ->
            firebaseWorkoutElement.exercise = workoutElementToSave.exercise?.docReference
            firebaseWorkoutElement.isTimeIntervalMode = workoutElementToSave.isTimeIntervalMode
            firebaseWorkoutElement.numOfReps = workoutElementToSave.numOfReps
            firebaseWorkoutElement.numOfSets = workoutElementToSave.numOfSets
            firebaseWorkoutElement.timeInterval = workoutElementToSave.timeInterval
            DatabaseService.instance.saveNewDocument(firebaseWorkoutElement,
                UserDataType.WORKOUT_ELEMENT) { result, firebaseWorkoutElementDocReference ->
                if (result == FirebaseRequestResult.SUCCESS) {
                    firebaseWorkoutElementDocReference?.let {
                        workoutToSaveExercises.add(it)
                    }
                    if (workoutToSaveExercises.size == workout.exercises?.size) {
                        firebaseWorkoutToSave.name = workout.name
                        firebaseWorkoutToSave.initDate = workout.initDate
                        firebaseWorkoutToSave.workoutElements = workoutToSaveExercises
                        DatabaseService.instance.saveNewDocument(firebaseWorkoutToSave, UserDataType.WORKOUT) {
                            result, _ ->
                            completion(result)
                        }
                    }
                }
                else {
                    completion(FirebaseRequestResult.FAILURE)
                }
            }


        }
    }
}