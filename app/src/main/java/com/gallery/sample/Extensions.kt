package com.gallery.sample

import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

fun AppCompatActivity.showArray(array: Array<String>, action: (position: Int) -> Unit) {
    AlertDialog.Builder(this)
            .setSingleChoiceItems(array, View.NO_ID)
            { dialog, which ->
                action.invoke(which)
                dialog.dismiss()
            }
            .show()
}