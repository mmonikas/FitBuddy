package com.monika.WorkoutsMainPage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.monika.ExercisesMainPage.AddAnotherListener
import com.monika.Model.WorkoutComponents.WorkoutElement
import com.monika.R
import com.monika.Services.Utils
import kotlinx.android.synthetic.main.workout_cardview.view.*
import kotlinx.android.synthetic.main.workoutcard_exercise_item.view.*


import kotlin.collections.ArrayList

class WorkoutElementItemsAdapter(private val listener: AddAnotherListener?, private val isAddingMode: Boolean,
                                 private val workoutElementItems: ArrayList<WorkoutElement>):
    RecyclerView.Adapter<WorkoutElementItemsAdapter.ItemsViewHolder>() {

    class ItemsViewHolder(cardView: CardView): RecyclerView.ViewHolder(cardView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        val cardView = LayoutInflater.from(parent.context).inflate(R.layout.workoutcard_exercise_item, parent, false) as CardView
        return ItemsViewHolder(cardView)
    }

    override fun getItemCount(): Int {
        return if (isAddingMode) {
            if (workoutElementItems.isEmpty()) {
                1
            } else {
                workoutElementItems.size + 1
            }
        } else {
            workoutElementItems.size
        }
    }

    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
        if (isAddingMode) {
            if (position == 0) {
                holder.itemView.workoutExerciseItemName.visibility = View.GONE
                holder.itemView.workoutExerciseRepsNum.visibility = View.GONE
                holder.itemView.workoutExerciseSetsNum.visibility = View.GONE
                holder.itemView.workoutElement_addAnother.visibility = View.VISIBLE
                holder.itemView.workoutElement_addAnother.setOnClickListener {
                    //nowy element alert dialog
                    listener?.onClickCallback()
                }
            }
            else {
                holder.itemView.workoutExerciseItemName.visibility = View.VISIBLE
                holder.itemView.workoutExerciseRepsNum.visibility = View.VISIBLE
                holder.itemView.workoutExerciseSetsNum.visibility = View.VISIBLE
                holder.itemView.workoutElement_addAnother.visibility = View.GONE
                val item = workoutElementItems[position - 1]
                holder.itemView.workoutExerciseItemName.text = item.exercise?.name
                holder.itemView.workoutExerciseRepsNum.text = item.numOfReps.toString()
                holder.itemView.workoutExerciseSetsNum.text = item.numOfSets.toString()
                val category = item.exercise?.category
                setCategoryImage(category, holder.itemView.workoutExerciseCategoryImage)
            }
        }
        else {
            holder.itemView.workoutExerciseItemName.visibility = View.VISIBLE
            holder.itemView.workoutExerciseRepsNum.visibility = View.VISIBLE
            holder.itemView.workoutExerciseSetsNum.visibility = View.VISIBLE
            holder.itemView.workoutElement_addAnother.visibility = View.GONE
            val item = workoutElementItems[position]
            holder.itemView.workoutExerciseItemName.text = item.exercise?.name
            holder.itemView.workoutExerciseRepsNum.text = item.numOfReps.toString()
            holder.itemView.workoutExerciseSetsNum.text = item.numOfSets.toString()
            val category = item.exercise?.category
            setCategoryImage(category, holder.itemView.workoutExerciseCategoryImage)
        }
    }

    private fun setCategoryImage(category: String?, imageView: ImageView) {
        category?.let {
            imageView.setImageResource(Utils.getCategoryImage(it)) }
    }

    fun getItemAt(position: Int) : WorkoutElement? {
        return if (position > 0 && position < workoutElementItems.size) {
            workoutElementItems[position-1]
        }
        else
            null
    }
}