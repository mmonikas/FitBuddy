package com.monika.Model.WorkoutPlan

import java.io.Serializable
import java.sql.Timestamp

class FirebaseWorkout: Serializable {

    val userID: String? = null
    val name: String? = null
    val initDate: Timestamp? = null
    val exercises: Array<String>? = null

    constructor()
}