package com.monika

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.monika.MainActivity.MainActivity
import com.monika.WorkoutsMainPage.LaunchScreenPresenter

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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.hide()
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
}
