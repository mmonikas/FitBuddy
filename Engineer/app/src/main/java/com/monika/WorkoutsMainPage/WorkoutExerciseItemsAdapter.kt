package com.monika.WorkoutsMainPage

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.monika.Model.WorkoutComponents.WorkoutElement
import com.monika.R
import com.monika.Services.Utils
import kotlinx.android.synthetic.main.workoutcard_exercise_item.view.*


import kotlin.collections.ArrayList

class WorkoutElementItemsAdapter(private val workoutElementItems: ArrayList<WorkoutElement>):
    RecyclerView.Adapter<WorkoutElementItemsAdapter.ItemsViewHolder>() {

    class ItemsViewHolder(cardView: CardView): RecyclerView.ViewHolder(cardView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        val cardView = LayoutInflater.from(parent.context).inflate(R.layout.workoutcard_exercise_item, parent, false) as CardView
        return ItemsViewHolder(cardView)
    }

    override fun getItemCount(): Int {
        return workoutElementItems.size
    }

    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
        val item = workoutElementItems[position]
        holder.itemView.workoutExerciseItemName.text = item.exercise?.name
        holder.itemView.workoutExerciseRepsNum.text = item.numOfReps.toString()
        holder.itemView.workoutExerciseSetsNum.text = item.numOfSets.toString()
        val category = item.exercise?.category
        setCategoryImage(category, holder.itemView.workoutExerciseCategoryImage)
    }

    private fun setCategoryImage(category: String?, imageView: ImageView) {
        category?.let {
            imageView.setImageResource(Utils.getCategoryImage(it)) }
    }
}