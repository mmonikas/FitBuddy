package com.monika.Model.User

import com.monika.Model.WorkoutComponents.MyDocument
import java.io.Serializable

class User : Serializable, MyDocument {
    var name: String? = null
    var uid: String? = null
    var password: String? = null
    var email: String? = null

    constructor()

}