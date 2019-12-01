package com.monika.ExercisesMainPage

import android.content.Context
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.monika.AlertDialogs.ConfirmationDialog
import com.monika.Enums.FirebaseRequestResult
import com.monika.MainActivity.MainActivity
import com.monika.Model.WorkoutPlan.PlannedWorkout
import com.monika.R
import com.monika.Services.Utils
import com.monika.WorkoutsMainPage.WorkoutsListAdapter
import kotlinx.android.synthetic.main.fragment_exercises_list.*


class ExercisesListFragment : Fragment(), ConfirmationListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: ExercisesListAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var confirmationDialog: ConfirmationDialog

    private val presenter = ExercisesListPresenter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewAdapter = ExercisesListAdapter(presenter.exercises)

        return inflater.inflate(R.layout.fragment_exercises_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpRecyclerView()
        setFAB()
    }

    override fun onStart() {
        super.onStart()
        context?.let { it ->
            view?.let { view ->
                Utils.hideSoftKeyBoard(it, view)
            }
        }
        getContent()
    }


    private fun getContent() {
        (activity as MainActivity).showProgressView()
        (activity as MainActivity).disableBottomNavigation()
        presenter.fetchExercises { result ->
            if (result.isNotEmpty()) {
                presenter.exercises = result
                if (context != null) {
                    viewAdapter = ExercisesListAdapter(presenter.exercises)
                    recyclerView.adapter = viewAdapter
                    (activity as MainActivity).enableBottomNavigation()
                }
//                viewAdapter = WorkoutsListAdapter(context!!, presenter.workouts, this)
            }
//            viewAdapter = WorkoutsListAdapter(context!!, presenter.workouts, this)
//            setRecyclerView()
            activity?.let {
                (it as MainActivity).hideProgressView()
            }
        }


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
                if (presenter.exercises[position].userId == null) {
                    showCantEditNorDeleteInfo()
                }
                else {
                    presenter.checkIfExerciseCanBeDeleted(position) {
                        existsSomewhere ->
                        when (existsSomewhere) {
                            null -> {
                                showErrorInfo()
                            }
                            true -> {
                                showCantDeleteInfo()
                            }
                            else -> {
                                showDeleteConfirmationDialog(position)
                            }
                        }
                    }
                }
            }

            override fun onLeftClicked(position: Int) {
                if (presenter.exercises[position].userId == null) {
                    showCantEditNorDeleteInfo()
                }
                else {
                    val bundle = Bundle()
                    bundle.putSerializable("exerciseForDetails", presenter.exercises[position])
                    findNavController().navigate(R.id.addExerciseFragment, bundle, null)
                }
            }
        }, context = context!!, isEditPossible = true)

        val itemTouchHelper = ItemTouchHelper(swipeController)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                swipeController.onDraw(c)
            }
        })
        (activity as MainActivity).hideProgressView()
    }

    private fun showDeleteConfirmationDialog(position: Int) {

    }

    private fun showCantDeleteInfo() {
        val title = resources.getString(R.string.deleteExerciseLabel)
        val contentText = resources.getString(R.string.deleteExerciseContentCant)
        context?.let {
            confirmationDialog = ConfirmationDialog(it, title, contentText, this, null, null, false)
            confirmationDialog.show()
        }
    }

    private fun showErrorInfo() {
        (activity as MainActivity).showToast(R.string.errorOccured)
    }

    private fun showCantEditNorDeleteInfo() {
        (activity as MainActivity).showToast(R.string.cantEditNorDelete)
    }

    override fun onConfirmCallback(position: Int?, plannedWorkout: PlannedWorkout?) {
        if (position != null) {
            presenter.removeItemAt((position)) { result ->
                if (result == FirebaseRequestResult.SUCCESS) {
                    viewAdapter.notifyDataSetChanged()
                    (activity as MainActivity).showToast(R.string.removeSuccess)
                } else if (result == FirebaseRequestResult.FAILURE) {
                    (activity as MainActivity).showToast(R.string.operationError)
                }
            }
        }
        confirmationDialog.dismiss()
    }

    override fun onCancelCallback() {
        confirmationDialog.dismiss()
    }

}
