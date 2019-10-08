package com.monika.Model.WorkoutComponents

data class Exercise (
    val name: String? = null,
    val description: String? = null,
    val equipment: Equipment? = null,
    val load: Double? = null,
    val level: Int? = null,
    val category: Category? = null
)