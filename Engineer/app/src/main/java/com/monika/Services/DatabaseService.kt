package com.monika.Services

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserInfo
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.*
import com.monika.Enums.FirebaseRequestResult
import com.monika.Enums.UserDataType
import com.monika.Model.WorkoutComponents.Category
import com.monika.Model.WorkoutComponents.Equipment
import com.monika.Model.WorkoutComponents.Exercise
import com.monika.Model.WorkoutComponents.MyDocument
import com.monika.Model.WorkoutPlan.FirebasePlannedWorkout
import com.monika.Model.WorkoutPlan.FirebaseWorkout
import com.monika.Model.WorkoutPlan.FirebaseWorkoutElement
import com.monika.Model.WorkoutPlan.PlannedWorkout


class DatabaseService {

    private val db = FirebaseFirestore.getInstance()
    private val currentUser = FirebaseAuth.getInstance().currentUser

    companion object {
        val instance = DatabaseService()
    }

    fun fetchUserData(requestedDataType: UserDataType, userId: String, completion: (result: ArrayList<Any>) -> Unit) {
        val dbCollectionToQuery = getCollectionForRequestedType(requestedDataType)
        db.collection(dbCollectionToQuery)
            .whereEqualTo("userId", null)
            .get()
            .addOnSuccessListener { documents ->
                val dataList: ArrayList<Any> = getProcessedFetchedDataArray(documents, requestedDataType)
                Log.w("USER_DATA_FETCHING", "Successfully fetched documents: ${requestedDataType.name}")
                db.collection(dbCollectionToQuery)
                    .whereEqualTo("userId", userId)
                    .get()
                    .addOnSuccessListener { userDocuments ->
                        val userData = getProcessedFetchedDataArray(userDocuments, requestedDataType)
                        dataList.addAll(userData)
                        completion(dataList)
                    }
                    .addOnFailureListener { exception ->
                        Log.w("USER_DATA_FETCHING", "Error getting documents: ", exception)
                        completion(dataList)
                    }
            }
            .addOnFailureListener { exception ->
                Log.w("USER_DATA_FETCHING", "Error getting documents: ", exception)
                completion(ArrayList())
            }

    }

    fun fetchUserPlannedWorkouts(userId: String, timestamp: String, completion: (result: ArrayList<FirebasePlannedWorkout>) -> Unit) {
        val dbCollectionToQuery = getCollectionForRequestedType(UserDataType.PLANNED_WORKOUT)
        db.collection(dbCollectionToQuery)
            .whereEqualTo("date", timestamp)
            .get()
            .addOnSuccessListener { documents ->
                val dataList: ArrayList<Any> = getProcessedFetchedDataArray(documents, UserDataType.PLANNED_WORKOUT)
                Log.w("USER_PlannedWorkouts_FETCHING", "Successfully fetched documents: ${UserDataType.PLANNED_WORKOUT.name}")
                val userData = ArrayList<FirebasePlannedWorkout>()
                (dataList as ArrayList<FirebasePlannedWorkout>).forEach { element ->
                    if (element.userId == userId) {
                        userData.add(element)
                    }
                }
                completion(userData)
            }
            .addOnFailureListener { exception ->
                Log.w("USER_PlannedWorkouts_FETCHING", "Error getting documents: ", exception)
                completion(ArrayList())
            }
    }

    fun fetchBaseData(requestedDataType: UserDataType, sortByField: String? = null, sortOrder: Query.Direction = Query.Direction.ASCENDING, completion: (result: ArrayList<Any>) -> Unit) {
        val dbCollectionToQuery = getCollectionForRequestedType(requestedDataType)
        if (sortByField != null) {
            db.collection(dbCollectionToQuery)
                .orderBy(sortByField, Query.Direction.ASCENDING)
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
        } else {
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
    }

    fun fetchCustomDocument(dataType: UserDataType, documentId: String, completion: (result: Any) -> Unit) {
        val dbCollectionToQuery = getCollectionForRequestedType(dataType)
        db.document(documentId)
            .get()
            .addOnSuccessListener { document ->
                val data = getProcessedFetchedDataDocument(document, dataType)
                Log.w("DATA_FETCHING", "Successfully fetched documents: ${dataType.name}")
                if (data != null) {
                    completion(data)
                }
            }
            .addOnFailureListener { exception ->
                Log.w("DATA_FETCHING", "Error getting documents: ", exception)
                completion(Exercise())
            }
    }

    fun saveNewDocument(document: MyDocument, collectionType: UserDataType, completion: (result: FirebaseRequestResult) -> Unit) {
        val dbCollectionForNewDocument = getCollectionForRequestedType(collectionType)
        val docReference = db.collection(dbCollectionForNewDocument).document()
        document.docReference = docReference.path
        document.userId = FirebaseAuth.getInstance().currentUser?.uid
        docReference.set(document)
            .addOnCompleteListener {
                task ->
                Log.w("DATA_ADDING_COMPLETED", "New document in: ${collectionType.name}")
                completion(FirebaseRequestResult.COMPLETED)
            }
            .addOnSuccessListener {
                Log.w("DATA_ADDING_SUCCESS", "Successfully added new document: ${collectionType.name}")
                completion(FirebaseRequestResult.SUCCESS)
            }
            .addOnFailureListener { e ->
                Log.w("DATA_ADDING_FAILURE", "Error writing document", e)
                completion(FirebaseRequestResult.FAILURE)
            }
    }

    fun savePlannedWorkouts(workoutsToSaveAsPlanned: ArrayList<FirebasePlannedWorkout>, completion: (result: FirebaseRequestResult) -> Unit) {
        val collectionToWrite = getCollectionForRequestedType(UserDataType.PLANNED_WORKOUT)
        var newReference: DocumentReference
        db.runBatch {
            writeBatch ->
            for (workout in workoutsToSaveAsPlanned) {
                newReference = db.collection(collectionToWrite).document()
                workout.userId = currentUser?.uid
                workout.docReference = newReference.path
                writeBatch.set(newReference, workout)
            }
        }
        .addOnCompleteListener { task ->
            Log.w("DATA_ADDING_COMPLETED", "New document in: $collectionToWrite")
        }
        .addOnSuccessListener {
            Log.w("DATA_ADDING_SUCCESS", "Successfully added new document: $collectionToWrite")
            completion(FirebaseRequestResult.SUCCESS)
        }
        .addOnFailureListener { e ->
            Log.w("DATA_ADDING_FAILURE", "Error writing document", e)
            completion(FirebaseRequestResult.FAILURE)
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
                    val element = document.toObject(FirebasePlannedWorkout::class.java)
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
                return document.toObject(FirebaseWorkoutElement::class.java)
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
            UserDataType.WORKOUT -> "Workout"
            UserDataType.PLANNED_WORKOUT -> "PlannedWorkout"
            UserDataType.WORKOUT_ELEMENT -> "WorkoutElement"
            UserDataType.EXERCISE -> "Exercise"
            UserDataType.CATEGORY -> "Category"
            UserDataType.EQUIPMENT -> "Equipment"
            UserDataType.USER_INFO -> "User"
        }
    }

    fun getQueryForFetching(userDataType: UserDataType): CollectionReference {
        val collectionToQuery = getCollectionForRequestedType(userDataType)
        return db.collection(collectionToQuery)
    }

    fun updateDocument(documentToUpdate: MyDocument, dataType: UserDataType, completion: (result: FirebaseRequestResult) -> Unit) {
        documentToUpdate.docReference?.let {
            db.document(it)
                .set(documentToUpdate)
                .addOnCompleteListener {
                        task ->
                    Log.w("DATA_EDIT", "Updating document in: ${dataType.name}")
                    completion(FirebaseRequestResult.COMPLETED)
                }
                .addOnSuccessListener {
                    Log.w("DATA_EDIT", "Successfully updated document: ${dataType.name}")
                    completion(FirebaseRequestResult.SUCCESS)
                }
                .addOnFailureListener { e ->
                    Log.w("DATA_EDIT", "Error updating document", e)
                    completion(FirebaseRequestResult.FAILURE)
                }
        }
    }

    fun removeDocument(documentToRemove: MyDocument, dataType: UserDataType, completion: (result: FirebaseRequestResult) -> Unit) {
        documentToRemove.docReference?.let {
            db.document(it)
                .delete()
                .addOnCompleteListener {
                        task ->
                    Log.w("DATA_DELETE", "Updating document in: ${dataType.name}")
                    completion(FirebaseRequestResult.COMPLETED)
                }
                .addOnSuccessListener {
                    Log.w("DATA_DELETE", "Successfully updated document: ${dataType.name}")
                    completion(FirebaseRequestResult.SUCCESS)
                }
                .addOnFailureListener { e ->
                    Log.w("DATA_DELETE", "Error updating document", e)
                    completion(FirebaseRequestResult.FAILURE)
                }
        }
    }

    fun setUserInfo(name: String, completion: (result: FirebaseRequestResult) -> Unit) {
        val user = FirebaseAuth.getInstance().currentUser
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(name)
            .build()

        user?.updateProfile(profileUpdates)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    completion(FirebaseRequestResult.SUCCESS)
                    Log.d("USER INFO UPDATE", "User profile updated.")
                }
                else {
                    completion(FirebaseRequestResult.FAILURE)
                }
            }
    }
}