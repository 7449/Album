package com.gallery.sample

import android.content.Context
import android.graphics.Color
import android.widget.TextView
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import com.gallery.core.extensions.drawable
import com.gallery.sample.widget.GallerySelectIconDialog

fun TextView.clickShowColorPicker() {
    setOnClickListener {
        context.showColorPicker(currentTextColor) {
            setTextColor(it)
        }
    }
}

fun Any?.toIntOrNull(action: () -> Int): Int {
    return toString().toIntOrNull() ?: action.invoke()
}

fun TextView.showCompoundDrawables(id: Int) {
    val drawable = context.drawable(id).apply {
        this?.setBounds(1, 1, 50, 50)
    }
    setCompoundDrawables(null, null, drawable, null)
}

fun TextView.clickSelectIcon() {
    setOnClickListener {
        GallerySelectIconDialog.show(context) {
            tag = it.second
            showCompoundDrawables(it.second)
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