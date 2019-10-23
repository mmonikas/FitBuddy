package com.monika.Model.WorkoutComponents

import java.io.Serializable

class WorkoutElement: Serializable {
    var exercise: Exercise? = null
    var numOfSets: Int? = null
    var numOfReps: Int? = null

    constructor()
}