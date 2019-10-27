package com.monika.Model.WorkoutPlan

import java.io.Serializable
import java.util.Date
import java.sql.Timestamp

class FirebaseWorkout: Serializable {

    val userId: String? = null
    val name: String? = null
    val initDate: Date? = null
    val workoutElements: List<String>? = null

    constructor()
}