package com.gallery.core.extensions

import android.view.View

fun View.hideExpand(): View = apply { if (!isGoneExpand()) visibility = View.GONE }

fun View.isGoneExpand(): Boolean = visibility == View.GONE

fun View.showExpand(): View = apply { if (!isVisibleExpand()) visibility = View.VISIBLE }

fun View.isVisibleExpand(): Boolean = visibility == View.VISIBLE