package com.monika.Model.WorkoutPlan

import com.monika.Model.WorkoutComponents.WorkoutElement
import java.sql.Timestamp
import java.io.Serializable

class Workout: Serializable {

    var userID: String? = null
    var name: String? = null
    var initDate: Timestamp? = null
    var exercises: ArrayList<WorkoutElement>? = null

    constructor()
}