package com.monika.WorkoutsMainPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.monika.HomeScreen.CalendarPager.ExercisesListAdapter
import com.monika.HomeScreen.MainActivity.MainActivity
import com.monika.Model.WorkoutComponents.Exercise
import com.monika.R
import kotlinx.android.synthetic.main.fragment_exercises_list.*

class ExercisesListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exercises_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey("workoutElements") }?.apply {
            val exercises = get("workoutElements") as ArrayList<Exercise>
            exercises.let {
                viewManager = LinearLayoutManager(context)
                viewAdapter = ExercisesListAdapter(exercises)
                recyclerView = view.findViewById<RecyclerView>(R.id.exercisesListRecyclerView).apply {
                    // use this setting to improve performance if you know that changes
                    // in content do not change the layout size of the RecyclerView
                    setHasFixedSize(true)
                    // use a linear layout manager
                    layoutManager = viewManager
                    // specify an viewAdapter (see also next example)
                    adapter = viewAdapter
                }
            }
            (activity as MainActivity).hideProgressView()
        }
        setFAB()
    }

    private fun setFAB() {
        fab_addExercise.setOnClickListener {
            findNavController().navigate(R.id.action_exercisesListFragment_to_addExerciseFragment)
        }
    }
}
