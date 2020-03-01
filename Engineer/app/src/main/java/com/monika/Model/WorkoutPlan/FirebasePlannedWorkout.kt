package com.monika.Model.WorkoutPlan

import com.google.firebase.Timestamp
import com.monika.Model.WorkoutComponents.MyDocument
import java.io.Serializable

class FirebasePlannedWorkout: Serializable, MyDocument {
    var workout: String? = null
    var date: String? = null
    var completed: Boolean? = null

    constructor()
}