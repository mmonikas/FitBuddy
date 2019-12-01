package com.monika.WorkoutsMainPage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.monika.AlertDialogs.PlanWorkoutDialog
import com.monika.ExercisesMainPage.WorkoutsPlannerListener
import com.monika.Model.WorkoutPlan.Workout
import com.monika.R
import kotlinx.android.synthetic.main.workout_cardview.view.*
import java.text.SimpleDateFormat
import java.util.*

class WorkoutsListAdapter(private val context: Context, private val workouts: ArrayList<Workout>,
                          private val listener: WorkoutsPlannerListener
):
    RecyclerView.Adapter<WorkoutsListAdapter.WorkoutsViewHolder>() {


    override fun getItemCount(): Int {
        return workouts.size
    }

    override fun onBindViewHolder(holder: WorkoutsViewHolder, position: Int) {
        holder.itemView.cardView_addAnother.visibility = View.GONE
        holder.itemView.cardView_workoutElement.visibility = View.VISIBLE

        val workout = workouts[position]
        if (workout.userId == null) {
            holder.itemView.workoutUserImage.visibility = View.GONE
        } else {
            holder.itemView.workoutUserImage.visibility = View.VISIBLE
        }
        holder.itemView.workoutcard_name.text = workout.name
        holder.itemView.workoutcard_exercisesNumber.text = workout.exercises?.size.toString()
        workout.initDate?.let {
            val workoutDate = workout.initDate?.time
            workoutDate?.let {
                val initDate = Date(workoutDate)
                val formatter = SimpleDateFormat("dd.MM.yyyy")
                val formattedDate = formatter.format(initDate)
                val localeFormat = SimpleDateFormat("EEEE", Locale.forLanguageTag("en-us"))
                val dayOfWeek = localeFormat.format(initDate)
                holder.itemView.workoutcard_inittime.text = "$dayOfWeek, $formattedDate"
            }

        }
        val exercisesInThisWorkout = workout.exercises
        exercisesInThisWorkout?.let {
            if (!exercisesInThisWorkout.isNullOrEmpty()) {
                viewAdapter = WorkoutElementItemsAdapter(workoutElementItems = exercisesInThisWorkout, isAddingMode = false, listener = null)
                val recyclerView = holder.itemView.workoutcard_items
                recyclerView.apply {
                    adapter = viewAdapter
                    layoutManager = LinearLayoutManager(context)
                }
            }
        }

        holder.itemView.workoutcard_planButton.setOnClickListener {
            val planWorkoutDialog = PlanWorkoutDialog(context, workout, listener)
            planWorkoutDialog.show()
        }
        holder.itemView.workoutcard_planButton.visibility = View.VISIBLE
        holder.itemView.workoutcard_logworkout_button.visibility = View.GONE
        holder.itemView.completedButton.visibility = View.GONE
    }

    private lateinit var viewAdapter: RecyclerView.Adapter<*>

    class WorkoutsViewHolder(cardView: CardView): RecyclerView.ViewHolder(cardView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutsViewHolder {
        val cardView = LayoutInflater.from(parent.context).inflate(R.layout.workout_cardview, parent, false) as CardView
        return WorkoutsViewHolder(cardView)
    }
}