package com.monika.Model.WorkoutPlan

data class Workout (
    val userID: String? = null,
    val name: String? = null
   // val exercises: Array<WorkoutElement>?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Workout

        if (userID != other.userID) return false
        if (name != other.name) return false
//        if (exercises != null) {
//            if (other.exercises == null) return false
//            if (!exercises.contentEquals(other.exercises)) return false
//        } else if (other.exercises != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = userID?.hashCode() ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
       // result = 31 * result + (exercises?.contentHashCode() ?: 0)
        return result
    }
}