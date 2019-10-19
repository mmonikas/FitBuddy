package com.monika.Services

import android.util.Log
import com.google.firebase.auth.UserInfo
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.monika.Enums.UserDataType
import com.monika.Model.Exercise
import com.monika.Model.WorkoutComponents.Category
import com.monika.Model.WorkoutComponents.Equipment
import com.monika.Model.WorkoutComponents.WorkoutElement
import com.monika.Model.WorkoutPlan.PlannedWorkout
import com.monika.Model.WorkoutPlan.Workout

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
                val dataList: ArrayList<Any> = getProcessedFetchedData(documents, requestedDataType)
                Log.w("USER_DATA_FETCHING", "Successfully fetched documents: ${requestedDataType.name}")
                completion(dataList)
            }
            .addOnFailureListener { exception ->
                Log.w("USER_DATA_FETCHING", "Error getting documents: ", exception)
                completion(ArrayList())
            }

    }

    private fun getProcessedFetchedData(documents: QuerySnapshot, collectionType: UserDataType): ArrayList<Any> {
        val dataList = ArrayList<Any>()
        when (collectionType) {
            UserDataType.WORKOUT -> {
                for (document in documents) {
                val element = document.toObject(Workout::class.java)
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
                    val element = document.toObject(WorkoutElement::class.java)
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

    fun fetchWorkoutsOfUser(userId: String, completion: (result: ArrayList<Workout>) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val tag = "UserWorkoutsFetching"
        db.collection("Workouts")
        .whereEqualTo("userID", userId)
        .get()
        .addOnSuccessListener { documents ->
            val workoutsList = ArrayList<Workout>()
            for (document in documents) {
                val workout = document.toObject(Workout::class.java)
                workoutsList.add(workout)
            }
            Log.w(tag, "User workouts fetched successfully")
            completion(workoutsList)
        }
        .addOnFailureListener { exception ->
            Log.w(tag, "Error getting documents: ", exception)
            completion(ArrayList())
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