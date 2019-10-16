package com.monika.SignInAndRegister

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.monika.Enums.FirebaseRequestResult
import com.monika.HomeScreen.MainActivity
import com.monika.R
import com.monika.Services.AuthenticationService

class LoginFragmentPresenter {

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

}