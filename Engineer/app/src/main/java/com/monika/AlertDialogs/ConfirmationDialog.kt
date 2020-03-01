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
import com.monika.ExercisesPage.ConfirmationListener
import com.monika.Model.WorkoutPlan.PlannedWorkout
import com.monika.R

class ConfirmationDialog(context: Context, private val title: String, private val content: String,
                         listener: ConfirmationListener, private val elementPosition: Int?, private val plannedWorkout: PlannedWorkout?,
                         private val isCancelable: Boolean) : Dialog(context) {

    private var listener = listener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val layout = LayoutInflater.from(context).inflate(R.layout.dialog_confirmation, null)
        window?.setLayout(300, WindowManager.LayoutParams.WRAP_CONTENT)
        setContentView(layout)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val okButton = layout.findViewById<Button>(R.id.confirmationDialog_ok)
        val cancelButton = layout.findViewById<Button>(R.id.confirmationDialog_cancel)
        val titleLabel = layout.findViewById<TextView>(R.id.title)
        val contentLabel = layout.findViewById<TextView>(R.id.content)

        titleLabel.text = title
        contentLabel.text = content

        okButton.setOnClickListener {
            listener.onConfirmCallback(position = elementPosition, plannedWorkout = plannedWorkout)
        }

        if (isCancelable) {
            cancelButton.visibility = View.VISIBLE
            cancelButton.setOnClickListener {
                listener.onCancelCallback()
            }
        }
        else {
            cancelButton.visibility = View.GONE
        }
    }
}