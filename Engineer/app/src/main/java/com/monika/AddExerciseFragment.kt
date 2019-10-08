//package com.monika
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.AdapterView
//import androidx.fragment.app.Fragment
//import kotlinx.android.synthetic.main.fragment_add_exercise.*
//import java.util.ArrayList
//import android.widget.ArrayAdapter as ArrayAdapter1
//
//class AddExerciseFragment : Fragment(), AdapterView.OnItemSelectedListener {
//
//    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun onNothingSelected(parent: AdapterView<*>?) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    //private lateinit var view: View
//    private lateinit var categoryItems: ArrayList<String>
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//
//        view = inflater.inflate(R.layout.fragment_add_exercise, container, false)
//
//        categoryItems = ArrayList()
//        for (i in 0 until Category.values().size) {
//            categoryItems.add(Category.values()[i].toString())
//        }
//
//        spinner_categories.minimumHeight = 28
////        val adapter = android.widget.ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryItems)
////        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
////        spinner_categories.adapter = adapter
//
//
//
//        return view
//    }
//}
