package com.monika.WorkoutsMainPage

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.monika.Model.WorkoutComponents.WorkoutElement
import com.monika.R
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
        when (category) {
            "Biceps" -> imageView.setImageResource(R.drawable.icons8biceps100)
            "Barki" -> imageView.setImageResource(R.drawable.icons8shoulders100)
            "Plecy" -> imageView.setImageResource(R.drawable.icons8torso100)
            "Nogi" -> imageView.setImageResource(R.drawable.icons8leg100)
            "Pośladki" -> imageView.setImageResource(R.drawable.icons8glutes100)
            "Brzuch" -> imageView.setImageResource(R.drawable.icons8prelum100)
            "Triceps" -> imageView.setImageResource(R.drawable.icons8triceps100)
            "Klatka piersiowa" -> imageView.setImageResource(R.drawable.icons8chest100)
            else -> imageView.setImageResource(R.drawable.icons8weightlifting100)
        }
    }
}