package com.monika

import com.monika.Model.WorkoutComponents.Exercise
import com.monika.Model.WorkoutComponents.WorkoutElement
import com.monika.Model.WorkoutPlan.PlannedWorkout
import com.monika.Model.WorkoutPlan.Workout
import com.monika.Services.DataValidator
import org.junit.Assert
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DataValidatorUnitTest {

    private val validator = DataValidator

    @Test
    fun testNameValidation() {
        var name = "Monika"
        Assert.assertTrue(validator.isNameValid(name))

        name = "Mo"
        Assert.assertFalse(validator.isNameValid(name))

        name = "M"
        Assert.assertFalse(validator.isNameValid(name))

        name = ""
        Assert.assertFalse(validator.isNameValid(name))
    }

    @Test
    fun testEmailValidation() {
        var email = "monika.stobienia@gmail.com"
        Assert.assertTrue(validator.isEmailValid(email))

        email = ""
        Assert.assertFalse(validator.isEmailValid(email))

        email = "monika"
        Assert.assertFalse(validator.isEmailValid(email))

        email = "monika@"
        Assert.assertFalse(validator.isEmailValid(email))

        email = "@onet"
        Assert.assertFalse(validator.isEmailValid(email))

        email = "@onet.pl"
        Assert.assertFalse(validator.isEmailValid(email))

        email = "monika@onet"
        Assert.assertFalse(validator.isEmailValid(email))

        email = "@"
        Assert.assertFalse(validator.isEmailValid(email))

        email = "monika@.pl"
        Assert.assertFalse(validator.isEmailValid(email))

    }

    @Test
    fun testPasswordValidation() {

        var password = "monika123"
        Assert.assertTrue(validator.isPasswordValid(password))

        password = ""
        Assert.assertFalse(validator.isPasswordValid(password))

        password = "monika1"
        Assert.assertFalse(validator.isPasswordValid(password))

    }

    @Test
    fun testExerciseIsValid() {
        val exercise = Exercise()
        exercise.name = "name"
        exercise.description = "description"
        exercise.category = "category"
        exercise.equipment = "equipment"

        Assert.assertTrue(validator.isDataForExerciseValid(exercise))

        exercise.name = ""
        exercise.description = "description"
        exercise.category = "category"
        exercise.equipment = "equipment"
        Assert.assertFalse(validator.isDataForExerciseValid(exercise))

        exercise.name = "name"
        exercise.description = ""
        exercise.category = "category"
        exercise.equipment = "equipment"
        Assert.assertFalse(validator.isDataForExerciseValid(exercise))

        exercise.name = "name"
        exercise.description = "description"
        exercise.category = ""
        exercise.equipment = "equipment"
        Assert.assertFalse(validator.isDataForExerciseValid(exercise))

        exercise.name = "name"
        exercise.description = "description"
        exercise.category = "category"
        exercise.equipment = ""
        Assert.assertFalse(validator.isDataForExerciseValid(exercise))
    }

    @Test
    fun testWorkoutDataisValid() {
        val workout = Workout()
        workout.name = "name"
        workout.exercises = ArrayList()
        workout.initDate = Date()

        workout.name = ""
        Assert.assertFalse(validator.isDataForNewWorkoutValid(workout))

        workout.name = "name"
        Assert.assertFalse(validator.isDataForNewWorkoutValid(workout))

        val workoutElement = WorkoutElement()
        workoutElement.timeInterval = 50
        workoutElement.numOfSets = 4
        workoutElement.isTimeIntervalMode = true
        workoutElement.exercise = Exercise()
        workout.exercises?.add(workoutElement)


        Assert.assertTrue(validator.isDataForNewWorkoutValid(workout))

        workout.name = ""
        Assert.assertFalse(validator.isDataForNewWorkoutValid(workout))

        workout.name = "name"
        workout.initDate = null

        Assert.assertFalse(validator.isDataForNewWorkoutValid(workout))
    }

    @Test
    fun testPlannedWorkoutForCompletionValid() {
        val calendar = Calendar.getInstance()
        val plannedWorkout = PlannedWorkout()
        val formatter = SimpleDateFormat("dd.MM.yyyy")
        calendar.add(Calendar.DATE , -1)
        val yesterday = calendar.time
        plannedWorkout.date = formatter.format(yesterday)

        Assert.assertTrue(validator.isPlannedWorkoutValidForCompleting(plannedWorkout))

        calendar.add(Calendar.DATE, 3)
        val tomorrow = calendar.time
        plannedWorkout.date = formatter.format(tomorrow)
        Assert.assertFalse(validator.isPlannedWorkoutValidForCompleting(plannedWorkout))

    }
}