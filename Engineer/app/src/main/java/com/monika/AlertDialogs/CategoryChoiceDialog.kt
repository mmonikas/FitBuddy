package com.monika.AlertDialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ListView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.monika.Model.WorkoutComponents.Category
import com.monika.R
import com.monika.ExercisesMainPage.CategoryListAdapter
import com.monika.ExercisesMainPage.SelectionListener
import kotlinx.android.synthetic.main.dialog_category_choice.*

class CategoryChoiceDialog(context: Context, listener: SelectionListener) : Dialog(context) {

    private var listener = listener
    private var categoriesList = ArrayList<Category>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val layout = LayoutInflater.from(context).inflate(R.layout.dialog_category_choice, null)
        setContentView(layout)

        val okButton = layout.findViewById<Button>(R.id.dialogChoice_buttonOK)
        okButton.setOnClickListener {
            listener.onConfirmCallback()
            dismiss()
        }

        if (categoriesList.isEmpty()) {
            categoriesList.add(Category(name = "Barki"))
            categoriesList.add(Category(name = "Biceps"))
            categoriesList.add(Category(name = "Plecy"))
            categoriesList.add(Category(name = "Nogi"))
            categoriesList.add(Category(name = "Pośladki"))
            categoriesList.add(Category(name = "Brzuch"))
            categoriesList.add(Category(name = "Triceps"))
            categoriesList.add(Category(name = "Klatka piersiowa"))
        }

        dialogChoiceList.adapter = CategoryListAdapter(
            context = context,
            categories = categoriesList,
            listener = listener
        )

        dialogChoiceList.setOnItemClickListener { parent, view, position, id ->
            listener.onClickCallback(categoriesList[position])
        }
    }


}