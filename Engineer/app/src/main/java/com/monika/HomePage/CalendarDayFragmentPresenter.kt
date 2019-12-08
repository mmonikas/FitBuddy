package com.monika.HomePage

import com.google.firebase.auth.FirebaseAuth
import com.monika.Enums.FirebaseRequestResult
import com.monika.Enums.UserDataType
import com.monika.Model.WorkoutComponents.Exercise
import com.monika.Model.WorkoutComponents.WorkoutElement
import com.monika.Model.WorkoutPlan.*
import com.monika.Services.DataValidator
import com.monika.Services.DatabaseService


class CalendarDayFragmentPresenter {

    private val currentUser = FirebaseAuth.getInstance().currentUser
    var plannedWorkouts = ArrayList<PlannedWorkout>()

    fun fetchWorkouts(completion: (result: ArrayList<Workout>) -> Unit) {
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



    fun fetchUserExercises(completion: (result: ArrayList<Exercise>) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            DatabaseService.fetchUserData(UserDataType.EXERCISE) {
                    result ->
                val exercisesList = result as ArrayList<Exercise>
                completion(exercisesList)
            }
        }
    }

    fun fetchBaseExercises(completion: (result: ArrayList<Exercise>) -> Unit) {
        DatabaseService.fetchBaseData(UserDataType.EXERCISE) {
                result ->
            val exercisesList = result as ArrayList<Exercise>
            completion(exercisesList)
        }
    }

    private fun fetchWorkout(documentId: String, completion: (result: Any) -> Unit) {
        var workout: FirebaseWorkout
        val workoutToAdd = Workout()
        var currentWorkoutComponentsPaths: List<FirebaseWorkoutElement>
        DatabaseService.fetchCustomDocument(UserDataType.WORKOUT, documentId) {
                result ->
            workout = result as FirebaseWorkout
            getWorkout(workout) {
                result ->
                completion(result)
            }
        }
    }

    private fun fetchWorkoutElement(documentId: String, completion: (result: Any) -> Unit) {
        DatabaseService.fetchCustomDocument(UserDataType.WORKOUT_ELEMENT, documentId) {
                result ->
            val element = result as FirebaseWorkoutElement
            completion(element)
        }
    }

    fun fetchPlannedWorkouts(timestamp: String, completion: (result: ArrayList<PlannedWorkout>) -> Unit) {
        var singlePlannedWorkout : PlannedWorkout
        val plannedWorkouts = ArrayList<PlannedWorkout>()
        var firebasePlannedWorkouts: ArrayList<FirebasePlannedWorkout>
        if (currentUser != null) {
            DatabaseService.fetchUserPlannedWorkouts(timestamp = timestamp, userId = currentUser.uid) {
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

    fun registerPlannedWorkoutCompletion(plannedWorkout: PlannedWorkout,
                                         completion: (result: FirebaseRequestResult) -> Unit) {
        plannedWorkout.docReference?.let { reference ->
            plannedWorkout.completed = true
            DatabaseService.updateField(reference, "completed", true) {
                result ->
                completion(result)
            }
        }
    }

    fun registerAddedPlannedWorkout(plannedWorkout: PlannedWorkout, completion: (result: Any) -> Unit) {
        val firebasePlannedWorkout = FirebasePlannedWorkout()
        firebasePlannedWorkout.completed = plannedWorkout.completed
        firebasePlannedWorkout.date = plannedWorkout.date
        firebasePlannedWorkout.workout = plannedWorkout.workout?.docReference
        DatabaseService.saveNewDocument(firebasePlannedWorkout, UserDataType.PLANNED_WORKOUT) {
                result, _ ->
            if (result == FirebaseRequestResult.SUCCESS) {
                plannedWorkouts.add(plannedWorkout)
            }
            completion(result)
        }
    }

    fun canBeCompleted(plannedWorkout: PlannedWorkout): Boolean {
        return DataValidator.isPlannedWorkoutValidForCompleting(plannedWorkout)
    }
}
