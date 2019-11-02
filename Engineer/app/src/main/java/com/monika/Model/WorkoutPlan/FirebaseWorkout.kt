package com.monika.Model.WorkoutPlan

import com.monika.Model.WorkoutComponents.MyDocument
import java.io.Serializable
import java.util.Date
import java.sql.Timestamp

class FirebaseWorkout: Serializable, MyDocument {

    val name: String? = null
    val initDate: Date? = null
    val workoutElements: List<String>? = null

    constructor()
}