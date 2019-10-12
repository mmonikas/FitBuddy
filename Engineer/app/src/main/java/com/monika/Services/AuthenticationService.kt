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


class AuthenticationService {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInAccount
    //Google Login Request Code
    //Google Sign In Client
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    companion object {
        val instance = AuthenticationService()
    }

    fun logUserWithCredentials(activity: Activity, username: String, password: String, completion: (result: FirebaseRequestResult) -> Unit) {
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

//    fun logUserWithGoogle(activity: Activity, completion: (result: FirebaseRequestResult) -> Unit) {
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(R.string.default_web_client_id)
//            .requestEmail()
//            .build()
//        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso)
//
//        val signInIntent = mGoogleSignInClient.getSignInIntent()
//        startActivityForResult(signInIntent, RC_SIGN_IN)
//    }

    fun logUserWithGoogle(acct: GoogleSignInAccount, completion: (result: FirebaseRequestResult) -> Unit) {
        Log.d("Login", "firebaseAuthWithGoogle:" + acct.id!!)
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth = FirebaseAuth.getInstance()
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Login", "signInWithCredential:success")
                    val user = auth.currentUser
                    completion(FirebaseRequestResult.SUCCESS)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Login", "signInWithCredential:failure", task.exception)
                    completion(FirebaseRequestResult.FAILURE)
                }

            }
    }
}