package com.monika


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.monika.Enums.MenuItemType
import com.monika.Enums.MenuItemType.*
import com.monika.HomeScreen.HomeGridAdapter
import com.monika.Model.Home.HomeMenuItem
import com.monika.HomeScreen.MainActivity
import com.monika.SignInAndRegister.LoginFragmentPresenter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
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
            KALENDARZ -> {

            }
            PROFIL -> {

            }
            TRENINGI -> {}
            ĆWICZENIA -> {
                fetchDataAndOpenExercisesFragment()
            }
            PRZYJACIELE -> {}
            USTAWIENIA -> {}
        }
    }

    private fun setMenuItems() {
        val item0 = HomeMenuItem(id = 0, name = KALENDARZ)
        val item1 = HomeMenuItem(id = 1, name = TRENINGI)
        val item2 = HomeMenuItem(id = 2, name = ĆWICZENIA)
        val item3 = HomeMenuItem(id = 3, name = PRZYJACIELE)
        val item4 = HomeMenuItem(id = 4, name = PROFIL)
        val item5 = HomeMenuItem(id = 5, name = USTAWIENIA)
        val items = arrayOf(item0, item1, item2, item3, item4, item5)
        val homeAdapter = HomeGridAdapter(menuItems = items)
        homeAdapter.homeFragmentDelegate = this
        home_recyclerView.adapter = homeAdapter
        home_recyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
        }
    }

    private fun fetchDataAndOpenExercisesFragment() {
        presenter.fetchBaseExercises { result ->
            if (result.isNotEmpty()) {
                presenter.exercises = result
                val bundle = bundleOf("exercises" to result)
                Navigation.findNavController(view!!).navigate(R.id.homeFragment, bundle, null)
            }
        }
    }
}
