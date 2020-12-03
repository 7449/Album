package com.gallery.core.extensions

import android.os.Build

/** 是否高于等于Q */
fun hasQExpand(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

fun hasLExpand(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP

fun hasMExpand(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M