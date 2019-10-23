package com.monika.HomeScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.monika.Model.WorkoutComponents.Exercise
import com.monika.R
import com.monika.SignInAndRegister.LoginFragmentPresenter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.lang.Exception


class HomeFragment : Fragment() {

    val presenter = LoginFragmentPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            val exercises = arguments?.get("exercises") as ArrayList<Exercise>
            presenter.exercises = exercises
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       // setUpDatabase()
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onStart() {
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser == null) {
            findNavController().navigate(R.id.loginFragment)
        } else {
            if (arguments == null) {
                presenter.fetchUserExercises { result ->
                    if (result.isNotEmpty()) {
                        presenter.exercises = result
                        setTabLayout()
//                    val bundle = bundleOf("workouts" to workoutListResult)
//                    Navigation.findNavController(view!!).navigate(R.id.homeFragment, bundle, null)
                    } else {
                        //loginFragment_progress.visibility = View.GONE
                        //TODO zrob to cos bo nie dziala obviously
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayout()
        if (presenter.exercises.isNotEmpty()) {
            setTabLayout()
        }
//        if (FirebaseAuth.getInstance().currentUser != null) {
//            setTabLayout()
//        }
//        if (FirebaseAuth.getInstance().currentUser == null) {
//            Navigation.findNavController(view!!).navigate(R.id.loginFragment)
//        }

    }

    private fun setLayout() {
        (activity as MainActivity).supportActionBar?.show()
    }

    private fun setTabLayout() {
        val tabLayout = view?.findViewById<TabLayout>(R.id.home_tab_layout)
        tabLayout?.setupWithViewPager(home_pager)
//        tabLayout?.minimumWidth =
//        calendarDaysCollectionPagerAdapter = HomeFragmentPagerAdapter(childFragmentManager)
        val adapter = HomeFragmentPagerAdapter(childFragmentManager)
        adapter.exercises = presenter.exercises
        home_pager.adapter = adapter
        (activity as MainActivity).hideProgressView()
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).supportActionBar?.hide()
    }
}

