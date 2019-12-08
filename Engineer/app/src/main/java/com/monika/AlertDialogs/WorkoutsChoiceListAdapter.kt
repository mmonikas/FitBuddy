package com.monika.AlertDialogs

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.core.content.ContextCompat
import com.monika.ExercisesPage.WorkoutSelectionListener
import com.monika.Model.WorkoutPlan.Workout
import com.monika.R
import kotlinx.android.synthetic.main.category_list_item.view.*
import java.util.*
import kotlin.collections.ArrayList

class WorkoutsChoiceListAdapter (private val context: Context, private val workouts: ArrayList<Workout>,
private val listener: WorkoutSelectionListener, private val date: Date
): BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.category_list_item, parent, false)
        val workout = workouts[position]
        var exercises = ""
        workout.exercises?.forEachIndexed { index, workoutElement ->
            exercises += workoutElement.exercise?.name
            workout.exercises?.size?.let { size ->
                if (index < size - 1)
                    exercises += ", "
            }
        }
        val textForName = "${workout.name} (${workout.exercises?.size} exercises) \n$exercises"
        view.name.text = textForName
        view.image.setImageResource(R.drawable.ic_assignment_black_24dp)
        view.image.drawable.setTint(ContextCompat.getColor(context, R.color.primaryColor))
        view.name.textSize = 18f
        view.setOnClickListener {
            listener.onWorkoutSelected(workout, date)
        }
        return view
    }

    override fun getItem(position: Int): Any {
        return workouts[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return workouts.size
    }
}