package com.monika.ExercisesMainPage

import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.monika.HomeScreen.MainActivity.MainActivity
import com.monika.R
import kotlinx.android.synthetic.main.fragment_exercises_list.*


class ExercisesListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: ExercisesListAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private val presenter = ExercisesListPresenter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val options = presenter.getOptionsForUpdatesListener()
        viewAdapter = ExercisesListAdapter(options)
        return inflater.inflate(R.layout.fragment_exercises_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpRecyclerView()
        setFAB()
    }

    override fun onStart() {
        super.onStart()
        viewAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        viewAdapter.stopListening()
    }

    private fun setFAB() {
        fab_addExercise.setOnClickListener {
            findNavController().navigate(R.id.addExerciseFragment)
        }
    }

    private fun setUpRecyclerView() {
        viewManager = LinearLayoutManager(context)
        recyclerView = view!!.findViewById<RecyclerView>(R.id.exercisesListRecyclerView).apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
        val swipeController = SwipeController(object : SwipeControllerActions() {
            override fun onRightClicked(position: Int) {
                presenter.removeItemAt(viewAdapter.getItem(position))
            }

            override fun onLeftClicked(position: Int) {
                val bundle = Bundle()
                bundle.putSerializable("exerciseForDetails", viewAdapter.getItem(position))
                findNavController().navigate(R.id.addExerciseFragment, bundle, null)
                //edit
            }
        }, context = context!!)

        val itemTouchHelper = ItemTouchHelper(swipeController)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                swipeController.onDraw(c)
            }
        })
        (activity as MainActivity).hideProgressView()
    }
}
