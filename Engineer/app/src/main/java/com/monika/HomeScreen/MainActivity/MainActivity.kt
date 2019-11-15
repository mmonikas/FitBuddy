package com.monika.HomeScreen.MainActivity

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
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

       // createUser()





    }

    override fun onStart() {
        super.onStart()
        val user = FirebaseAuth.getInstance().currentUser
        user?.let { Log.w("UserID", user?.uid.toString()) }
    }

    fun setUpNavigation() {


//        toolbar = findViewById(R.id.toolbar)
//        setSupportActionBar(toolbar)
//        toolbar.setTitleTextColor(getColor(R.color.primaryTextColor))
//        supportActionBar?.title = getString(R.string.nothing)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
      //  supportActionBar!!.setDisplayShowHomeEnabled(false)

//        drawerLayout = findViewById(R.id.drawer_layout)
//        navigationView = findViewById(R.id.navigationView)
//        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
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
//        NavigationUI.setupActionBarWithNavController(this, navController)
//        NavigationUI.setupWithNavController(navigationView, navController)
//
//        navigationView.setNavigationItemSelectedListener(this)
//        hideBottomNavigation()

    }

//    fun setUpNavigation() {
//        bottomNavigationView = findViewById<View>(R.id.bttm_nav)
//        val navHostFragment = supportFragmentManager
//            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
//        NavigationUI.setupWithNavController(
//            bottomNavigationView,
//            navHostFragment!!.navController
//        )
//    }

//    override fun onSupportNavigateUp(): Boolean {
//        return NavigationUI.navigateUp(navController, drawerLayout) || super.onSupportNavigateUp()
//    }

    override fun onBackPressed() {
        super.onBackPressed()
//        if (main_progressView.visibility == View.VISIBLE) {
//            hideProgressView()
//        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.action_logOut -> {
//                presenter.logOutUser()
//               // navController.popBackStack(R.id.homeFragment, true)
//                val navBuilder = NavOptions.Builder()
//                val navOptions = navBuilder.setEnterAnim(R.anim.slide_out_top).setPopUpTo(R.id.nav_graph, true)
//                navController.navigate(R.id.loginFragment, null, navOptions.build(), null)
//                false
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        // Handle navigation view item clicks here.
//
//        drawerLayout.closeDrawers()
//
//        when (item.itemId) {
//            R.id.nav_exercises -> {
//                navController.navigate(R.id.exercisesListFragment)
//            }
//            R.id.nav_add_exercise -> {
//               // navController.navigate(R.id.addExerciseFragment)
//            }
//            R.id.nav_share -> {
//
//            }
//        }
//
//        drawerLayout.closeDrawer(GravityCompat.START)
//
//        return false
//    }

    fun showBottomNavigation() {
        bottomNavigationView?.visibility = View.VISIBLE
    }

    fun hideBottomNavigation() {
        bottomNavigationView?.visibility = View.GONE
    }

    fun showProgressView() {
        findViewById<LinearLayout>(R.id.main_progressView).visibility = View.VISIBLE
    }

    fun hideProgressView() {
        findViewById<LinearLayout>(R.id.main_progressView).visibility = View.GONE
    }


}
