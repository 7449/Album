package com.gallery.core.widget

import android.os.Parcelable
import androidx.annotation.ColorInt
import kotlinx.parcelize.Parcelize

@Parcelize
data class GalleryTextViewConfig(
        val text: String,
        val textSize: Float,
        @ColorInt val textColor: Int
) : Parcelable

@Parcelize
data class GalleryRecyclerViewConfig(
        val spanCount: Int,
        val orientation: Int,
        val dividerWidth: Int
) : Parcelable