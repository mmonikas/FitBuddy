package com.monika.Model.WorkoutPlan

import java.util.*

data class PlannedWorkout (
    val workout: Workout,
    val date: Date,
    val completed: Boolean
)