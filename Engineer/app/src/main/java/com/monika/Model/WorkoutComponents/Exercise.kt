package com.monika.Model.WorkoutComponents

import java.io.Serializable

class Exercise : Serializable {

    val name: String? = null
    val description: String? = null
    val equipment: Equipment? = null
    val load: Double? = null
//    val level: Int? = null
    val category: String? = null
    val userId: String? = null

    constructor()
}