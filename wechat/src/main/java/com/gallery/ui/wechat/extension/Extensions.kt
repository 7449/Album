package com.gallery.ui.wechat.extension

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Parcelable
import android.view.animation.Animation
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.gallery.ui.wechat.args.WeChatGalleryConfig
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

@SuppressLint("SimpleDateFormat")
private val formatter = SimpleDateFormat("yyyy/MM")

internal val Parcelable?.weChatGalleryArgOrDefault: WeChatGalleryConfig
    get() = this as? WeChatGalleryConfig ?: WeChatGalleryConfig()

fun Context.color(@ColorRes id: Int): Int = ContextCompat.getColor(this, id)

fun Long.formatTimeVideo(): String {
    if (toInt() == 0) {
        return "--:--"
    }
    val format: String = String.format(
        "%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(this),
        TimeUnit.MILLISECONDS.toSeconds(this) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(this))
    )
    if (!format.startsWith("0")) {
        return format
    }
    return format.substring(1)
}

fun Long.formatTime(): String {
    if (toInt() == 0) {
        return "--/--"
    }
    return formatter.format(this * 1000)
}

fun Long.toFileSize(): String {
    return when {
        this < 1024 -> DecimalFormat().format(this) + " B"
        this < 1048576 -> DecimalFormat().format(this / 1024) + " KB"
        this < 1073741824 -> DecimalFormat().format(this / 1048576) + " MB"
        else -> DecimalFormat().format(this / 1073741824) + " GB"
    }
}

fun Context.openVideo(uri: Uri, error: () -> Unit) {
    runCatching {
        val video = Intent(Intent.ACTION_VIEW)
        video.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        video.setDataAndType(uri, "video/*")
        startActivity(video)
    }.onFailure { error.invoke() }
}

fun Animation.doOnAnimationEnd(action: (animation: Animation) -> Unit): Animation =
    setAnimationListener(onAnimationEnd = action)

fun Animation.setAnimationListener(
    onAnimationRepeat: (animation: Animation) -> Unit = {},
    onAnimationEnd: (animation: Animation) -> Unit = {},
    onAnimationStart: (animation: Animation) -> Unit = {},
): Animation {
    val listener = object : Animation.AnimationListener {
        override fun onAnimationRepeat(animation: Animation) {
            onAnimationRepeat.invoke(animation)
        }

        override fun onAnimationEnd(animation: Animation) {
            onAnimationEnd.invoke(animation)
        }

        override fun onAnimationStart(animation: Animation) {
            onAnimationStart.invoke(animation)
        }
    }
    setAnimationListener(listener)
    return this
}