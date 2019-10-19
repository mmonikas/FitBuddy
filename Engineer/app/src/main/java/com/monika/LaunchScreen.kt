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
import com.monika.WorkoutsMainPage.LaunchScreenPresenter
import kotlinx.android.synthetic.main.fragment_login.*

class LaunchScreen : Fragment() {

    private val presenter = LaunchScreenPresenter()

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
            presenter.fetchUserWorkouts {
                workoutListResult ->
                if (workoutListResult.isNotEmpty()) {
                    val bundle = bundleOf("workouts" to workoutListResult)
                    Navigation.findNavController(view!!).navigate(R.id.homeFragment, bundle, null)
                }
                else {
                    //loginFragment_progress.visibility = View.GONE
                    //TODO zrob to cos bo nie dziala obviously
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.hide()
    }
}
