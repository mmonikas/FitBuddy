package com.monika.HomeScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.monika.R
import kotlinx.android.synthetic.main.fragment_collection_calendar_day.*
import kotlinx.android.synthetic.main.nav_header_main.*


class CalendarDayFragment : Fragment() {

    val ARG_OBJECT = "object"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                               savedInstanceState: Bundle? ): View? {
        // Inflate the layout for this fragment
        // setUpDatabase()
        return inflater.inflate(R.layout.fragment_collection_calendar_day, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
//            val textView: TextView = view.findViewById(R.id.calendar_dayName)
//            textView.text = getInt(ARG_OBJECT).toString()
//        }
        arguments?.takeIf { it.containsKey("exerciseName") }?.apply {
            calendar_dayName.text = getString("exerciseName")
        }


    }
}