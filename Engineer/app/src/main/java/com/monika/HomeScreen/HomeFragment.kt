package com.monika.HomeScreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.navigation.Navigation
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.monika.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {

    private val TAG: String = "dbTag"
    private lateinit var calendarDaysCollectionPagerAdapter: HomeFragmentPagerAdapter
    private lateinit var viewPager: ViewPager

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
            Navigation.findNavController(view!!).navigate(R.id.loginFragment)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayout()
        setTabLayout()
        FirebaseAuth.getInstance().addAuthStateListener {
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null ) {
                viewPager.adapter = HomeFragmentPagerAdapter(childFragmentManager)
            }
        }
    }

    private fun setLayout() {
        (activity as MainActivity).supportActionBar?.show()
    }

    private fun setTabLayout() {
        val tabLayout = view?.findViewById<TabLayout>(R.id.home_tab_layout)
        tabLayout?.setupWithViewPager(home_pager)
//        tabLayout?.minimumWidth =
        calendarDaysCollectionPagerAdapter = HomeFragmentPagerAdapter(childFragmentManager)
        viewPager = view?.findViewById(R.id.home_pager)!!
        viewPager.adapter = calendarDaysCollectionPagerAdapter

    }


}
