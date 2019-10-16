package com.monika.HomeScreen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.monika.Model.WorkoutPlan.Workout


class HomeFragmentPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {


    val ARG_OBJECT = "object"

    override fun getCount(): Int {
        return 10
    }

    override fun getItem(position: Int): Fragment {
        val fragment = CalendarDayFragment()
        val db = FirebaseFirestore.getInstance()
        // Create a new user with a first and last name
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val TAG = "itemtag"
//        db.collection("Workouts")
//            .get()
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    for (document in task.result!!) {
//                        Log.d(TAG, document.id + " => " + document.data)
//                    }
//                } else {
//                    Log.w(TAG, "Error getting documents.", task.exception)
//                }
//            }
        db.collection("Workouts")
            .whereEqualTo("userID", true)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val workout = document.toObject(Workout::class.java)
                    Log.d(TAG, "${document.id} => ${document.data}")
                    fragment.arguments = Bundle().apply {
                        // Our object is just an integer :-P
                        putInt(ARG_OBJECT, position + 1)
                        putString("exerciseName", workout.name)
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }


        return fragment
    }

    override fun getPageTitle(position: Int): CharSequence {
        return "OBJECT ${(position + 1)}"
    }
//    override fun getCount(): Int  = 4
//
//    override fun getPageTitle(position: Int): CharSequence {
//        return "OBJECT ${(position + 1)}"
//    }
}

