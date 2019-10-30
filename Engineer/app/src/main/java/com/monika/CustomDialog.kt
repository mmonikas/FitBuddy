package com.monika

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.Window
import kotlinx.android.synthetic.main.dialog_category_choice.*

class CustomDialog(context: Context?, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?) :
    AlertDialog(context, cancelable, cancelListener) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    
    private fun showDialog(title: String) {
        val dialog = Dialog(context)
        dialog .requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog .setCancelable(false)
        dialog .setContentView(R.layout.dialog_category_choice)
        setChoiceList()
        dialogChoice_button.setOnClickListener {
           // dialog .dismiss()
        }
        //noBtn.setOnClickListener { dialog .dismiss() }
        dialog.show()

    }

    private fun setChoiceList() {
        dialogChoiceList?.apply {

        }
    }
}

