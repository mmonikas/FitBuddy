package com.monika.WorkoutsMainPage

import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.firebase.ui.firestore.SnapshotParser
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.monika.Enums.FirebaseRequestResult
import com.monika.Enums.UserDataType
import com.monika.Model.WorkoutComponents.Exercise
import com.monika.Model.WorkoutComponents.WorkoutElement
import com.monika.Model.WorkoutPlan.FirebasePlannedWorkout
import com.monika.Model.WorkoutPlan.FirebaseWorkout
import com.monika.Model.WorkoutPlan.FirebaseWorkoutElement
import com.monika.Model.WorkoutPlan.Workout
import com.monika.Services.DatabaseService
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


class WorkoutsListPresenter {

    var workouts = ArrayList<Workout>()
    var workoutsToSaveAsPlanned = ArrayList<FirebasePlannedWorkout>()


    fun planWorkoutForDates(
        workout: Workout,
        dates: ArrayList<String>,
        completion: (result: FirebaseRequestResult) -> Unit) {
        val workoutToPlan = FirebasePlannedWorkout()
        dates.forEach {
            workoutToPlan.workout = workout.docReference
            workoutToPlan.date = it
            workoutToPlan.completed = false
            workoutsToSaveAsPlanned.add(workoutToPlan)
        }
        DatabaseService.instance.savePlannedWorkouts(workoutsToSaveAsPlanned) { result ->
            completion(result)
        }

    }

    private fun getTimestampFrom(date: Date): Timestamp {
        return Timestamp(date)
    }

    fun fetchUserWorkouts(completion: (result: ArrayList<Workout>) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            var workoutsList = ArrayList<Workout>()
            var workoutToAdd = Workout()
            var currentWorkoutComponentsPath: List<String>

            DatabaseService.instance.fetchUserData(UserDataType.WORKOUT, currentUser.uid) {
                    result ->
                val firebaseWorkoutList = result as ArrayList<FirebaseWorkout>
                firebaseWorkoutList.forEach { workout ->
                    workoutToAdd.docReference = workout.docReference
                    workoutToAdd.userId = currentUser.uid
                    workoutToAdd.initDate = workout.initDate
                    workoutToAdd.name = workout.name
                    workoutToAdd.exercises = ArrayList()
                    currentWorkoutComponentsPath = workout.workoutElements!!

                    currentWorkoutComponentsPath?.forEach { path ->
                        fetchWorkoutElement(path) {
                                result ->
                            val workoutElement = result as FirebaseWorkoutElement
                            val workoutElementToAdd = WorkoutElement()
                            workoutElementToAdd.docReference = workoutElement.docReference
                            workoutElementToAdd.numOfReps = workoutElement.numOfReps
                            workoutElementToAdd.numOfSets = workoutElement.numOfSets
                            workoutElementToAdd.timer = workoutElement.timer
                            val exerciseId = workoutElement.exercise
                            exerciseId?.let {
                                fetchExercise(exerciseId) {
                                        result ->
                                    val exercise = result as Exercise
                                    workoutElementToAdd.exercise = exercise
                                    workoutToAdd.exercises?.add(workoutElementToAdd)
                                    if(workoutToAdd.exercises?.size == currentWorkoutComponentsPath.size) {
                                        workoutsList.add(workoutToAdd)
                                        completion(workoutsList)
                                    }
                                }
                            }

                        }
                    }
                }

            }
        }
    }



//    fun getOptionsForWorkoutsListListener(): FirestoreRecyclerOptions<Workout> {
//        val query: Query = DatabaseService.instance.getQueryForFetching(UserDataType.WORKOUT)
//            .orderBy("name", Query.Direction.ASCENDING)
//        var workoutToAdd = Workout()
//        var currentWorkoutComponentsPath: List<String>
//        val options = FirestoreRecyclerOptions.Builder<Workout>()
//            .setQuery(query) { snapshot ->
//                val firebaseWorkout = snapshot.toObject(FirebaseWorkout::class.java)
//                firebaseWorkout?.let {
//                    workoutToAdd.docReference = firebaseWorkout.docReference
//                    workoutToAdd.userId = firebaseWorkout.userId
//                    workoutToAdd.initDate = firebaseWorkout.initDate
//                    workoutToAdd.name = firebaseWorkout.name
//                    workoutToAdd.exercises = ArrayList()
//                    currentWorkoutComponentsPath = firebaseWorkout.workoutElements!!
//                    currentWorkoutComponentsPath?.forEach { path ->
//                        fetchWorkoutElement(path) { result ->
//                            val workoutElement = result as FirebaseWorkoutElement
//                            val workoutElementToAdd = WorkoutElement()
//                            workoutElementToAdd.docReference = workoutElement.docReference
//                            workoutElementToAdd.numOfReps = workoutElement.numOfReps
//                            workoutElementToAdd.numOfSets = workoutElement.numOfSets
//                            workoutElementToAdd.timer = workoutElement.timer
//                            val exerciseId = workoutElement.exercise
//                            exerciseId?.let {
//                                fetchExercise(exerciseId) { result ->
//                                    val exercise = result as Exercise
//                                    workoutElementToAdd.exercise = exercise
//                                    workoutToAdd.exercises?.add(workoutElementToAdd)
//                                    //workoutsList.add(workoutToAdd)
//                                    workoutToAdd?.let {
//                                        return@fetchExercise it
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//            .build()
//
//        return options
//    }


    fun fetchExercise(documentId: String, completion: (result: Any) -> Unit) {
        DatabaseService.instance.fetchCustomDocument(UserDataType.EXERCISE, documentId) {
                result ->
//                        val exercises = result as ArrayList<Exercise>
//            val exercise = exercises.first()
            val exercise = result as Exercise
            completion(exercise)
        }
    }

    fun fetchWorkoutElement(documentId: String, completion: (result: Any) -> Unit) {
        DatabaseService.instance.fetchCustomDocument(UserDataType.WORKOUT_ELEMENT, documentId) {
                result ->
            val element = result as FirebaseWorkoutElement
            completion(element)
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
