package com.monika.HomeScreen


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.monika.Enums.MenuItemType
import com.monika.Enums.MenuItemType.*
import com.monika.HomeScreen.MainActivity.MainActivity
import com.monika.Model.Home.HomeMenuItem
import com.monika.R
import com.monika.SignInAndRegister.LoginFragmentPresenter
import kotlinx.android.synthetic.main.fragment_home_grid.*

interface HomeFragmentDelegate {
    fun gridMenuItemTriggered(atIndex: Int, withId: MenuItemType)
}

class HomeGridFragment : Fragment(), HomeFragmentDelegate {

    val presenter = LoginFragmentPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_grid, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.hide()
        setMenuItems()
    }

    override fun onStart() {
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser == null) {
            findNavController().navigate(R.id.loginFragment)
        }
//        } else {
//            presenter.fetchUserWorkouts {
//                    workoutListResult ->
//                if (workoutListResult.isNotEmpty()) {
//                    presenter.workouts = workoutListResult
//                    homeFragment_progress.visibility = View.GONE
////                    val bundle = bundleOf("workouts" to workoutListResult)
////                    Navigation.findNavController(view!!).navigate(R.id.homeFragment, bundle, null)
//                }
//                else {
//                    //loginFragment_progress.visibility = View.GONE
//                    //TODO zrob to cos bo nie dziala obviously
//                }
//            }
//        }
    }

    override fun gridMenuItemTriggered(atIndex: Int, withId: MenuItemType) {
        (activity as MainActivity).showProgressView()
        when (withId) {
            kalendarz -> fetchDataAndOpenCalendarFragment()
            treningi -> fetchDataAndOpenWorkoutsFragment()
            ćwiczenia -> fetchDataAndOpenExercisesFragment()
            przyjaciele -> {}
            profil -> {}
            ustawienia -> {}
        }
    }

    private fun fetchDataAndOpenCalendarFragment() {
        presenter.fetchBaseExercises { result ->
            if (result.isNotEmpty()) {
                presenter.exercises = result
                val bundle = bundleOf("workoutElements" to result)
                (activity as MainActivity).showToolbar()
                Navigation.findNavController(view!!).navigate(R.id.homeFragment, bundle, null)
            }
        }
    }

    private fun fetchDataAndOpenWorkoutsFragment() {
        presenter.fetchUserWorkouts { result ->
            if (result.isNotEmpty()) {
                //presenter.workouts = result
                val bundle = bundleOf("workouts" to presenter.workouts)
                (activity as MainActivity).showToolbar()
                Navigation.findNavController(view!!).navigate(R.id.workoutsList, bundle, null)
            }
        }
    }

    private fun fetchDataAndOpenExercisesFragment() {
        presenter.fetchBaseExercises { result ->
            if (result.isNotEmpty()) {
                presenter.exercises = result
                val bundle = bundleOf("workoutElements" to result)
                (activity as MainActivity).showToolbar()
                Navigation.findNavController(view!!).navigate(R.id.exercisesListFragment, bundle, null)
            }
        }
    }

    private fun setMenuItems() {
        val item0 = HomeMenuItem(id = 0, name = kalendarz)
        val item1 = HomeMenuItem(id = 1, name = treningi)
        val item2 = HomeMenuItem(id = 2, name = ćwiczenia)
        val item3 = HomeMenuItem(id = 3, name = przyjaciele)
        val item4 = HomeMenuItem(id = 4, name = profil)
        val item5 = HomeMenuItem(id = 5, name = ustawienia)
        val items = arrayOf(item0, item1, item2, item3, item4, item5)
        val homeAdapter = HomeGridAdapter(menuItems = items)
        homeAdapter.homeFragmentDelegate = this
        home_recyclerView.adapter = homeAdapter
        home_recyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
        }
        (activity as MainActivity).hideProgressView()
    }

}
