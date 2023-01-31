package com.gallery.sample

import android.content.Context
import android.graphics.Color
import android.widget.TextView
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder

fun TextView.clickShowColorPicker() {
    setOnClickListener {
        context.showColorPicker(currentTextColor) {
            setTextColor(it)
        }
    }
}

fun Context.showColorPicker(
    defaultColor: Int = Color.BLACK,
    action: (color: Int) -> Unit
) {
    ColorPickerDialogBuilder
        .with(this)
        .setTitle("ColorPicker")
        .initialColor(defaultColor)
        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
        .density(12)
        .setPositiveButton(android.R.string.ok) { _, selectedColor, _ -> action.invoke(selectedColor) }
        .build()
        .show()
}