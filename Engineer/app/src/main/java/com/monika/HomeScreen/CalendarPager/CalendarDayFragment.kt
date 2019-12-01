package com.monika.HomeScreen.CalendarPager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.monika.AlertDialogs.AddWorkoutChoiceDialog
import com.monika.AlertDialogs.ConfirmationDialog
import com.monika.Enums.FirebaseRequestResult
import com.monika.ExercisesMainPage.ConfirmationListener
import com.monika.ExercisesMainPage.PlannedWorkoutsLogCompleted
import com.monika.ExercisesMainPage.WorkoutSelectionListener
import com.monika.MainActivity.MainActivity
import com.monika.Model.CalendarDayTimestamp
import com.monika.Model.WorkoutPlan.PlannedWorkout
import com.monika.Model.WorkoutPlan.Workout
import com.monika.R
import com.monika.WorkoutsMainPage.PlannedWorkoutsAdapter
import kotlinx.android.synthetic.main.fragment_collection_calendar_day.*
import java.text.SimpleDateFormat
import java.util.*


class CalendarDayFragment : Fragment(), PlannedWorkoutsLogCompleted, ConfirmationListener, WorkoutSelectionListener{

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private val presenter = CalendarDayFragmentPresenter()
    private lateinit var timestamp: CalendarDayTimestamp
    private lateinit var confirmationDialog: ConfirmationDialog
    private lateinit var addWorkoutSelectDialog: AddWorkoutChoiceDialog
    private lateinit var confirmAddingWorkoutDialog: ConfirmationDialog
    private var isAddingPlanned: Boolean = false
    private val formatter = SimpleDateFormat("dd.MM.yyyy")



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                               savedInstanceState: Bundle? ): View? {
        // Inflate the layout for this fragment
        if (arguments != null) {
            //   workouts = arguments?.get("workouts") as ArrayList<Workout>
//            val workouts = arguments?.get("workouts") as ArrayList<PlannedWorkout>
//            presenter.workouts = workouts
            timestamp = arguments?.get("timestamp") as CalendarDayTimestamp
        }
        return inflater.inflate(R.layout.fragment_collection_calendar_day, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        calendarDay_progressView.visibility = View.VISIBLE
        timestamp.timestamp?.let { timestamp ->
            presenter.fetchPlannedWorkouts(timestamp) {
                plannedWorkouts ->
                presenter.plannedWorkouts = plannedWorkouts
                viewManager = LinearLayoutManager(context)
                val formattedDate = formatter.parse(timestamp)
                viewAdapter = PlannedWorkoutsAdapter(false, view.context, plannedWorkouts, this, formattedDate)
                recyclerView = view.findViewById<RecyclerView>(R.id.calendarDay_recyclerView).apply {
                    // use this setting to improve performance if you know that changes
                    // in content do not change the layout size of the RecyclerView
                    setHasFixedSize(true)
                    // use a linear layout manager
                    layoutManager = viewManager

                    // specify an viewAdapter (see also next example)
                    adapter = viewAdapter
                }
                view.findViewById<LinearLayout>(R.id.calendarDay_progressView).visibility = View.GONE
            }
        }
//        arguments?.takeIf { it.containsKey("workoutElements") }?.apply {
//            val exercises = get("workoutElements") as ArrayList<Exercise>
//            exercises.let {
    }

    override fun onCompletedChosen(plannedWorkout: PlannedWorkout, position: Int) {
        context?.let { context ->
            val title = resources.getString(R.string.logWorkout)
            val subtitle = resources.getString(R.string.logWorkoutSubtitle)
            isAddingPlanned = false
            confirmationDialog = ConfirmationDialog(context, title, subtitle,
                listener = this, elementPosition = position, plannedWorkout = plannedWorkout, isCancelable = true)
            confirmationDialog.show()
        }
    }

    override fun onAddWorkoutClicked(date: Date) {
       context?.let { context ->
           if ((activity as MainActivity).allWorkouts.isEmpty()) {
               view?.findViewById<LinearLayout>(R.id.calendarDay_progressView)?.visibility = View.VISIBLE
               presenter.fetchWorkouts { result ->
                   view?.findViewById<LinearLayout>(R.id.calendarDay_progressView)?.visibility = View.GONE
                   (activity as MainActivity).allWorkouts = result
                   addWorkoutSelectDialog = AddWorkoutChoiceDialog(result, context, this, date)
                   addWorkoutSelectDialog.show()
               }
           }
           else {
               addWorkoutSelectDialog = AddWorkoutChoiceDialog((activity as MainActivity).allWorkouts,
                   context, this, date)
               addWorkoutSelectDialog.show()
           }

       }
    }

    override fun onConfirmCallback(position: Int?, plannedWorkout: PlannedWorkout?) {
        if (isAddingPlanned) {
            plannedWorkout?.let { item ->
                view?.findViewById<LinearLayout>(R.id.calendarDay_progressView)?.visibility = View.VISIBLE
                presenter.registerAddedPlannedWorkout(item) { result ->
                    when (result) {
                        FirebaseRequestResult.SUCCESS -> {
                            (activity as MainActivity).showToast(R.string.workoutLogAsDone)
                        }
                        FirebaseRequestResult.FAILURE -> {
                            (activity as MainActivity).showToast(R.string.operationError)
                        }
                        FirebaseRequestResult.COMPLETED -> {
                            viewAdapter.notifyItemChanged(0)
                        }
                    }
                    confirmationDialog.dismiss()
                    addWorkoutSelectDialog.dismiss()
                    viewAdapter.notifyDataSetChanged()
                    view?.findViewById<LinearLayout>(R.id.calendarDay_progressView)?.visibility = View.GONE
                }
            }

        }
        else {
            position?.let { pos ->
                view?.findViewById<LinearLayout>(R.id.calendarDay_progressView)?.visibility = View.VISIBLE
                presenter.registerPlannedWorkoutCompletion(presenter.plannedWorkouts[pos]) { result ->
                    view?.findViewById<LinearLayout>(R.id.calendarDay_progressView)?.visibility = View.GONE
                    when (result) {
                        FirebaseRequestResult.SUCCESS -> {
                            (activity as MainActivity).showToast(R.string.workoutLogAsDone)
                        }
                        FirebaseRequestResult.FAILURE -> {
                            (activity as MainActivity).showToast(R.string.operationError)
                        }
                        FirebaseRequestResult.COMPLETED -> {
                            viewAdapter.notifyItemChanged(pos)
                        }
                    }
                    confirmationDialog.dismiss()
                }
            }
        }
    }

    override fun onCancelCallback() {
        confirmationDialog.dismiss()
    }

    override fun onWorkoutSelected(workout: Workout, date: Date) {
        context?.let { context ->
            val title = resources.getString(R.string.logWorkout)
            val subtitle = resources.getString(R.string.logWorkoutSubtitle)
            val plannedWorkout = PlannedWorkout()
            plannedWorkout.workout = workout
            plannedWorkout.date = formatter.format(date)
            plannedWorkout.completed = true
            plannedWorkout.userId = FirebaseAuth.getInstance().currentUser?.uid
            confirmationDialog = ConfirmationDialog(context, title, subtitle, this,
                elementPosition = null, isCancelable = true, plannedWorkout = plannedWorkout)
            isAddingPlanned = true
            confirmationDialog.show()
        }
    }



}