package com.monika.HomeScreen.CalendarPager

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.monika.Model.WorkoutComponents.Exercise
import com.monika.R
import kotlinx.android.synthetic.main.exercise_cardview.view.*

class ExercisesListAdapter(@NonNull options: FirestoreRecyclerOptions<Exercise>):
    FirestoreRecyclerAdapter<Exercise, ExercisesListAdapter.ExercisesListViewHolder>(options) {

    override fun onBindViewHolder(holder: ExercisesListViewHolder, position: Int, exercise: Exercise) {
        val numberString = (position + 1).toString()
        holder.itemView.exerciseNumberInWorkout.text = "$numberString."
        holder.itemView.exerciseName.text = exercise.name
        holder.itemView.exerciseDescription.text = exercise.description
        //holder.itemView.exercise.visibility = View.GONE
        val level = exercise.load
        level?.let {
            val levelString = level.toString()
            if (levelString != "") {
//                holder.itemView.exerciseLevel.text = "Poziom trudności: $levelString"
//                holder.itemView.exerciseLevel.visibility = View.VISIBLE
            }
        }
        val category = exercise.category
        category?.let {
            setCategoryImage(category, holder.itemView.exerciseImage)
        }
    }
//    RecyclerView.Adapter<ExercisesListAdapter.ExercisesListViewHolder>() {

    class ExercisesListViewHolder(cardView: CardView): RecyclerView.ViewHolder(cardView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExercisesListViewHolder {
        val cardView = LayoutInflater.from(parent.context).inflate(R.layout.exercise_cardview, parent, false) as CardView
        //TODO set view size margins etc
        return ExercisesListViewHolder(cardView)
    }

    override fun onDataChanged() {
        super.onDataChanged()

    }

//    override fun onBindViewHolder(holder: ExercisesListViewHolder, position: Int) {
//        val exercise = exercisesForDay[position]
//        val numberString = (position + 1).toString()
//        holder.itemView.exerciseNumberInWorkout.text = "$numberString."
//        holder.itemView.exerciseName.text = exercise.name
//        holder.itemView.exerciseDescription.text = exercise.description
//        //holder.itemView.exercise.visibility = View.GONE
//        val level = exercise.load
//        level?.let {
//            val levelString = level.toString()
//            if (levelString != "") {
////                holder.itemView.exerciseLevel.text = "Poziom trudności: $levelString"
////                holder.itemView.exerciseLevel.visibility = View.VISIBLE
//            }
//        }
//        val category = exercise.category
//        category?.let {
//            setCategoryImage(category, holder.itemView.exerciseImage)
//        }
//    }

    private fun setCategoryImage(category: String, imageView: ImageView) {
        when (category) {
           "Biceps" -> imageView.setImageResource(R.drawable.icons8biceps100)
           "Barki" -> imageView.setImageResource(R.drawable.icons8shoulders100)
            "Plecy" -> imageView.setImageResource(R.drawable.icons8torso100)
            "Nogi" -> imageView.setImageResource(R.drawable.icons8leg100)
            "Pośladki" -> imageView.setImageResource(R.drawable.icons8glutes100)
            "Brzuch" -> imageView.setImageResource(R.drawable.icons8prelum100)
            "Triceps" -> imageView.setImageResource(R.drawable.icons8triceps100)
            "Klatka piersiowa" -> imageView.setImageResource(R.drawable.icons8chest100)
        }
    }
}