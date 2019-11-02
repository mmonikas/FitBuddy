package com.monika.WorkoutsMainPage

import com.google.firebase.Timestamp
import com.monika.Enums.FirebaseRequestResult
import com.monika.Model.WorkoutPlan.FirebasePlannedWorkout
import com.monika.Model.WorkoutPlan.FirebaseWorkoutElement
import com.monika.Model.WorkoutPlan.PlannedWorkout
import com.monika.Model.WorkoutPlan.Workout
import com.monika.Services.DatabaseService
import java.util.*
import kotlin.collections.ArrayList



class WorkoutsListPresenter {

    var workoutsList = ArrayList<Workout>()
    var workoutsToSaveAsPlanned = ArrayList<FirebasePlannedWorkout>()


    fun planWorkoutForDates(workout: Workout, dates: ArrayList<Date>, completion: (result: FirebaseRequestResult) -> Unit) {
        val workoutToPlan = FirebasePlannedWorkout()
        for (date in dates) {
            workoutToPlan.workout = workout.docReference
            workoutToPlan.date = getTimestampFrom(date)
            workoutToPlan.completed = false
            workoutsToSaveAsPlanned.add(workoutToPlan)
        }
        DatabaseService.instance.savePlannedWorkouts(workoutsToSaveAsPlanned) {
            result ->
            completion(result)
        }

    }

    private fun getTimestampFrom(date: Date): Timestamp {
        return Timestamp(date)
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
