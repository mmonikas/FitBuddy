package com.monika.Model.WorkoutComponents

import java.io.Serializable

class Exercise : Serializable, MyDocument {
    var name: String? = null
    var description: String? = null
    var equipment: String? = null
    var category: String? = null

    constructor()
}