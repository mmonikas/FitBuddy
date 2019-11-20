package com.monika.AlertDialogs

import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import com.monika.ExercisesMainPage.ExerciseSelectionListener
import com.monika.Model.WorkoutComponents.Exercise
import com.monika.R
import kotlinx.android.synthetic.main.dialog_category_choice.*

class ExerciseChoiceDialog(private val exercises: ArrayList<Exercise>, context: Context, listener: ExerciseSelectionListener) : Dialog(context) {

    private var listener = listener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val layout = LayoutInflater.from(context).inflate(R.layout.dialog_category_choice, null)
        val titleLabel = layout.findViewById<TextView>(R.id.titleLabel)
        titleLabel.text = Resources.getSystem().getString(R.string.chooseExercise)
        window?.setLayout(500, WindowManager.LayoutParams.WRAP_CONTENT)
        setContentView(layout)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val okButton = layout.findViewById<Button>(R.id.dialogChoice_buttonOK)
        okButton.visibility = View.GONE



//        okButton.setOnClickListener {
//            listener.onConfirmCallback()
//            dismiss()
//        }

//        dialogChoiceList.adapter = ArrayAdapter(
//            context = context,
//            exercises = exercises,
//            listener = listener
//        )

        dialogChoiceList.setOnItemClickListener { parent, view, position, id ->
            listener.onClickCallback(exercises[position])
        }


    }


}