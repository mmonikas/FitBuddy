package com.monika.Model.WorkoutComponents

import java.io.Serializable

class Exercise : Serializable, MyDocument {
    var name: String? = null
    var description: String? = null
    var equipment: ArrayList<String>? = null
    var load: Double? = null
//    val level: Int? = null
    var category: String? = null

    constructor()
}