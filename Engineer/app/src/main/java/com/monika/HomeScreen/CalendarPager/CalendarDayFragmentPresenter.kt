package com.monika.HomeScreen.CalendarPager

import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.monika.Enums.UserDataType
import com.monika.Model.WorkoutComponents.Exercise
import com.monika.Model.WorkoutComponents.WorkoutElement
import com.monika.Model.WorkoutPlan.FirebaseWorkout
import com.monika.Model.WorkoutPlan.FirebaseWorkoutElement
import com.monika.Model.WorkoutPlan.Workout
import com.monika.Services.DatabaseService




class CalendarDayFragmentPresenter {

    fun getOptionsForDataFetching() : FirestoreRecyclerOptions<Exercise> {
        val query : Query = DatabaseService.instance.getQueryForFetching(UserDataType.EXERCISE)
        return FirestoreRecyclerOptions.Builder<Exercise>()
                .setQuery(query, Exercise::class.java)
                .build()
    }

    fun fetchWorkouts(completion: (result: ArrayList<Workout>) -> Unit) {
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
            val exercises = result as ArrayList<Exercise>
            val exercise = exercises.first()
            completion(exercise)
        }
    }

    private fun fetchWorkoutElement(documentId: String, completion: (result: Any) -> Unit) {
        DatabaseService.instance.fetchCustomDocument(UserDataType.WORKOUT_ELEMENT, documentId) {
                result ->
            val elements = result as ArrayList<FirebaseWorkoutElement>
            val element = elements.first()
            completion(element)
        }
    }
}
