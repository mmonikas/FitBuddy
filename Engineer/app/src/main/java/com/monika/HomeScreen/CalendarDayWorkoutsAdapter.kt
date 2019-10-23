package com.monika.HomeScreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.monika.Model.WorkoutComponents.Category
import com.monika.Model.WorkoutComponents.Exercise
import com.monika.R
import kotlinx.android.synthetic.main.calendar_day_workout_cardview.view.*

class CalendarDayWorkoutsAdapter(private val exercisesForDay: ArrayList<Exercise>):
    RecyclerView.Adapter<CalendarDayWorkoutsAdapter.CalendarDayWorkoutsViewHolder>() {

    class CalendarDayWorkoutsViewHolder(cardView: CardView): RecyclerView.ViewHolder(cardView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarDayWorkoutsViewHolder {
        val cardView = LayoutInflater.from(parent.context).inflate(R.layout.calendar_day_workout_cardview, parent, false) as CardView

        //TODO set view size margins etc
        return CalendarDayWorkoutsViewHolder(cardView)
    }

    override fun getItemCount(): Int {
        return exercisesForDay.size
    }

    override fun onBindViewHolder(holder: CalendarDayWorkoutsViewHolder, position: Int) {
        val exercise = exercisesForDay[position]
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
        exercise.category?.let {
            setCategoryImage(exercise.category, holder.itemView.exerciseImage)
        }
    }

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