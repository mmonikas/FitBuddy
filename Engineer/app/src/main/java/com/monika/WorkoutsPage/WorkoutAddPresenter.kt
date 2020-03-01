package com.monika.WorkoutsPage

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
    lateinit var workoutToEdit: Workout


    fun getAllExercises(completion: (result: ArrayList<Exercise>) -> Unit) {
        DatabaseService.fetchUserData(UserDataType.EXERCISE) {
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
        val workoutToSaveExercises = ArrayList<FirebaseWorkoutElement>()
        var firebaseWorkoutElement: FirebaseWorkoutElement
        workout.exercises?.forEach { workoutElementToSave ->
            firebaseWorkoutElement = FirebaseWorkoutElement()
            firebaseWorkoutElement.exercise = workoutElementToSave.exercise?.docReference
            firebaseWorkoutElement.isTimeIntervalMode = workoutElementToSave.isTimeIntervalMode
            firebaseWorkoutElement.numOfReps = workoutElementToSave.numOfReps
            firebaseWorkoutElement.numOfSets = workoutElementToSave.numOfSets
            firebaseWorkoutElement.timeInterval = workoutElementToSave.timeInterval
            workoutToSaveExercises.add(firebaseWorkoutElement)
            if (workoutToSaveExercises.size == workout.exercises?.size) {
                firebaseWorkoutToSave.name = workout.name
                firebaseWorkoutToSave.initDate = workout.initDate
                firebaseWorkoutToSave.workoutElements = workoutToSaveExercises
                DatabaseService.saveNewDocument(firebaseWorkoutToSave, UserDataType.WORKOUT) {
                        result, _ ->
                    completion(result)
                }
            }
        }
    }

    fun updateEditedWorkout(workoutToSave: Workout, completion: (result: FirebaseRequestResult) -> Unit) {
        workoutToEdit.docReference?.let { reference ->
            val firebaseWorkoutToSave = FirebaseWorkout()
            val workoutToSaveExercises = ArrayList<FirebaseWorkoutElement>()
            var firebaseWorkoutElement: FirebaseWorkoutElement
            firebaseWorkoutToSave.docReference = reference
            firebaseWorkoutToSave.userId = workoutToSave.userId
            workoutToSave.exercises?.forEach { workoutElementToSave ->
                firebaseWorkoutElement = FirebaseWorkoutElement()
                firebaseWorkoutElement.exercise = workoutElementToSave.exercise?.docReference
                firebaseWorkoutElement.isTimeIntervalMode = workoutElementToSave.isTimeIntervalMode
                firebaseWorkoutElement.numOfReps = workoutElementToSave.numOfReps
                firebaseWorkoutElement.numOfSets = workoutElementToSave.numOfSets
                firebaseWorkoutElement.timeInterval = workoutElementToSave.timeInterval
                workoutToSaveExercises.add(firebaseWorkoutElement)
                if (workoutToSaveExercises.size == workoutToSave.exercises?.size) {
                    firebaseWorkoutToSave.name = workoutToSave.name
                    firebaseWorkoutToSave.initDate = workoutToSave.initDate
                    firebaseWorkoutToSave.workoutElements = workoutToSaveExercises
                    DatabaseService.updateDocument(firebaseWorkoutToSave, UserDataType.WORKOUT) {
                            result ->
                        completion(result)
                    }
                }
            }

        }
    }
}