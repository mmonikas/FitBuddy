package com.monika.AlertDialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.Button
import com.monika.Model.WorkoutComponents.Category
import com.monika.R
import com.monika.ExercisesMainPage.CategoryListAdapter
import com.monika.ExercisesMainPage.SelectionListener
import com.monika.Model.ModelSingletons.Categories
import kotlinx.android.synthetic.main.dialog_category_choice.*

class CategoryChoiceDialog(context: Context, listener: SelectionListener) : Dialog(context) {

    private var listener = listener
    private var categoriesList = ArrayList<Category>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val layout = LayoutInflater.from(context).inflate(R.layout.dialog_category_choice, null)
        window?.setLayout(300, WindowManager.LayoutParams.WRAP_CONTENT)
        setContentView(layout)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val okButton = layout.findViewById<Button>(R.id.dialogChoice_buttonOK)
        okButton.visibility = View.GONE
//        okButton.setOnClickListener {
//            listener.onConfirmCallback()
//            dismiss()
//        }

        if (categoriesList.isEmpty()) {
            categoriesList.addAll(Categories.instance.getItems())
            categoriesList.sortBy { element -> element.name }
            categoriesList.map { element -> element.name?.capitalize() }
        }

        dialogChoiceList.adapter = CategoryListAdapter(
            context = context,
            categories = categoriesList,
            listener = listener
        )

        dialogChoiceList.setOnItemClickListener { parent, view, position, id ->
            listener.onClickCallback(categoriesList[position])
           // listener.onConfirmCallback()
        }


    }


}