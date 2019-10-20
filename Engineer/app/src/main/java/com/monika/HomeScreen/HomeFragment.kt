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
import com.monika.Model.WorkoutPlan.Workout
import com.monika.R
import com.monika.SignInAndRegister.LoginFragmentPresenter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {

    val presenter = LoginFragmentPresenter()
    private val TAG: String = "dbTag"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
         //   workouts = arguments?.get("workouts") as ArrayList<Workout>
            val workouts = arguments?.get("workouts") as ArrayList<Workout>
            presenter.workouts = workouts
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
            presenter.fetchUserWorkouts {
                    workoutListResult ->
                if (workoutListResult.isNotEmpty()) {
                    presenter.workouts = workoutListResult
                    setTabLayout()
                    homeFragment_progress.visibility = View.GONE
//                    val bundle = bundleOf("workouts" to workoutListResult)
//                    Navigation.findNavController(view!!).navigate(R.id.homeFragment, bundle, null)
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
        setLayout()
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
        adapter.workouts = presenter.workouts
        home_pager.adapter = adapter


    }
}
