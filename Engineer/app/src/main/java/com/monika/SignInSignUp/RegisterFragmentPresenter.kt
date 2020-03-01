package com.monika.SignInSignUp

import android.app.Activity
import com.monika.Enums.FirebaseRequestResult
import com.monika.Model.User.User
import com.monika.Services.AuthenticationService
import com.monika.Services.DataValidator
import com.monika.Services.DatabaseService

class RegisterFragmentPresenter {

    fun isDataValid(user: User, passwordRepeat: String): Boolean {
        val validator = DataValidator
        return validator.isNameValid(user.name!!) && validator.isEmailValid(user.email!!)
                && validator.isPasswordValid(user.password!!) && user.password!! == passwordRepeat
    }

    fun registerUserWithUserdata(activity: Activity, user: User, completionHandler: (result: FirebaseRequestResult) -> Unit) {
        val email = user.email
        val password = user.password
        if (email != null && password != null) {
            AuthenticationService.signUpUserWithUserdata(activity, email, password) {
                result ->
                if (result == FirebaseRequestResult.SUCCESS) {
                    completionHandler(result)
                    user.name?.let {
                        DatabaseService.setUserInfo(it) { result ->

                        }
                    }

                }
            }
        }
    }

}