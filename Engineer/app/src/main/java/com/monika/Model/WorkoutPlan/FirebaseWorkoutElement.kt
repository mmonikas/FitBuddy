package com.monika.Model.WorkoutPlan

import com.monika.Model.WorkoutComponents.MyDocument
import java.io.Serializable

class FirebaseWorkoutElement: Serializable, MyDocument {
    val exercise: String? = null
    val numOfSets: Int? = null
    val numOfReps: Int? = null
    val timer: Int? = null

    constructor()
}