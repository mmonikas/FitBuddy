package com.monika.SignInAndRegister

import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.monika.Enums.FirebaseRequestResult
import com.monika.Enums.UserDataType
import com.monika.HomeScreen.MainActivity
import com.monika.Model.WorkoutComponents.Exercise
import com.monika.Model.WorkoutPlan.Workout
import com.monika.Services.AuthenticationService
import com.monika.Services.DatabaseService

class LoginFragmentPresenter {

    var workouts = ArrayList<Workout>()
    var exercises = ArrayList<Exercise>()

    fun signInUserWith(username: String, password: String, activity: MainActivity, completionHandler:
        (result: FirebaseRequestResult) -> Unit) {
        if (!username.isNullOrEmpty() && !password.isNullOrEmpty()) {
            AuthenticationService.instance.signInUserWithCredentials(activity, username, password) {
                    result ->
                completionHandler(result)
            }
        }
        else {
            completionHandler(FirebaseRequestResult.FAILURE)
        }
    }

    fun signInUserWithGoogle(data: Intent?, completionHandler: (result: FirebaseRequestResult) -> Unit) {
        if (data != null) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    AuthenticationService.instance.signInUserWithGoogle(account) {
                            result ->
                        completionHandler(result)
                    }
                }
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("Login", "Google sign in failed", e)
                completionHandler(FirebaseRequestResult.FAILURE)
            }

        }
    }

    fun fetchUserWorkouts(completion: (result: ArrayList<Workout>) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            DatabaseService.instance.fetchUserData(UserDataType.WORKOUT, currentUser.uid) {
                    result ->
                val workoutList = result as ArrayList<Workout>
                completion(workoutList)
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
}