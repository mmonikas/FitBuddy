package com.monika.Model.ModelSingletons

import com.monika.Enums.UserDataType
import com.monika.Model.WorkoutComponents.Equipment
import com.monika.Services.DatabaseService

class EquipmentItems {
    private var items = ArrayList<Equipment>()

    companion object {
        val instance = EquipmentItems()
    }

    init {
        if (items.isEmpty()) {
            DatabaseService.instance.fetchBaseData(UserDataType.EQUIPMENT) { result ->
                (result as ArrayList<Equipment>).let {
                    items = it
                }
            }
        }
    }

    fun getItems() : ArrayList<Equipment> {
        return items
    }

    fun getNames() : List<String?> {
        return items.map{ equipment -> equipment.name }
    }

    fun getIdOf(name: String) : String? {
        val equipment = items.first { equipment -> equipment.name == name }
        equipment.docReference?.let {
            return it
        }
        return null
    }

    fun getNameOf(docReference: String) : String? {
        val equipment = items.first { equipment -> equipment.docReference == docReference }
        equipment.name?.let {
            return it
        }
        return null
    }

    fun setItems(equipmentList: ArrayList<Equipment>) {
        items = equipmentList
    }
}