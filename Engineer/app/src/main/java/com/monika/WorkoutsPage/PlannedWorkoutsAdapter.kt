package com.monika.WorkoutsPage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.monika.ExercisesPage.PlannedWorkoutsLogCompleted
import com.monika.Model.WorkoutPlan.PlannedWorkout
import com.monika.Model.WorkoutPlan.Workout
import com.monika.R
import kotlinx.android.synthetic.main.workout_cardview.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PlannedWorkoutsAdapter(private val isHistory: Boolean, private val context: Context, private val plannedWorkouts: ArrayList<PlannedWorkout>,
                             private val listener: PlannedWorkoutsLogCompleted, private val pageDate: Date?):
    RecyclerView.Adapter<PlannedWorkoutsAdapter.PlannedWorkoutsViewHolder>() {

    private var workoutsForToday = ArrayList<Workout>()
    private var isWorkoutForToday: Boolean? = null
    private var formatter = SimpleDateFormat("dd.MM.yyyy")
    private val localeFormat = SimpleDateFormat("EEEE", Locale.forLanguageTag("en-us"))

    override fun getItemCount(): Int {
        return if (plannedWorkouts.isNotEmpty()) {
            plannedWorkouts.size
        } else {
            return if (isHistory)
                0
            else
                1
        }
    }

    override fun onBindViewHolder(holder: PlannedWorkoutsViewHolder, position: Int) {
        if (plannedWorkouts.isEmpty()) {
            if (isHistory) {
                holder.itemView.cardView_addAnother.visibility = View.GONE
                holder.itemView.cardView_workoutElement.visibility = View.GONE
            }
            else {
                holder.itemView.cardView_workoutElement.visibility = View.GONE
                holder.itemView.cardView_addAnother.visibility = View.VISIBLE
                holder.itemView.workoutCard_addAnother.setOnClickListener {
                    //przeniesc do dodawania treningu do dnia
                    pageDate?.let { date ->
                        listener.onAddWorkoutClicked(date)
                    }

                }
            }
        } else {
            holder.itemView.cardView_addAnother.visibility = View.GONE
            holder.itemView.cardView_workoutElement.visibility = View.VISIBLE
            val workout = plannedWorkouts[position].workout
            holder.itemView.workoutcard_name.text = workout?.name
            holder.itemView.workoutcard_exercisesNumber.text = workout?.exercises?.size.toString()
            if (isHistory) {
                holder.itemView.workoutcard_items.visibility = View.GONE

            }
            else {
                val exercisesInThisWorkout = workout?.exercises
                exercisesInThisWorkout?.let {
                    if (!exercisesInThisWorkout.isNullOrEmpty()) {
                        viewAdapter = WorkoutElementItemsAdapter(
                            workoutElementItems = exercisesInThisWorkout,
                            isAddingMode = false,
                            listener = null
                        )
                        val recyclerView = holder.itemView.workoutcard_items
                        recyclerView.visibility = View.VISIBLE
                        recyclerView.apply {
                            adapter = viewAdapter
                            layoutManager = LinearLayoutManager(context)
                        }
                    }
                }
            }
            holder.itemView.workoutcard_logworkout_button.visibility = View.VISIBLE
            holder.itemView.workoutcard_planButton.visibility = View.GONE
            if (isHistory) {
                plannedWorkouts[position].date?.let {
                    holder.itemView.initTimeLabel.text = context.resources.getString(R.string.plannedDate)
                    holder.itemView.workoutcard_inittime.text = it
                    holder.itemView.workoutcard_inittime.visibility = View.VISIBLE
                    holder.itemView.initTimeLabel.visibility = View.VISIBLE
                }

            }
            else {
                holder.itemView.workoutcard_inittime.visibility = View.GONE
                holder.itemView.initTimeLabel.visibility = View.GONE
            }


            holder.itemView.workoutcard_logworkout_button.setOnClickListener {
                listener.onCompletedChosen(plannedWorkouts[position], position)
            }
//
            plannedWorkouts[position].completed?.let { isCompleted ->
                if (isCompleted) {
                    holder.itemView.workoutcard_logworkout_button.visibility = View.GONE
                    holder.itemView.completedButton.visibility = View.VISIBLE
                    if (isHistory) {
                        holder.itemView.workoutCardView.strokeColor =
                            ContextCompat.getColor(context, R.color.primaryLightColor)
                    }
                    else {
                        holder.itemView.workoutCardView.strokeColor =
                            ContextCompat.getColor(context, R.color.accentOrange)
                    }
                }
                else {
                    if (isHistory) {
                        holder.itemView.workoutcard_logworkout_button.visibility = View.GONE
                        holder.itemView.completedButton.visibility = View.VISIBLE
                        holder.itemView.completedButton.setText(R.string.notCompleted)
                        holder.itemView.completedButton.setTextColor(ContextCompat.getColor(context, R.color.primaryColor))
                        val drawable = context.getDrawable(R.drawable.ic_done_white_24dp)
                        holder.itemView.completedButton.setCompoundDrawables(drawable, null, null, null)
                    }
                    else {
                        holder.itemView.workoutcard_logworkout_button.visibility = View.VISIBLE
                        holder.itemView.completedButton.visibility = View.GONE
                        holder.itemView.workoutCardView.strokeColor =
                            ContextCompat.getColor(context, R.color.primaryLightColor)
                    }
                }
            }


        }
    }

    private lateinit var viewAdapter: RecyclerView.Adapter<*>

    class PlannedWorkoutsViewHolder(cardView: CardView): RecyclerView.ViewHolder(cardView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlannedWorkoutsViewHolder {
        val cardView = LayoutInflater.from(parent.context).inflate(R.layout.workout_cardview, parent, false) as CardView
        return PlannedWorkoutsViewHolder(cardView)
    }



}