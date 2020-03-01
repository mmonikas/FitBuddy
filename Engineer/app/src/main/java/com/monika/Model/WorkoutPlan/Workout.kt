package com.monika.Model.WorkoutPlan

import com.monika.Model.WorkoutComponents.MyDocument
import com.monika.Model.WorkoutComponents.WorkoutElement
import java.io.Serializable
import java.util.*

class Workout: Serializable, MyDocument {

    var name: String? = null
    var initDate: Date? = null
    var exercises: ArrayList<WorkoutElement>? = null

    constructor()
}