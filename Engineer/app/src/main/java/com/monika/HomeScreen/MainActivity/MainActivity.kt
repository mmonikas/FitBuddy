package com.monika.HomeScreen.MainActivity

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.monika.AlertDialogs.CategoryChoiceDialog
import com.monika.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var navController: NavController
    public val TAG: String = "dbTag"
    private val mAuth = FirebaseAuth.getInstance()
    private val presenter = MainActivityPresenter()
    private var backPressedOnce: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
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


        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(getColor(R.color.primaryTextColor))
        supportActionBar?.title = getString(R.string.nothing)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
      //  supportActionBar!!.setDisplayShowHomeEnabled(false)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigationView)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        NavigationUI.setupWithNavController(navigationView, navController)

        navigationView.setNavigationItemSelectedListener(this)
        hideToolbar()

    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, drawerLayout) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (main_progressView.visibility == View.VISIBLE) {
            hideProgressView()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logOut -> {
                presenter.logOutUser()
               // navController.popBackStack(R.id.homeFragment, true)
                val navBuilder = NavOptions.Builder()
                val navOptions = navBuilder.setPopExitAnim(R.anim.slide_out_top).setPopUpTo(R.id.loginFragment, true)
                navController.popBackStack(R.id.loginFragment, true)
                navController.navigate(R.id.loginFragment, null, navOptions.build(), null)
                false
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.

        drawerLayout.closeDrawers()

        when (item.itemId) {
            R.id.nav_exercises -> {
                navController.navigate(R.id.exercisesListFragment)
            }
            R.id.nav_add_exercise -> {
               // navController.navigate(R.id.addExerciseFragment)
            }
            R.id.nav_share -> {

            }
        }

        drawerLayout.closeDrawer(GravityCompat.START)

        return false
    }

    fun showToolbar() {
        supportActionBar?.show()
    }

    fun hideToolbar() {
        supportActionBar?.hide()
    }

    fun showProgressView() {
        findViewById<LinearLayout>(R.id.main_progressView).visibility = View.VISIBLE
    }

    fun hideProgressView() {
        findViewById<LinearLayout>(R.id.main_progressView).visibility = View.GONE
    }


}
