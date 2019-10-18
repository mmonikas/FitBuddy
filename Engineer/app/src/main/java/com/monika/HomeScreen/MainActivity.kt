package com.monika.HomeScreen

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.monika.Enums.FirebaseRequestResult
import com.monika.Model.WorkoutPlan.Workout
import com.monika.R
import kotlinx.android.synthetic.main.fragment_home.*


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
        Log.w("UserID", user?.uid.toString())
    }

    private fun setUpNavigation() {

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(getColor(R.color.primaryTextColor))
        supportActionBar!!.title = getString(R.string.nothing)

        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
      //  supportActionBar!!.setDisplayShowHomeEnabled(false)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigationView)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        NavigationUI.setupWithNavController(navigationView, navController)

        navigationView.setNavigationItemSelectedListener(this)

    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

    override fun onBackPressed() {

        if(backPressedOnce) {
            System.exit(0)
        }
        else if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        Toast.makeText(this, R.string.closeAgainToExit, Toast.LENGTH_SHORT).show()
        backPressedOnce = true

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
             //   setUpDatabase()
                false
            }
            R.id.action_logOut -> {
                presenter.logOutUser()
                navController.navigate(R.id.loginFragment)
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

//    private fun fetchData(completion: (result: ArrayList<Workout>) -> Unit) {
//        val db = FirebaseFirestore.getInstance()
//        // Create a new user with a first and last name
//        val userId = FirebaseAuth.getInstance().currentUser?.uid
//        val TAG = "itemtag"
//        db.collection("Workouts")
//            .whereEqualTo("userID", true)
//            .get()
//            .addOnSuccessListener { documents ->
//                val workoutsList = ArrayList<Workout>()
//                for (document in documents) {
//                    val workout = document.toObject(Workout::class.java)
//                    Log.w(TAG, "Error getting documents: ", document.data.getValue("name") as Throwable?)
//                    workoutsList.add(workout)
//                }
//                completion(workoutsList)
//            }
//            .addOnFailureListener { exception ->
//                Log.w(TAG, "Error getting documents: ", exception)
//                homeFragment_progress.visibility = View.GONE
//                completion(ArrayList())
//            }
//    }

    private fun setUpDatabase() {
        val db = FirebaseFirestore.getInstance()
        // Create a new user with a first and last name
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        val workout = hashMapOf(
            "name" to "superButtSquat",
            "userID" to userId
        )

        // Add a new document with a generated ID
        db.collection("Workouts")
            .add(workout)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "Workout added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }

        // Create a new user with a first, middle, and last name
//        val user2 = hashMapOf(
//            "first" to "Alan",
//            "middle" to "Mathison",
//            "last" to "Turing",
//            "born" to 1912
//        )
//
//        // Add a new document with a generated ID
//        db.collection("Users")
//            .add(user2)
//            .addOnSuccessListener { documentReference ->
//                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
//            }
//            .addOnFailureListener { e ->
//                Log.w(TAG, "Error adding document", e)
//            }


    }
}
