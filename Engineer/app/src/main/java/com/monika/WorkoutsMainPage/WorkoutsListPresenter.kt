package com.monika.WorkoutsMainPage

import com.google.firebase.auth.FirebaseAuth
import com.monika.Enums.UserDataType
import com.monika.Model.WorkoutPlan.Workout
import com.monika.Services.DatabaseService

class WorkoutsListPresenter {

    var workoutsList = ArrayList<Workout>()

    fun fetchUserWorkouts(completion: (result: ArrayList<Workout>) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            DatabaseService.instance.fetchUserData(UserDataType.WORKOUT, currentUser.uid) {
                    result ->
                //workoutsList = result as ArrayList<Workout>
                completion(workoutsList)
            }
        }
    }
}