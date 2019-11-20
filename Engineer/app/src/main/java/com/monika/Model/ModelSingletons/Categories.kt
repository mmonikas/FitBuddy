package com.monika.Model.ModelSingletons

import com.monika.Enums.UserDataType
import com.monika.Model.WorkoutComponents.Category
import com.monika.Services.DatabaseService

class Categories {

    private var items = ArrayList<Category>()

    companion object {
        val instance = Categories()
    }

    init {
        if (items.isEmpty()) {
            DatabaseService.instance.fetchBaseData(UserDataType.CATEGORY) { result ->
                (result as ArrayList<Category>).let {
                    items.clear()
                    items = it
                }
            }
        }
    }

    fun getItems() : ArrayList<Category> {
        if (items.isEmpty()) {
            items = getLocalItems()
        }
        return items
    }

    fun getNames() : List<String?> {
        return items.map{ category -> category.name }
    }

    fun getIdOf(name: String) : String? {
        val category = items.first { category -> category.name == name }
        category.docReference?.let {
            return it
        }
        return null
    }

    fun getNameOf(docReference: String) : String? {
        val category = items.first { category -> category.docReference == docReference }
        category.name?.let {
            return it
        }
        return null
    }

    fun setItems(categoriesList: ArrayList<Category>) {
        items = categoriesList
    }

    private fun getLocalItems() : ArrayList<Category> {
        val list = ArrayList<Category>()
        val ABS = Category().apply {
            name = "ABS"
            docReference = "Category/onROJitHSJcWp31jiTBU"
        }
        list.add(ABS)
        val back = Category().apply {
            name = "back"
            docReference = "Category/KJqreadRPWewA3YwLRWb"
        }
        list.add(back)
        val biceps = Category().apply {
            name = "biceps"
            docReference = "Category/W8EUO2o3pGUHhIhvaSYV"
        }
        list.add(biceps)
        val bodyweight = Category().apply {
            name = "bodyweight"
            docReference = "Category/j7zbA9KGkgc0QaVpbGIT"
        }
        list.add(bodyweight)
        val cardio = Category().apply {
            name = "cardio"
            docReference = "Category/wKKKmH3QwkfXpRehrOg3"
        }
        list.add(cardio)
        val chest = Category().apply {
            name = "chest"
            docReference = "Category/Y7Z0rYNrW3DNeJekExu0"
        }
        list.add(chest)
        val glutes = Category().apply {
            name = "glutes"
            docReference = "Category/EU9qa7DDTxCwkrQczxwY"
        }
        list.add(glutes)
        val legs = Category().apply {
            name = "legs"
            docReference = "Category/WkJwDbo9hzUVCeq0joUi"
        }
        list.add(legs)
        val shoulders = Category().apply {
            name = "shoulders"
            docReference = "Category/G66NDt5z7YNEuy2j8cbq"
        }
        list.add(shoulders)
        val triceps = Category().apply {
            name = "triceps"
            docReference = "Category/dD6AUEl50xq51pTBNyxu"
        }
        list.add(triceps)
        return list
    }
}