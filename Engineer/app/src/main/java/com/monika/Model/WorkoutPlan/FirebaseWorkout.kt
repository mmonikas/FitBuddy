package com.monika.Model.WorkoutPlan

import com.monika.Model.WorkoutComponents.MyDocument
import java.io.Serializable
import java.util.Date
import java.sql.Timestamp

class FirebaseWorkout: Serializable, MyDocument {

    var name: String? = null
    var initDate: Date? = null
    var workoutElements: List<String>? = null

    constructor()
}