package com.monika.HomeScreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.monika.Model.WorkoutPlan.Workout
import com.monika.R
import kotlinx.android.synthetic.main.calendar_day_workout_cardview.view.*
import kotlinx.android.synthetic.main.exercise_card.view.*

class CalendarDayWorkoutsAdapter(private val workoutsForDay: ArrayList<Workout>):
    RecyclerView.Adapter<CalendarDayWorkoutsAdapter.CalendarDayWorkoutsViewHolder>() {

    class CalendarDayWorkoutsViewHolder(cardView: CardView): RecyclerView.ViewHolder(cardView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarDayWorkoutsViewHolder {
        val cardView = LayoutInflater.from(parent.context).inflate(R.layout.calendar_day_workout_cardview, parent, false) as CardView

        //TODO set view size margins etc
        return CalendarDayWorkoutsViewHolder(cardView)
    }

    override fun getItemCount(): Int {
        return workoutsForDay.size
    }

    override fun onBindViewHolder(holder: CalendarDayWorkoutsViewHolder, position: Int) {
        //cos tam ustawianko textView np
        holder.itemView.exerciseName.text = workoutsForDay[position].name
        holder.itemView.exerciseDescription.text = workoutsForDay[position].userID
    }

}