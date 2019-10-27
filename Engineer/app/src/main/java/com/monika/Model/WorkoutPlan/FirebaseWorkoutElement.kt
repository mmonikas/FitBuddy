package com.monika.Model.WorkoutPlan

import java.io.Serializable

class FirebaseWorkoutElement: Serializable {
    val exercise: String? = null
    val numOfSets: Int? = null
    val numOfReps: Int? = null
    val timer: Int? = null
    val userId: String? = null

    constructor()
}