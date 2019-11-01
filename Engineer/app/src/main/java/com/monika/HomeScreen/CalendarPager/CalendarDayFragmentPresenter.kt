package com.monika.HomeScreen.CalendarPager

import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import com.monika.Enums.UserDataType
import com.monika.Model.WorkoutComponents.Exercise
import com.monika.Services.DatabaseService




class CalendarDayFragmentPresenter {

    fun getOptionsForDataFetching() : FirestoreRecyclerOptions<Exercise> {
        val query : Query = DatabaseService.instance.getQueryForFetching(UserDataType.EXERCISE)
        return FirestoreRecyclerOptions.Builder<Exercise>()
                .setQuery(query, Exercise::class.java)
                .build()
    }
}
