package com.gallery.ui.wechat.util

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import com.gallery.scan.ScanEntity
import java.text.DecimalFormat
import java.text.SimpleDateFormat

@SuppressLint("SimpleDateFormat")
private val formatter = SimpleDateFormat("mm:ss")

internal fun Long.formatTime(): String {
    if (toInt() == 0) {
        return "--:--"
    }
    val format: String = formatter.format(this)
    if (!format.startsWith("0")) {
        return format
    }
    return format.substring(1)
}

internal fun RotateAnimation.doOnAnimationEnd(action: (animation: Animation) -> Unit) {
    setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationRepeat(animation: Animation) {
        }

        override fun onAnimationEnd(animation: Animation) {
            action.invoke(animation)
        }

        override fun onAnimationStart(animation: Animation) {
        }
    })
}

internal fun View.addOnPreDrawListener(action: () -> Unit) {
    viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
        override fun onPreDraw(): Boolean {
            viewTreeObserver.removeOnPreDrawListener(this)
            action.invoke()
            return true
        }
    })
}

internal fun ScanEntity.isGif() = mimeType.contains("gif")

internal fun ScanEntity.isVideo() = mediaType == "3"

internal fun Long.toFileSize(): String {
    val df = DecimalFormat()
    return when {
        this < 1024 -> {
            df.format(this) + " B"
        }
        this < 1048576 -> {
            df.format(this / 1024) + " KB"
        }
        this < 1073741824 -> {
            df.format(this / 1048576) + " MB"
        }
        else -> {
            df.format(this / 1073741824) + " GB"
        }
    }
}
