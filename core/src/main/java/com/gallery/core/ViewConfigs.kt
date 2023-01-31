package com.gallery.core

import android.graphics.Color
import android.os.Parcelable
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import kotlinx.parcelize.Parcelize

@Parcelize
data class CameraConfig(
    val text: String = "",
    val textSize: Float = 16F,
    @ColorInt val textColor: Int = Color.WHITE,
    @DrawableRes val icon: Int = R.drawable.ic_default_camera_drawable,
    @ColorInt val iconColor: Int = Color.parseColor("#FF02A5D2"),
    @ColorInt val bg: Int = Color.parseColor("#FFB0C9C9"),
    @DrawableRes val emptyIcon: Int = 0,
    @DrawableRes val checkBoxIcon: Int = R.drawable.selector_default_gallery_item_check,
) : Parcelable

@Parcelize
data class GridConfig(
    val spanCount: Int = 3,
    val orientation: Int = RecyclerView.VERTICAL,
) : Parcelable