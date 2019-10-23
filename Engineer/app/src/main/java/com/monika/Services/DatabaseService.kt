package com.monika.Services

import android.util.Log
import com.google.firebase.auth.UserInfo
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.monika.Enums.UserDataType
import com.monika.Model.WorkoutComponents.Category
import com.monika.Model.WorkoutComponents.Equipment
import com.monika.Model.WorkoutComponents.Exercise
import com.monika.Model.WorkoutComponents.WorkoutElement
import com.monika.Model.WorkoutPlan.FirebaseWorkout
import com.monika.Model.WorkoutPlan.FirebaseWorkoutElement
import com.monika.Model.WorkoutPlan.PlannedWorkout

class DatabaseService {

    companion object {
        val instance = DatabaseService()
    }

    fun fetchUserData(requestedDataType: UserDataType, userId: String, completion: (result: ArrayList<Any>) -> Unit) {
        val dbCollectionToQuery = getCollectionForRequestedType(requestedDataType)
        val db = FirebaseFirestore.getInstance()
        db.collection(dbCollectionToQuery)
            .whereEqualTo("userID", userId)
            .get()
            .addOnSuccessListener { documents ->
                val dataList: ArrayList<Any> = getProcessedFetchedDataArray(documents, requestedDataType)
                Log.w("USER_DATA_FETCHING", "Successfully fetched documents: ${requestedDataType.name}")
                completion(dataList)
            }
            .addOnFailureListener { exception ->
                Log.w("USER_DATA_FETCHING", "Error getting documents: ", exception)
                completion(ArrayList())
            }

    }

    fun fetchBaseData(requestedDataType: UserDataType, completion: (result: ArrayList<Any>) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val dbCollectionToQuery = getCollectionForRequestedType(requestedDataType)
        db.collection(dbCollectionToQuery)
            .get()
            .addOnSuccessListener { documents ->
                val dataList: ArrayList<Any> = getProcessedFetchedDataArray(documents, requestedDataType)
                Log.w("DATA_FETCHING", "Successfully fetched documents: ${requestedDataType.name}")
                completion(dataList)
            }
            .addOnFailureListener { exception ->
                Log.w("DATA_FETCHING", "Error getting documents: ", exception)
                completion(ArrayList())
            }
    }

    fun fetchCustomDocument(dataType: UserDataType, documentId: String, completion: (result: Any?) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val dbCollectionToQuery = getCollectionForRequestedType(dataType)
        db.collection(dbCollectionToQuery)
            .document(documentId)
            .get()
            .addOnSuccessListener { document ->
                val data = getProcessedFetchedDataDocument(document, dataType)
                Log.w("DATA_FETCHING", "Successfully fetched documents: ${dataType.name}")
                completion(data)
            }
            .addOnFailureListener { exception ->
                Log.w("DATA_FETCHING", "Error getting documents: ", exception)
                completion(null)
            }
    }

    private fun getProcessedFetchedDataArray(documents: QuerySnapshot, collectionType: UserDataType): ArrayList<Any> {
        val dataList = ArrayList<Any>()
        when (collectionType) {
            UserDataType.WORKOUT -> {
                for (document in documents) {
                    val element = document.toObject(FirebaseWorkout::class.java)
                    dataList.add(element)
                }
            }
            UserDataType.PLANNED_WORKOUT -> {
                for (document in documents) {
                    val element = document.toObject(PlannedWorkout::class.java)
                    dataList.add(element)
                }
            }
            UserDataType.WORKOUT_ELEMENT -> {
                for (document in documents) {
                    val element = document.toObject(FirebaseWorkoutElement::class.java)
                    dataList.add(element)
                }
            }
            UserDataType.EXERCISE -> {
                for (document in documents) {
                    val element = document.toObject(Exercise::class.java)
                    dataList.add(element)
                }
            }
            UserDataType.CATEGORY -> {
                for (document in documents) {
                    val element = document.toObject(Category::class.java)
                    dataList.add(element)
                }
            }
            UserDataType.EQUIPMENT -> {
                for (document in documents) {
                    val element = document.toObject(Equipment::class.java)
                    dataList.add(element)
                }
            }
            UserDataType.USER_INFO -> {
                for (document in documents) {
                    val element = document.toObject(UserInfo::class.java)
                    dataList.add(element)
                }
            }
        }
        return dataList
    }


    private fun getProcessedFetchedDataDocument(document: DocumentSnapshot, collectionType: UserDataType): Any? {
        when (collectionType) {
            UserDataType.WORKOUT -> {
                return document.toObject(FirebaseWorkout::class.java)
            }
            UserDataType.PLANNED_WORKOUT -> {
                return document.toObject(PlannedWorkout::class.java)
            }
            UserDataType.WORKOUT_ELEMENT -> {
                return document.toObject(WorkoutElement::class.java)
            }
            UserDataType.EXERCISE -> {
                return document.toObject(Exercise::class.java)
            }
            UserDataType.CATEGORY -> {
                return document.toObject(Category::class.java)
            }
            UserDataType.EQUIPMENT -> {
                return document.toObject(Equipment::class.java)
            }
            UserDataType.USER_INFO -> {
                return document.toObject(UserInfo::class.java)
            }
        }
    }

    private fun getCollectionForRequestedType(requestedDataType: UserDataType): String {
        return when (requestedDataType) {
            UserDataType.WORKOUT -> "Workouts"
            UserDataType.PLANNED_WORKOUT -> "PlannedWorkout"
            UserDataType.WORKOUT_ELEMENT -> "WorkoutElement"
            UserDataType.EXERCISE -> "Exercise"
            UserDataType.CATEGORY -> "Category"
            UserDataType.EQUIPMENT -> "Equipment"
            UserDataType.USER_INFO -> "User"
        }
    }

}