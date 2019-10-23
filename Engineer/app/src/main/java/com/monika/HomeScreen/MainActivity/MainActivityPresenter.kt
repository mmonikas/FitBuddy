package com.monika.HomeScreen.MainActivity

import com.google.firebase.auth.FirebaseAuth

class MainActivityPresenter {

    fun logOutUser() {
        FirebaseAuth.getInstance().signOut()
    }
}