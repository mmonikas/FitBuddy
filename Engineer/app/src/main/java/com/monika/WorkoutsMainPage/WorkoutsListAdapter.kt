package com.monika.WorkoutsMainPage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.monika.Model.WorkoutPlan.Workout
import com.monika.R
import kotlinx.android.synthetic.main.fragment_workouts_list.view.*
import kotlinx.android.synthetic.main.workout_cardview.view.*
import java.text.SimpleDateFormat
import java.util.*

class WorkoutsListAdapter(private val workoutsList: ArrayList<Workout>):
RecyclerView.Adapter<WorkoutsListAdapter.WorkoutsViewHolder>() {

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    class WorkoutsViewHolder(cardView: CardView): RecyclerView.ViewHolder(cardView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutsViewHolder {
        val cardView = LayoutInflater.from(parent.context).inflate(R.layout.workout_cardview, parent, false) as CardView
        return WorkoutsViewHolder(cardView)
    }

    override fun getItemCount(): Int {
        return workoutsList.size
    }

    override fun onBindViewHolder(holder: WorkoutsViewHolder, position: Int) {
        val workout = workoutsList[position]
        holder.itemView.workoutcard_name.text = workout.name
        holder.itemView.workoutcard_exercisesNumber.text = workout.exercises?.size.toString()
        workout.initDate?.let {
            val workoutDate = workout.initDate?.time
            workoutDate?.let {
                val initDate = Date(workoutDate)
                val formatter = SimpleDateFormat("dd.MM.yyyy")
                val formattedDate = formatter.format(initDate)
                val localeFormat = SimpleDateFormat("EEEE", Locale.forLanguageTag("pl-PL"))
                val dayOfWeek = localeFormat.format(initDate)
                holder.itemView.workoutcard_inittime.text = "$dayOfWeek, $formattedDate"
            }

        }
        val exercisesInThisWorkout = workout.exercises
        exercisesInThisWorkout?.let {
            if (!exercisesInThisWorkout.isNullOrEmpty()) {
                viewAdapter = WorkoutElementItemsAdapter(workoutElementItems = exercisesInThisWorkout)
                holder.itemView.workoutListRecyclerView.apply {
                    adapter = viewAdapter
                    //layoutManager = LinearLayoutManager()
                }
            }
        }
    }
}