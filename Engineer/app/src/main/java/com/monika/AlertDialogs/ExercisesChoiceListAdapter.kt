package com.monika.ExercisesPage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.monika.Model.WorkoutComponents.Exercise
import com.monika.R
import com.monika.Services.Utils
import kotlinx.android.synthetic.main.category_list_item.view.*

class ExercisesChoiceListAdapter(private val context: Context, private val exercises: ArrayList<Exercise>,
                                 private val listener: ExerciseSelectionListener
): BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.category_list_item, parent, false)
        val exercise = exercises[position]
        view.name.text = exercise.name
        val categoryName = exercise.category
        categoryName?.let {
            view.image.setImageResource(Utils.getCategoryImage(categoryName)) }
        view.setOnClickListener {
            listener.onClickCallback(exercise)
        }
        return view
    }

    override fun getItem(position: Int): Any {
        return exercises[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return exercises.size
    }
}