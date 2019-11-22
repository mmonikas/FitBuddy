package com.monika.AlertDialogs

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.Toast
import com.monika.ExercisesMainPage.WorkoutsPlannerListener
import com.monika.Model.WorkoutPlan.Workout
import com.monika.R
import kotlinx.android.synthetic.main.dialog_plan_workout.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class PlanWorkoutDialog(context: Context, private val workout: Workout, private val listener: WorkoutsPlannerListener) : Dialog(context), DatePickerDialog.OnDateSetListener {

    private var stringDates = ArrayList<String>()
    private var selectedDates = ArrayList<String>()
    private lateinit var adapter: ArrayAdapter<String>
    private val formatter = SimpleDateFormat("dd.MM.yyyy")
    private val calendar: Calendar = Calendar.getInstance(TimeZone.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val layout = LayoutInflater.from(context).inflate(R.layout.dialog_plan_workout, null)
        setContentView(layout)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)

        planWorkoutName.text = workout.name

        val okButton = layout.findViewById<Button>(R.id.workoutElement_confirmButton)
        okButton.setOnClickListener {
            if (selectedDates.isNotEmpty()) {
                listener.datesChoosenFor(workout = workout, dates = selectedDates)
                selectedDates.clear()
                dismiss()
            } else {
                Toast.makeText(context, R.string.noDatesChosen, Toast.LENGTH_LONG).show()
            }

        }
        planWorkout_cancelButton.setOnClickListener {
            selectedDates.clear()
            dismiss()
        }

        planWorkout_chooseWhen.setOnClickListener {
            openDatePickerDialog()
        }

        adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, stringDates)
        planWorkout_datesList.adapter = adapter
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val realMonth = month + 1
        val chosenDate: Date = formatter.parse("$dayOfMonth.$realMonth.$year") as Date
        val formattedDate = getSimpleStringDate(chosenDate)
        if (!selectedDates.contains(formattedDate)) {
            selectedDates.add(formattedDate)
            stringDates.add(getStringOfDate(chosenDate))
        }
        adapter.notifyDataSetChanged()
    }

    private fun openDatePickerDialog() {
        val dialog = DatePickerDialog(context, this,
            calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        dialog.datePicker.minDate = calendar.timeInMillis
        dialog.show()
    }

    private fun getStringOfDate(date: Date): String {
        val formatter = SimpleDateFormat("dd.MM.yyyy")
        val formattedDate = formatter.format(date)
        val localeFormat = SimpleDateFormat("EEEE", Locale.forLanguageTag("en-us"))
        val dayOfWeek = localeFormat.format(date)
        return "$dayOfWeek, $formattedDate"
    }

    private fun getSimpleStringDate(date: Date): String {
        val formatter = SimpleDateFormat("dd.MM.yyyy")
        val formattedDate = formatter.format(date)
        return formattedDate
    }


}