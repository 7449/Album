package com.gallery.core.extensions

import android.view.View

/** 隐藏 */
fun View.hideExpand(): View = apply { if (!isGoneExpand()) visibility = View.GONE }

/** 是否为隐藏GONE */
fun View.isGoneExpand(): Boolean = visibility == View.GONE

/** 显示 */
fun View.showExpand(): View = apply { if (!isVisibleExpand()) visibility = View.VISIBLE }

/** 是否为显示VISIBLE */
fun View.isVisibleExpand(): Boolean = visibility == View.VISIBLE