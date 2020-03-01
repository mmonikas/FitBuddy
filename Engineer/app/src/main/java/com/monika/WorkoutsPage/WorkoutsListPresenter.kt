package com.monika.WorkoutsPage

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.monika.Enums.FirebaseRequestResult
import com.monika.Enums.UserDataType
import com.monika.Model.WorkoutComponents.Exercise
import com.monika.Model.WorkoutComponents.WorkoutElement
import com.monika.Model.WorkoutPlan.FirebasePlannedWorkout
import com.monika.Model.WorkoutPlan.FirebaseWorkout
import com.monika.Model.WorkoutPlan.FirebaseWorkoutElement
import com.monika.Model.WorkoutPlan.Workout
import com.monika.Services.DatabaseService
import java.util.*
import kotlin.collections.ArrayList


class WorkoutsListPresenter {

    var workouts = ArrayList<Workout>()
    var workoutsToSaveAsPlanned = ArrayList<FirebasePlannedWorkout>()


    fun planWorkoutForDates(
        workout: Workout,
        dates: ArrayList<String>,
        completion: (result: FirebaseRequestResult) -> Unit) {
        FirebaseAuth.getInstance().currentUser?.uid?.let {
            uid ->
            var workoutToPlan : FirebasePlannedWorkout
            dates.forEach {
                workoutToPlan = FirebasePlannedWorkout()
                workoutToPlan.workout = workout.docReference
                workoutToPlan.date = it
                workoutToPlan.completed = false
                workoutToPlan.userId = uid
                workoutsToSaveAsPlanned.add(workoutToPlan)
            }
            DatabaseService.savePlannedWorkouts(workoutsToSaveAsPlanned) { result ->
                completion(result)
            }
        }


    }

    private fun getTimestampFrom(date: Date): Timestamp {
        return Timestamp(date)
    }

    fun fetchUserWorkouts(completion: (result: ArrayList<Workout>) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            var workoutsList = ArrayList<Workout>()

            var workoutElementToAdd : WorkoutElement
            var exercise: Exercise
            var workoutElement : FirebaseWorkoutElement
            var firebaseExercise: Exercise


            DatabaseService.fetchUserData(UserDataType.WORKOUT) {
                    result ->
                val firebaseWorkoutList = result as ArrayList<FirebaseWorkout>
                firebaseWorkoutList.forEach { workout ->
                    getWorkout(workout) { result: Workout ->
                        workoutsList.add(result)
                        if (workoutsList.size == firebaseWorkoutList.size) {
                            completion(workoutsList)
                        }
                    }
                }
            }
        }
    }
    //completion(workoutToAdd)

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
        DatabaseService.fetchCustomDocument(UserDataType.EXERCISE, documentId) {
                result ->
//                        val exercises = result as ArrayList<Exercise>
//            val exercise = exercises.first()
            val exercise = result as Exercise
            completion(exercise)
        }
    }

    fun fetchWorkoutElement(documentId: String, completion: (result: Any) -> Unit) {
        DatabaseService.fetchCustomDocument(UserDataType.WORKOUT_ELEMENT, documentId) {
                result ->
            val element = result as FirebaseWorkoutElement
            completion(element)
        }
    }

    fun removeWorkoutAt(position: Int, completion: (result: FirebaseRequestResult) -> Unit) {
        val item = workouts[position]
        DatabaseService.removeDocument(item, UserDataType.WORKOUT) {
                result ->
            if (result == FirebaseRequestResult.SUCCESS) {
                workouts.removeAt(position)
            }
            completion(result)
        }

    }

    fun checkIfWorkoutCanBeDeleted(position: Int, completion: (result: Boolean?) -> Unit) {
        val workoutId = workouts[position].docReference
        if (workoutId != null) {
            DatabaseService.checkIfExistsWorkoutInPlannedWorkouts(workoutDocReference = workoutId) {
                existsInPlannedWorkouts ->
                completion(existsInPlannedWorkouts)
            }
        }
        else {
            completion(null)
        }
    }
//    fun fetchUserWorkouts(completion: (result: ArrayList<Workout>) -> Unit) {
//        val currentUser = FirebaseAuth.getInstance().currentUser
//        if (currentUser != null) {
//            DatabaseService.instance.fetchUserData(UserDataType.WORKOUT, currentUser.uid) {
//                    result ->
//                //workoutsList = result as ArrayList<Workout>
//                completion(workoutsList)
//            }
//        }
//    }
}
