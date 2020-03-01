package com.monika.Services

import android.app.Activity
import android.content.ContentValues.TAG
import android.util.Log
import androidx.core.app.ActivityCompat.startActivityForResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.monika.Enums.FirebaseRequestResult
import com.monika.R


object AuthenticationService {

    private lateinit var auth: FirebaseAuth

    fun signInUserWithCredentials(activity: Activity, username: String, password: String,
                                  completion: (result: FirebaseRequestResult) -> Unit) {
        auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(username, password).addOnCompleteListener(activity) {
                task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "signInWithEmail:success")
                val user = auth.currentUser
                completion(FirebaseRequestResult.SUCCESS)
            } else {
                // If sign in fails, display a message to the user.
                Log.w(TAG, "signInWithEmail:failure", task.exception)
                completion(FirebaseRequestResult.FAILURE)
            }
        }


    }

    fun signInUserWithGoogle(acct: GoogleSignInAccount, completion: (result: FirebaseRequestResult) -> Unit) {
        Log.d("Login", "firebaseAuthWithGoogle:" + acct.id!!)
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth = FirebaseAuth.getInstance()
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.w("Google Login", "Google sign in success")
                    val user = auth.currentUser
                    completion(FirebaseRequestResult.SUCCESS)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Google Login", "Google sign in failure", task.exception)
                    completion(FirebaseRequestResult.FAILURE)
                }

            }
    }

    fun signUpUserWithUserdata(activity: Activity, email: String, password: String,
                               completion: (result: FirebaseRequestResult) -> Unit) {
//        val email = "mmail2@interia.pl"
//        val password = "moniczka123!"
        auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Registration", "createUserWithEmail:success")
                    val user = auth.currentUser
                    completion(FirebaseRequestResult.SUCCESS)

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Registration", "createUserWithEmail:failure", task.exception)
                    completion(FirebaseRequestResult.FAILURE)
                }
            }
    }
}