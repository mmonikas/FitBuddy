package com.monika.AlertDialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import com.monika.ExercisesPage.WorkoutSelectionListener
import com.monika.Model.WorkoutPlan.Workout
import com.monika.R
import kotlinx.android.synthetic.main.dialog_category_choice.*
import java.util.*
import kotlin.collections.ArrayList

class AddWorkoutChoiceDialog(private val workouts: ArrayList<Workout>, context: Context,
                             private val listener: WorkoutSelectionListener, private val date: Date
) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val layout = LayoutInflater.from(context).inflate(R.layout.dialog_category_choice, null)
        val titleLabel = layout.findViewById<TextView>(R.id.titleLabel)
        titleLabel.text = context.resources.getString(R.string.chooseWorkout)
        window?.setLayout(500, WindowManager.LayoutParams.WRAP_CONTENT)
        setContentView(layout)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val okButton = layout.findViewById<Button>(R.id.dialogChoice_buttonOK)
        okButton.visibility = View.GONE

        dialogChoiceList.adapter = WorkoutsChoiceListAdapter(
            context = context,
            workouts = workouts,
            date = date,
            listener = listener
        )

        dialogChoiceList.setOnItemClickListener { parent, view, position, id ->
            listener.onWorkoutSelected(workouts[position], date)
        }


    }


}