package com.monika.HomeScreen.CalendarPager

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.monika.Enums.UserDataType
import com.monika.Model.WorkoutComponents.Exercise
import com.monika.Model.WorkoutComponents.WorkoutElement
import com.monika.Model.WorkoutPlan.*
import com.monika.Services.DatabaseService


class CalendarDayFragmentPresenter {

    private val currentUser = FirebaseAuth.getInstance().currentUser

    fun fetchWorkouts(completion: (result: ArrayList<Workout>) -> Unit) {
        if (currentUser != null) {
            var workoutsList = ArrayList<Workout>()
            var workoutToAdd = Workout()
            var currentWorkoutComponentsPaths: List<String>

            DatabaseService.instance.fetchUserData(UserDataType.WORKOUT, currentUser.uid) {
                    result ->
                val firebaseWorkoutList = result as ArrayList<FirebaseWorkout>
                firebaseWorkoutList.forEach { workout ->
                    workoutToAdd.docReference = workout.docReference
                    workoutToAdd.userId = currentUser.uid
                    workoutToAdd.initDate = workout.initDate
                    workoutToAdd.name = workout.name
                    workoutToAdd.exercises = ArrayList()
                    currentWorkoutComponentsPaths = workout.workoutElements!!
                    currentWorkoutComponentsPaths?.forEach { path ->
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
                                    if(workoutToAdd.exercises?.size == currentWorkoutComponentsPaths.size) {
                                        workoutsList.add(workoutToAdd)
                                    }
                                    if(workoutsList.size == firebaseWorkoutList.size) {
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

    fun fetchUserExercises(completion: (result: ArrayList<Exercise>) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            DatabaseService.instance.fetchUserData(UserDataType.EXERCISE, currentUser.uid) {
                    result ->
                val exercisesList = result as ArrayList<Exercise>
                completion(exercisesList)
            }
        }
    }

    fun fetchBaseExercises(completion: (result: ArrayList<Exercise>) -> Unit) {
        DatabaseService.instance.fetchBaseData(UserDataType.EXERCISE) {
                result ->
            val exercisesList = result as ArrayList<Exercise>
            completion(exercisesList)
        }
    }


    private fun fetchExercise(documentId: String, completion: (result: Any) -> Unit) {
        DatabaseService.instance.fetchCustomDocument(UserDataType.EXERCISE, documentId) {
                result ->
//            val exercises = result as ArrayList<Exercise>
            val exercise = result as Exercise
            completion(exercise)
        }
    }

    private fun fetchWorkout(documentId: String, completion: (result: Any) -> Unit) {
        var workout: FirebaseWorkout
        val workoutToAdd = Workout()
        var currentWorkoutComponentsPaths: List<String>
        DatabaseService.instance.fetchCustomDocument(UserDataType.WORKOUT, documentId) {
                result ->
            workout = result as FirebaseWorkout
            workoutToAdd.docReference = workout.docReference
            workoutToAdd.userId = currentUser?.uid
            workoutToAdd.initDate = workout.initDate
            workoutToAdd.name = workout.name
            workoutToAdd.exercises = ArrayList()
            currentWorkoutComponentsPaths = workout.workoutElements!!
            currentWorkoutComponentsPaths?.forEach { path ->
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
                            if(workoutToAdd.exercises?.size == currentWorkoutComponentsPaths.size) {
                                completion(workoutToAdd)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun fetchWorkoutElement(documentId: String, completion: (result: Any) -> Unit) {
        DatabaseService.instance.fetchCustomDocument(UserDataType.WORKOUT_ELEMENT, documentId) {
                result ->
            val element = result as FirebaseWorkoutElement
            completion(element)
        }
    }

    fun fetchPlannedWorkouts(timestamp: Timestamp, completion: (result: ArrayList<PlannedWorkout>) -> Unit) {
        if (currentUser != null) {
            DatabaseService.instance.fetchUserPlannedWorkouts(timestamp = timestamp.toDate(), userId = currentUser.uid) {
                documents ->
                if (documents.size > 0) {
                    val firebasePlannedWorkouts = documents as ArrayList<FirebasePlannedWorkout>
                    val plannedWorkouts = ArrayList<PlannedWorkout>()
                    val singlePlannedWorkout = PlannedWorkout()
                    firebasePlannedWorkouts.forEach { firebasePlannedWorkout ->
                        singlePlannedWorkout.date = firebasePlannedWorkout.date
                        singlePlannedWorkout.completed = firebasePlannedWorkout.completed
                        firebasePlannedWorkout.workout?.let { reference ->
                            fetchWorkout(reference) { workout ->
                                singlePlannedWorkout.workout = workout as Workout
                                plannedWorkouts.add(singlePlannedWorkout)
                                if (plannedWorkouts.size == firebasePlannedWorkouts.size) {
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
}
