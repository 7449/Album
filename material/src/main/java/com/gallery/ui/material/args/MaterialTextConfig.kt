package com.gallery.ui.material.args

import android.os.Parcelable
import androidx.annotation.ColorInt
import kotlinx.parcelize.Parcelize

@Parcelize
data class MaterialTextConfig(
    val text: String = "",
    val textSize: Float = 0F,
    @ColorInt val textColor: Int = -1,
) : Parcelable