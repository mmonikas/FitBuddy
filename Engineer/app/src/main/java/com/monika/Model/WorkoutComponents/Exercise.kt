package com.monika.Model.WorkoutComponents

import java.io.Serializable

class Exercise : Serializable {

    var name: String? = null
    var description: String? = null
    var equipment: Equipment? = null
    var load: Double? = null
//    val level: Int? = null
    var category: String? = null
    var userId: String? = null

    constructor()
}