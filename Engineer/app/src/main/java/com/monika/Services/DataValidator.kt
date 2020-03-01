package com.monika.Services

import com.monika.Model.WorkoutComponents.Exercise
 import com.monika.Model.WorkoutPlan.PlannedWorkout
import com.monika.Model.WorkoutPlan.Workout
 import java.text.SimpleDateFormat
 import java.util.*
 import java.util.Calendar.HOUR
 import java.util.Calendar.MINUTE
 import java.util.regex.Pattern

object DataValidator {

    fun isNameValid(name: String): Boolean {
        return name.isNotEmpty() && !name.isBlank() && name.length > 2
    }

    fun isEmailValid(email: String): Boolean {
        val pattern = Pattern.compile(".+@.+\\.[a-z]+")
        val matcher = pattern.matcher(email)
        return email.isNotEmpty() && !email.isBlank() && matcher.matches()
    }

    fun isPasswordValid(password: String): Boolean {
        return password.isNotEmpty() && !password.isBlank() && password.length > 7
    }

    fun isDataForNewWorkoutValid(workout: Workout) : Boolean {
        if (workout.name != null && workout.exercises != null) {
            return workout.name!!.isNotEmpty() && workout.exercises!!.isNotEmpty() && workout.initDate != null
        }
        return false
    }

    fun isPlannedWorkoutValidForCompleting(plannedWorkout: PlannedWorkout): Boolean {
        if (plannedWorkout.date != null) {
            val calendar = Calendar.getInstance()
            calendar.set(HOUR, 0)
            calendar.set(MINUTE, 0)
            val formatter = SimpleDateFormat("dd.MM.yyyy")
            return formatter.parse(plannedWorkout.date).before(calendar.time)
        }
        return false
    }

    fun isDataForExerciseValid(exercise: Exercise) : Boolean {
        if (exercise.name != null && exercise.description != null && exercise.equipment != null
            && exercise.category != null) {
            return exercise.name!!.isNotEmpty() && exercise.name!!.isNotBlank() &&
                    exercise.description!!.isNotEmpty() &&  exercise.description!!.isNotBlank() &&
                    exercise.equipment!!.isNotEmpty() &&  exercise.equipment!!.isNotBlank() &&
                    exercise.category!!.isNotEmpty() && exercise.category!!.isNotBlank()
        }
        return false
    }
}