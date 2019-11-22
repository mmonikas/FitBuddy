package com.monika.ExercisesMainPage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.monika.Model.WorkoutComponents.Exercise
import com.monika.R
import com.monika.Services.Utils
import kotlinx.android.synthetic.main.exercise_cardview.view.*

class ExercisesListAdapter(private val exercises: ArrayList<Exercise>):
    RecyclerView.Adapter<ExercisesListAdapter.ExercisesListViewHolder>() {

    override fun getItemCount(): Int {
        return exercises.size
    }

    override fun onBindViewHolder(holder: ExercisesListViewHolder, position: Int) {
        val numberString = (position + 1).toString()
        val exercise = exercises[position]
        if (exercise.userId == null) {
            holder.itemView.exerciseNumberInWorkout.visibility = View.GONE
        } else {
            holder.itemView.exerciseNumberInWorkout.visibility = View.VISIBLE
        }
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
        exercise.category?.let {
            setCategoryImage(it, holder.itemView.exerciseImage)
        }
        exercise.equipment?.let {
            holder.itemView.exerciseEquipment.text = it
        }
    }

    class ExercisesListViewHolder(cardView: CardView): RecyclerView.ViewHolder(cardView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExercisesListViewHolder {
        val cardView = LayoutInflater.from(parent.context).inflate(R.layout.exercise_cardview, parent, false) as CardView
        //TODO set view size margins etc
        return ExercisesListViewHolder(cardView)
    }

    private fun setCategoryImage(category: String, imageView: ImageView) {
        imageView.setImageResource(Utils.getCategoryImage(category))
//        when (category) {
//
//           "Biceps" -> imageView.setImageResource(R.drawable.icons8biceps100)
//           "Barki" -> imageView.setImageResource(R.drawable.icons8shoulders100)
//            "Plecy" -> imageView.setImageResource(R.drawable.icons8torso100)
//            "Nogi" -> imageView.setImageResource(R.drawable.icons8leg100)
//            "Pośladki" -> imageView.setImageResource(R.drawable.icons8glutes100)
//            "Brzuch" -> imageView.setImageResource(R.drawable.icons8prelum100)
//            "Triceps" -> imageView.setImageResource(R.drawable.icons8triceps100)
//            "Klatka piersiowa" -> imageView.setImageResource(R.drawable.icons8chest100)
//        }
    }
}