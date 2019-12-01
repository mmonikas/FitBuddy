package com.monika.WorkoutsMainPage

import com.google.firebase.auth.FirebaseAuth
import com.monika.Enums.FirebaseRequestResult
import com.monika.Enums.UserDataType
import com.monika.Model.WorkoutComponents.Exercise
import com.monika.Model.WorkoutComponents.WorkoutElement
import com.monika.Model.WorkoutPlan.*
import com.monika.Services.DatabaseService

class HistoryWorkoutsPresenter {
    var historyWorkouts = ArrayList<PlannedWorkout>()

    fun fetchAllPlannedWorkouts(completion: (result: ArrayList<PlannedWorkout>) -> Unit) {
        FirebaseAuth.getInstance().currentUser?.uid?.let {
            uid ->
            val plannedWorkouts = ArrayList<PlannedWorkout>()
            var firebasePlannedWorkouts: ArrayList<FirebasePlannedWorkout>
            DatabaseService.instance.fetchAllPlannedWorkoutsHistory(uid) {
                documents ->
                if (documents.size > 0) {
                    firebasePlannedWorkouts = documents
                    firebasePlannedWorkouts.forEach { firebasePlannedWorkout ->
                        val singlePlannedWorkout = PlannedWorkout()
                        singlePlannedWorkout.date = firebasePlannedWorkout.date
                        singlePlannedWorkout.userId = firebasePlannedWorkout.userId
                        singlePlannedWorkout.completed = firebasePlannedWorkout.completed
                        singlePlannedWorkout.docReference = firebasePlannedWorkout.docReference
                        firebasePlannedWorkout.workout?.let { reference ->
                            fetchWorkout(reference) { workout ->
                                singlePlannedWorkout.workout = workout as com.monika.Model.WorkoutPlan.Workout
                                plannedWorkouts.add(singlePlannedWorkout)
                                if (plannedWorkouts.size == firebasePlannedWorkouts.size) {
                                    historyWorkouts = plannedWorkouts
                                    completion(plannedWorkouts)
                                }
                            }
                        }
                    }
                }
                else {
                    completion(ArrayList())
                }
            }
        }

    }

    private fun fetchWorkout(documentId: String, completion: (result: Any) -> Unit) {
        var workout: FirebaseWorkout
        val workoutToAdd = Workout()
        var currentWorkoutComponentsPaths: List<FirebaseWorkoutElement>
        DatabaseService.instance.fetchCustomDocument(UserDataType.WORKOUT, documentId) {
                result ->
            workout = result as FirebaseWorkout
            getWorkout(workout) {
                    result ->
                completion(result)
            }
        }
    }

    private fun getWorkout(workout: FirebaseWorkout, completion: (result: Workout) -> Unit) {
        var currentWorkoutComponentsPath = workout.workoutElements!!
        var workoutToAdd = Workout()
        workoutToAdd.docReference = workout.docReference
        workoutToAdd.userId = workout.userId
        workoutToAdd.initDate = workout.initDate
        workoutToAdd.name = workout.name
        workoutToAdd.exercises = ArrayList()
        workout.workoutElements?.forEach { firebaseWorkoutElement ->
            getWorkoutElement(firebaseWorkoutElement) { resultWorkoutElement ->
                workoutToAdd.exercises?.add(resultWorkoutElement)
                if (workoutToAdd.exercises?.size == currentWorkoutComponentsPath.size) {
                    completion(workoutToAdd)
                }
            }
        }
    }

    private fun getWorkoutElement(firebaseWorkoutElement: FirebaseWorkoutElement, completion: (result: WorkoutElement) -> Unit) {
        val workoutElementToAdd = WorkoutElement()
        workoutElementToAdd.docReference = firebaseWorkoutElement.docReference
        workoutElementToAdd.numOfReps = firebaseWorkoutElement.numOfReps
        workoutElementToAdd.numOfSets = firebaseWorkoutElement.numOfSets
        workoutElementToAdd.timeInterval = firebaseWorkoutElement.timeInterval
        workoutElementToAdd.isTimeIntervalMode = firebaseWorkoutElement.isTimeIntervalMode
        val exerciseId = firebaseWorkoutElement.exercise
        exerciseId?.let {
            fetchExercise(exerciseId) { result ->
                val exercise = result as Exercise
                workoutElementToAdd.exercise = exercise
                completion(workoutElementToAdd)
            }
        }

    }


    fun fetchExercise(documentId: String, completion: (result: Any) -> Unit) {
        DatabaseService.instance.fetchCustomDocument(UserDataType.EXERCISE, documentId) {
                result ->
            //                        val exercises = result as ArrayList<Exercise>
//            val exercise = exercises.first()
            val exercise = result as Exercise
            completion(exercise)
        }
    }

}