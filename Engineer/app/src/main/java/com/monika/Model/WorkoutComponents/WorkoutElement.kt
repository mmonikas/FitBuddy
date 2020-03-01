package com.monika.Model.WorkoutComponents

import java.io.Serializable

class WorkoutElement: Serializable, MyDocument {
    var exercise: Exercise? = null
    var numOfSets: Int? = null
    var numOfReps: Int? = null
    var timeInterval: Int? = null
    var isTimeIntervalMode: Boolean? = null

    constructor()
}