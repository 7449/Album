package com.album.core

import android.os.Build

//Android版本是否为L
fun hasL(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP

//Android版本是否为M
fun hasM(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

//Android版本是否为N
fun hasN(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N

//Android版本是否为Q
fun hasQ(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q