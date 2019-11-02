package com.monika.Services

import android.util.Log
import android.widget.Adapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthSettings
import com.google.firebase.auth.UserInfo
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.firestore.*
import com.monika.Enums.UserDataType
import com.monika.Model.WorkoutPlan.FirebaseWorkout
import com.monika.Model.WorkoutPlan.FirebaseWorkoutElement
import com.monika.Model.WorkoutPlan.PlannedWorkout
import com.google.firebase.firestore.FirebaseFirestore
import com.monika.Enums.FirebaseRequestResult
import com.monika.Model.WorkoutComponents.*
import com.monika.Model.WorkoutPlan.FirebasePlannedWorkout


class DatabaseService {

    private val db = FirebaseFirestore.getInstance()
    private val currentUser = FirebaseAuth.getInstance().currentUser

    companion object {
        val instance = DatabaseService()
    }

    fun fetchUserData(requestedDataType: UserDataType, userId: String, completion: (result: ArrayList<Any>) -> Unit) {
        val dbCollectionToQuery = getCollectionForRequestedType(requestedDataType)
        db.collection(dbCollectionToQuery)
//            .whereEqualTo("userId", userId)
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

    fun fetchCustomDocument(dataType: UserDataType, documentId: String, completion: (result: ArrayList<Any>) -> Unit) {
        val dbCollectionToQuery = getCollectionForRequestedType(dataType)
        db.collection(dbCollectionToQuery)
            .get()
            .addOnSuccessListener { documents ->
               // val document = documents.first { queryDocumentSnapshot -> queryDocumentSnapshot.id == documentId }
                val data = getProcessedFetchedDataArray(documents, dataType)
                val document = documents.first { queryDocumentSnapshot -> queryDocumentSnapshot.id == documentId }
                Log.w("DATA_FETCHING", "Successfully fetched documents: ${dataType.name}")
                completion(data)
            }
            .addOnFailureListener { exception ->
                Log.w("DATA_FETCHING", "Error getting documents: ", exception)
                completion(ArrayList())
            }
    }

    fun saveNewDocument(document: MyDocument, collectionType: UserDataType) {
        val dbCollectionForNewDocument = getCollectionForRequestedType(collectionType)
        val docReference = db.collection(dbCollectionForNewDocument).document()
        document.docReference = docReference.path
        document.userId = currentUser?.uid
        docReference.set(document)
            .addOnCompleteListener {
                task ->
                Log.w("DATA_ADDING_COMPLETED", "New document in: ${collectionType.name}")
            }
            .addOnSuccessListener {
                Log.w("DATA_ADDING_SUCCESS", "Successfully added new document: ${collectionType.name}")
            }
            .addOnFailureListener { e ->
                Log.w("DATA_ADDING_FAILURE", "Error writing document", e)
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

    fun updateDocument(documentToUpdate: MyDocument, dataType: UserDataType) {
        documentToUpdate.docReference?.let {
            db.document(it)
                .set(documentToUpdate)
                .addOnCompleteListener {
                        task ->
                    Log.w("DATA_EDIT", "Updating document in: ${dataType.name}")
                }
                .addOnSuccessListener {
                    Log.w("DATA_EDIT", "Successfully updated document: ${dataType.name}")
                }
                .addOnFailureListener { e ->
                    Log.w("DATA_EDIT", "Error updating document", e)
                }
        }
    }

    fun removeDocument(documentToRemove: MyDocument, dataType: UserDataType) {
        documentToRemove.docReference?.let {
            db.document(it)
                .delete()
                .addOnCompleteListener {
                        task ->
                    Log.w("DATA_DELETE", "Updating document in: ${dataType.name}")
                }
                .addOnSuccessListener {
                    Log.w("DATA_DELETE", "Successfully updated document: ${dataType.name}")
                }
                .addOnFailureListener { e ->
                    Log.w("DATA_DELETE", "Error updating document", e)
                }
        }
    }
}