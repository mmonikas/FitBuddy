package com.monika.ExercisesMainPage

import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import com.monika.Enums.UserDataType
import com.monika.Model.WorkoutComponents.Exercise
import com.monika.Services.DatabaseService

class ExercisesListPresenter {

    fun getOptionsForUpdatesListener(): FirestoreRecyclerOptions<Exercise> {
        val query : Query = DatabaseService.instance.getQueryForFetching(UserDataType.EXERCISE)
            .orderBy("name", Query.Direction.ASCENDING)
        return FirestoreRecyclerOptions.Builder<Exercise>()
            .setQuery(query, Exercise::class.java)
            .build()
    }

    fun removeItemAt(item: Exercise) {
        DatabaseService.instance.removeDocument(item, UserDataType.EXERCISE)
    }


}
