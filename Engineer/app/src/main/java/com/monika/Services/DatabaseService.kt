package com.monika.Services

import com.google.firebase.database.DatabaseReference

class DatabaseService {

    private lateinit var database: DatabaseReference

    companion object {
        val instance = DatabaseService()
    }


}