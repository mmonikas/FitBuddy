package com.monika.Services

class DataValidator {

    companion object{
        val instance = DataValidator()
    }

    fun isNameValid(name: String): Boolean {
        return name.isNotEmpty() && !name.isNullOrBlank() && name.length > 2
    }

    fun isEmailValid(email: String): Boolean {
        return email.isNotEmpty() && !email.isNullOrBlank()
    }

    fun isPasswordValid(password: String): Boolean {
        return password.isNotEmpty() && !password.isNullOrBlank() && password.length > 4
        //TODO for now, password has to be better and so does email
    }
}