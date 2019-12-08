package com.monika.MainActivity

import com.monika.Enums.UserDataType
import com.monika.Model.Categories
import com.monika.Model.WorkoutComponents.Category
import com.monika.Model.WorkoutComponents.Equipment
import com.monika.Model.WorkoutComponents.Exercise
import com.monika.Services.DatabaseService

class MainActivityPresenter {

    var categoriesList = ArrayList<Category>()
    var equipmentList = ArrayList<Equipment>()
    var exercisesList = ArrayList<Exercise>()

    fun getAllCategories(completion: (result: Any) -> Unit) {
        DatabaseService.fetchBaseData(UserDataType.CATEGORY, sortByField = "name") {
            result ->
            categoriesList = result as ArrayList<Category>
            if (!categoriesList.isNullOrEmpty()) {
                Categories.setItems(categoriesList)
                completion

            }
            else {
                completion
            }
        }
    }

    fun getAllExercises(completion: (result: Any) -> Unit) {
        DatabaseService.fetchUserData(UserDataType.EXERCISE) {
            result ->
            if(result.isNotEmpty()) {
                exercisesList = result as ArrayList<Exercise>
            }
            completion
        }
    }
}