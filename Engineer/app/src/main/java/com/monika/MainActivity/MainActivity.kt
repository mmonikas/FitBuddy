package com.monika.MainActivity

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.monika.Model.WorkoutComponents.Exercise
import com.monika.R


class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var navController: NavController
    public val TAG: String = "dbTag"
    private val mAuth = FirebaseAuth.getInstance()
    private val presenter = MainActivityPresenter()
    private var backPressedOnce: Boolean = false
    private lateinit var bottomNavigationView: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        setUpNavigation()
        fetchAllBaseData()

       // createUser()





    }

    private fun fetchAllBaseData() {
      //  showProgressView()
        presenter.getAllCategories { }
        presenter.getAllEquipment {  }

    }

    override fun onStart() {
        super.onStart()
        val user = FirebaseAuth.getInstance().currentUser
        user?.let { Log.w("UserID", it.uid) }
    }

    private fun setUpNavigation() {

        bottomNavigationView = findViewById(R.id.bttm_nav)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        navController = navHostFragment!!.navController
            NavigationUI.setupWithNavController(
            bottomNavigationView,
            navController)
//        )
//
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.registerFragment -> hideBottomNavigation()
                R.id.loginFragment -> hideBottomNavigation()
                else -> showBottomNavigation()
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    fun showBottomNavigation() {
        bottomNavigationView?.visibility = View.VISIBLE
    }

    fun hideBottomNavigation() {
        bottomNavigationView?.visibility = View.GONE
    }

    fun enableBottomNavigation() {
        bottomNavigationView.isEnabled = true
    }

    fun disableBottomNavigation() {
        bottomNavigationView.isEnabled = false
    }

    fun showProgressView() {
        findViewById<LinearLayout>(R.id.main_progressView).visibility = View.VISIBLE
    }

    fun hideProgressView() {
        findViewById<LinearLayout>(R.id.main_progressView).visibility = View.GONE
    }

//    fun getExercises() : ArrayList<Exercise> {
//        if (presenter.exercisesList.isEmpty()) {
//            presenter.getAllExercises {
//                //
//            }
//        }
//        return presenter.exercisesList
//    }
//
//    fun setExercises(exercises: ArrayList<Exercise>) {
//        presenter.exercisesList = exercises
//    }
}
