package com.monika


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_my_activity.*

class MyActivity : Fragment() {

    private val presenter = MyActivityPresenter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_my_activity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayout()
        setSeeHistoryListener()
        setLogOutListener()
    }

    private fun setLayout() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let { firebaseUser ->
            // Name, email address, and profile photo Url
            if (user.displayName != null) {
                val name = user.displayName
                name?.let { meLabel.text = it }
                val email = user.email
                email?.let { meLabel2.text = it }
            }
            else {
                for (profile in firebaseUser.providerData) {
                    val name = profile.displayName
                    name?.let { meLabel.text = it }
                    val email = profile.email
                    email?.let { meLabel2.text = it }
                }
            }
        }
    }

    private fun setLogOutListener() {
        logOutButton.setOnClickListener {
            logOutUser()
        }
    }

    private fun setSeeHistoryListener() {
        seeHistoryButton.setOnClickListener {
            findNavController().navigate(R.id.historyWorkouts)
        }
    }

    fun logOutUser() {
        FirebaseAuth.getInstance().signOut()
        val navBuilder = NavOptions.Builder()
        val navOptions = navBuilder.setEnterAnim(R.anim.slide_out_top).setPopUpTo(R.id.nav_graph, true)
        findNavController().navigate(R.id.loginFragment, null, navOptions.build(), null)
        false
    }
}
