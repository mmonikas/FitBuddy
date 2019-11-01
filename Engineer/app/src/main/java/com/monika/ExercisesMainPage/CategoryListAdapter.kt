package com.monika.ExercisesMainPage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.monika.Model.WorkoutComponents.Category
import com.monika.R
import com.monika.Services.Utils
import kotlinx.android.synthetic.main.category_list_item.view.*

class CategoryListAdapter(private val context: Context, private val categories: ArrayList<Category>,
                          private val listener: SelectionListener): BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.category_list_item, parent, false)
        val category = categories[position]
        view.name.text = category.name
        val categoryName = category.name
        categoryName?.let {
            view.image.setImageResource(Utils.getCategoryImage(categoryName)) }
        view.setOnClickListener {
            listener.onClickCallback(category)
        }
        return view
    }

    override fun getItem(position: Int): Any {
        return categories[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return categories.size
    }
}