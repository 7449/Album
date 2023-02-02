package com.gallery.core.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.gallery.core.entity.ScanEntity
import com.gallery.scan.impl.file.FileScanEntity

inline fun <reified T : Parcelable> Bundle.parcelableVersion(key: String): T? = when {
    Build.VERSION.SDK_INT >= 33 -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}

inline fun <reified T : Parcelable> Bundle.parcelableVersionNotNull(key: String): T =
    requireNotNull(parcelableVersion(key))

fun View.hide(): View = apply { if (!isGone) visibility = View.GONE }

fun View.show(): View = apply { if (!isVisible) visibility = View.VISIBLE }

fun Bundle?.orEmpty(): Bundle = this ?: Bundle.EMPTY

fun Context.drawable(@DrawableRes id: Int): Drawable? =
    if (id == 0) null else ContextCompat.getDrawable(this, id)

fun ArrayList<FileScanEntity>.toScanEntity(): ArrayList<ScanEntity> =
    mapTo(ArrayList()) { ScanEntity(it) }

fun ArrayList<ScanEntity>.toFileEntity(): ArrayList<FileScanEntity> =
    mapTo(ArrayList()) { it.delegate }

fun FileScanEntity.toScanEntity(): ScanEntity = ScanEntity(this)