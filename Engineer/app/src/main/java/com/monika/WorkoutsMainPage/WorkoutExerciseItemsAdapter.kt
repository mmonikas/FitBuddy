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
                hideWorkoutElement(holder)
                showAddAnother(holder)
                holder.itemView.workoutElement_addAnother.setOnClickListener {
                    //nowy element alert dialog
                    listener?.onClickCallback()
                }
            }
            else {
                hideAddAnother(holder)
                showWorkoutElement(holder)
                val item = workoutElementItems[position - 1]
                holder.itemView.workoutExerciseItemName.text = item.exercise?.name
                holder.itemView.workoutExerciseSetsNum.text = item.numOfSets.toString()
                if (item.isTimeIntervalMode != null) {
                    if (item.isTimeIntervalMode == true) {
                        setViewForTimeIntervalMode(holder)
                        holder.itemView.workoutExerciseTimeInterval.text = item.timeInterval.toString()
                    }
                    else {
                        setViewForRepsMode(holder)
                        holder.itemView.workoutExerciseRepsNum.text = item.numOfReps.toString()
                    }
                }
                else {
                    setViewForRepsMode(holder)
                    holder.itemView.workoutExerciseRepsNum.text = item.numOfReps.toString()
                }

                holder.itemView.workoutExerciseRepsNum.text = item.numOfReps.toString()
                val category = item.exercise?.category
                setCategoryImage(category, holder.itemView.workoutExerciseCategoryImage)
            }
        }
        else {
            showWorkoutElement(holder)
            hideAddAnother(holder)
            val item = workoutElementItems[position]
            holder.itemView.workoutExerciseItemName.text = item.exercise?.name
            holder.itemView.workoutExerciseSetsNum.text = item.numOfSets.toString()
            if (item.isTimeIntervalMode != null) {
                if (item.isTimeIntervalMode == true) {
                    setViewForTimeIntervalMode(holder)
                    holder.itemView.workoutExerciseTimeInterval.text = item.timeInterval.toString()
                }
                else {
                    setViewForRepsMode(holder)
                    holder.itemView.workoutExerciseRepsNum.text = item.numOfReps.toString()
                }
            }
            else {
                setViewForRepsMode(holder)
                holder.itemView.workoutExerciseRepsNum.text = item.numOfReps.toString()
            }
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

    private fun hideWorkoutElement(holder: ItemsViewHolder) {
        holder.itemView.workoutExerciseItemName.visibility = View.GONE
        holder.itemView.workoutExerciseRepsNum.visibility = View.GONE
        holder.itemView.workoutExerciseSetsNum.visibility = View.GONE
        holder.itemView.workoutElement_Element.visibility = View.GONE
        holder.itemView.time.visibility = View.GONE
        holder.itemView.workoutExerciseTimeInterval.visibility = View.GONE
    }

    private fun showWorkoutElement(holder: ItemsViewHolder) {
        holder.itemView.workoutExerciseItemName.visibility = View.VISIBLE
        holder.itemView.workoutExerciseRepsNum.visibility = View.VISIBLE
        holder.itemView.workoutExerciseSetsNum.visibility = View.VISIBLE
        holder.itemView.workoutElement_Element.visibility = View.VISIBLE
        holder.itemView.time.visibility = View.VISIBLE
        holder.itemView.workoutExerciseTimeInterval.visibility = View.VISIBLE
    }

    private fun hideAddAnother(holder: ItemsViewHolder) {
        holder.itemView.workoutElementcardView_addAnother.visibility = View.GONE
        holder.itemView.workoutElement_addAnother.visibility = View.GONE
    }

    private fun showAddAnother(holder: ItemsViewHolder) {
        holder.itemView.workoutElementcardView_addAnother.visibility = View.VISIBLE
        holder.itemView.workoutElement_addAnother.visibility = View.VISIBLE
    }

    private fun setViewForTimeIntervalMode(holder: ItemsViewHolder) {
        holder.itemView.workoutExerciseRepsNum.visibility = View.GONE
        holder.itemView.repsNum.visibility = View.GONE
        holder.itemView.time.visibility = View.VISIBLE
        holder.itemView.workoutExerciseTimeInterval.visibility = View.VISIBLE
    }

    private fun setViewForRepsMode(holder: ItemsViewHolder) {
        holder.itemView.time.visibility = View.GONE
        holder.itemView.workoutExerciseTimeInterval.visibility = View.GONE
        holder.itemView.workoutExerciseRepsNum.visibility = View.VISIBLE
        holder.itemView.repsNum.visibility = View.VISIBLE
    }
}