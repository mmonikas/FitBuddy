package com.monika.ExercisesMainPage

import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.monika.Enums.FirebaseRequestResult
import com.monika.Enums.UserDataType
import com.monika.Model.WorkoutComponents.Exercise
import com.monika.Services.DatabaseService

class ExercisesListPresenter {

    var exercises = ArrayList<Exercise>()

    fun getOptionsForUpdatesListener(): FirestoreRecyclerOptions<Exercise>? {
        val user = FirebaseAuth.getInstance().currentUser
        return if (user != null) {
            val query : Query = DatabaseService.instance.getQueryForFetching(UserDataType.EXERCISE)
                .whereEqualTo("userId", user.uid)
                .orderBy("name", Query.Direction.ASCENDING)
            FirestoreRecyclerOptions.Builder<Exercise>()
                .setQuery(query, Exercise::class.java)
                .build()
        } else null
    }

    fun getOptionsForBaseDataListener(): FirestoreRecyclerOptions<Exercise> {
        val query : Query = DatabaseService.instance.getQueryForFetching(UserDataType.EXERCISE)
            .whereEqualTo("userId", null)
            .orderBy("name", Query.Direction.ASCENDING)
        return FirestoreRecyclerOptions.Builder<Exercise>()
            .setQuery(query, Exercise::class.java)
            .build()
    }

    fun removeItemAt(position: Int, completion: (result: FirebaseRequestResult) -> Unit) {
        val item = exercises[position]
        DatabaseService.instance.removeDocument(item, UserDataType.EXERCISE) {
            result ->
            if (result == FirebaseRequestResult.SUCCESS) {
                exercises.removeAt(position)
            }
            completion(result)
        }
    }

    fun fetchExercises (completion: (result: ArrayList<Exercise>) -> Unit) {
        DatabaseService.instance.fetchUserData(UserDataType.EXERCISE) {
            result ->
            if(result.isNotEmpty()) {
                val exercisesList = result as ArrayList<Exercise>
                exercisesList.sortBy { exercise -> exercise.name }
                completion(exercisesList)
            }
        }
    }

    fun checkIfExerciseCanBeDeleted(position: Int, completion: (result: Boolean?) -> Unit) {
        val exerciseId = exercises[position].docReference
        if (exerciseId != null) {
            DatabaseService.instance.checkIfExistsInAnyWorkout(exerciseDocReference = exerciseId) {
                    existsSomewhere ->
                completion(existsSomewhere)
            }
        }
        else {
            completion(null)
        }
    }


}
