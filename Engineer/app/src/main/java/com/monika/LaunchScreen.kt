package com.monika

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.monika.HomeScreen.MainActivity
import com.monika.Model.WorkoutPlan.Workout
import kotlinx.android.synthetic.main.fragment_login.*

class LaunchScreen : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_launch_screen, container, false)
    }

    override fun onStart() {
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser == null) {
            Navigation.findNavController(view!!).navigate(R.id.loginFragment)
        } else {
            fetchData {
                workoutList ->
                val bundle = bundleOf("workouts" to workoutList)
                Navigation.findNavController(view!!).navigate(R.id.homeFragment, bundle, null)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.hide()
    }

    private fun fetchData(completion: (result: ArrayList<Workout>) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        // Create a new user with a first and last name
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val TAG = "itemtag"
        db.collection("Workouts")
            .whereEqualTo("userID", userId)
            .get()
            .addOnSuccessListener { documents ->
                val workoutsList = ArrayList<Workout>()
                for (document in documents) {
                    val workout = document.toObject(Workout::class.java)
                    workoutsList.add(workout)
                }
                completion(workoutsList)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
                loginFragment_progress.visibility = View.GONE
                completion(ArrayList())
            }
    }
}
