//package com.monika.HomeScreen
//
//
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.core.os.bundleOf
//import androidx.navigation.NavOptions
//import androidx.navigation.Navigation
//import androidx.navigation.fragment.findNavController
//import androidx.recyclerview.widget.GridLayoutManager
//import com.google.firebase.auth.FirebaseAuth
//import com.monika.Enums.MenuItemType
//import com.monika.Enums.MenuItemType.*
//import com.monika.MainActivity.MainActivity
//import com.monika.Model.Home.HomeMenuItem
//import com.monika.R
//import com.monika.SignInAndRegister.LoginFragmentPresenter
//import kotlinx.android.synthetic.main.fragment_home_grid.*
//
//interface HomeFragmentDelegate {
//    fun gridMenuItemTriggered(atIndex: Int, withId: MenuItemType)
//}
//
//class HomeGridFragment : Fragment(), HomeFragmentDelegate {
//
//    val presenter = LoginFragmentPresenter()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//        }
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home_grid, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        (activity as MainActivity).supportActionBar?.hide()
//        setMenuItems()
//    }
//
//    override fun onStart() {
//        super.onStart()
//        if (FirebaseAuth.getInstance().currentUser == null) {
//            val navBuilder = NavOptions.Builder()
//            val navOptions = navBuilder.setEnterAnim(R.anim.slide_out_top).setPopUpTo(R.id.nav_graph, true)
//            findNavController().navigate(R.id.loginFragment, null, navOptions.build(), null)
////            findNavController().navigate(R.id.loginFragment)
//        }
//        (activity as MainActivity).supportActionBar?.show()
//    }
//
//
//    private fun fetchDataAndOpenCalendarFragment() {
//        (activity as MainActivity).showBottomNavigation()
//        Navigation.findNavController(view!!).navigate(R.id.homeFragment)
////        presenter.fetchBaseExercises { result ->
////            if (result.isNotEmpty()) {
////                presenter.exercises = result
////                val bundle = bundleOf("workoutElements" to result)
////                (activity as MainActivity).showBottomNavigation()
////                Navigation.findNavController(view!!).navigate(R.id.homeFragment, bundle, null)
////            }
////        }
//    }
//
//
//    private fun fetchDataAndOpenExercisesFragment() {
//        (activity as MainActivity).showProgressView()
//        (activity as MainActivity).showBottomNavigation()
//        Navigation.findNavController(view!!).navigate(R.id.exercisesListFragment, null)
//
////        presenter.fetchBaseExercises { result ->
////            if (result.isNotEmpty()) {
////                presenter.exercises = result
////                val bundle = bundleOf("workoutElements" to result)
////                (activity as MainActivity).showBottomNavigation()
////                Navigation.findNavController(view!!).navigate(R.id.exercisesListFragment, bundle, null)
////            }
////        }
//    }
//
//    private fun setMenuItems() {
//        val item0 = HomeMenuItem(id = 0, name = kalendarz)
//        val item1 = HomeMenuItem(id = 1, name = treningi)
//        val item2 = HomeMenuItem(id = 2, name = ćwiczenia)
//        val item3 = HomeMenuItem(id = 3, name = przyjaciele)
//        val item4 = HomeMenuItem(id = 4, name = profil)
//        val item5 = HomeMenuItem(id = 5, name = ustawienia)
//        val items = arrayOf(item0, item1, item2, item3, item4, item5)
//        val homeAdapter = HomeGridAdapter(menuItems = items)
//        homeAdapter.homeFragmentDelegate = this
//        home_recyclerView.adapter = homeAdapter
//        home_recyclerView.apply {
//            layoutManager = GridLayoutManager(context, 2)
//        }
//        (activity as MainActivity).hideProgressView()
//    }
//
//}
