package com.monika.HomeScreen

import com.google.firebase.auth.FirebaseAuth

class MainActivityPresenter {

    fun logOutUser() {
        FirebaseAuth.getInstance().signOut()
    }
}