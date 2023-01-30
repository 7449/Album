package com.gallery.core.extensions

import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible

fun View.hide(): View = apply { if (!isGone()) visibility = View.GONE }

fun View.show(): View = apply { if (!isVisible()) visibility = View.VISIBLE }

private fun View.isGone(): Boolean = isGone

private fun View.isVisible(): Boolean = isVisible