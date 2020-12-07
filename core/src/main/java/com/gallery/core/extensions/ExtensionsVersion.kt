package com.gallery.core.extensions

import android.os.Build

/** 是否高于等于Q */
fun hasQExpand(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

/** 是否高于等于L */
fun hasLExpand(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP

/** 是否高于等于M */
fun hasMExpand(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M