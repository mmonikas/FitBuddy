package com.monika.Model.WorkoutPlan

import com.monika.Model.WorkoutComponents.MyDocument
import java.io.Serializable

class FirebaseWorkoutElement: Serializable, MyDocument {
    var exercise: String? = null
    var numOfSets: Int? = null
    var numOfReps: Int? = null
    var timeInterval: Int? = null
    var isTimeIntervalMode: Boolean? = null

    constructor()
}