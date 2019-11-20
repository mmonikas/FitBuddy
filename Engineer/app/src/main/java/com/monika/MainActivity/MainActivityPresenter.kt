package com.monika.MainActivity

import com.google.firebase.auth.FirebaseAuth
import com.monika.Enums.UserDataType
import com.monika.Model.ModelSingletons.Categories
import com.monika.Model.ModelSingletons.EquipmentItems
import com.monika.Model.WorkoutComponents.Category
import com.monika.Model.WorkoutComponents.Equipment
import com.monika.Services.DatabaseService

class MainActivityPresenter {

    var categoriesList = ArrayList<Category>()
    var equipmentList = ArrayList<Equipment>()

    fun getAllCategories(completion: (result: Any) -> Unit) {
        DatabaseService.instance.fetchBaseData(UserDataType.CATEGORY, sortByField = "name") {
            result ->
            categoriesList = result as ArrayList<Category>
            if (!categoriesList.isNullOrEmpty()) {
                Categories.instance.setItems(categoriesList)
                completion

            }
            else {
                completion
            }
        }
    }

    fun getAllEquipment(completion: (result: Any) -> Unit) {
        DatabaseService.instance.fetchBaseData(UserDataType.EQUIPMENT) {
                result ->
            equipmentList = result as ArrayList<Equipment>
            if (!equipmentList.isNullOrEmpty()) {
                EquipmentItems.instance.setItems(equipmentList)
                completion
            }
            else {
                completion
            }
        }
    }
}