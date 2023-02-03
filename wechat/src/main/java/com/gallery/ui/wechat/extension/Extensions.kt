package com.gallery.ui.wechat.extension

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Parcelable
import android.view.Gravity
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.core.view.postDelayed
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gallery.compat.extensions.galleryFragment
import com.gallery.core.extensions.hide
import com.gallery.core.extensions.show
import com.gallery.ui.wechat.activity.WeChatGalleryActivity
import com.gallery.ui.wechat.args.WeChatGalleryConfig
import com.gallery.ui.wechat.colorWhite
import com.gallery.ui.wechat.size13
import com.gallery.ui.wechat.widget.WeChatGalleryItem
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

@SuppressLint("SimpleDateFormat")
private val formatter = SimpleDateFormat("yyyy/MM")

internal val Parcelable?.weChatGalleryArgOrDefault: WeChatGalleryConfig
    get() = this as? WeChatGalleryConfig ?: WeChatGalleryConfig()

internal fun Context.color(@ColorRes id: Int): Int = ContextCompat.getColor(this, id)

internal fun Long.formatTimeVideo(): String {
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

internal fun Long.formatTime(): String {
    if (toInt() == 0) {
        return "--/--"
    }
    return formatter.format(this * 1000)
}

internal fun Long.toFileSize(): String {
    return when {
        this < 1024 -> DecimalFormat().format(this) + " B"
        this < 1048576 -> DecimalFormat().format(this / 1024) + " KB"
        this < 1073741824 -> DecimalFormat().format(this / 1048576) + " MB"
        else -> DecimalFormat().format(this / 1073741824) + " GB"
    }
}

internal fun Context.openVideo(uri: Uri, error: () -> Unit) {
    runCatching {
        val video = Intent(Intent.ACTION_VIEW)
        video.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        video.setDataAndType(uri, "video/*")
        startActivity(video)
    }.onFailure { error.invoke() }
}

internal fun Animation.doOnAnimationEnd(action: (animation: Animation) -> Unit): Animation =
    setAnimationListener(onAnimationEnd = action)

internal fun Animation.setAnimationListener(
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

internal val rotateAnimationSupport = RotateAnimation(
    0.toFloat(),
    180.toFloat(),
    Animation.RELATIVE_TO_SELF,
    0.5.toFloat(),
    Animation.RELATIVE_TO_SELF,
    0.5.toFloat()
).apply {
    interpolator = LinearInterpolator()
    duration = 200
    fillAfter = true
}

internal val rotateAnimationSupport2 = RotateAnimation(
    180.toFloat(),
    360.toFloat(),
    Animation.RELATIVE_TO_SELF,
    0.5.toFloat(),
    Animation.RELATIVE_TO_SELF,
    0.5.toFloat()
).apply {
    interpolator = LinearInterpolator()
    duration = 200
    fillAfter = true
}

internal fun FrameLayout.getCheckBoxView(): TextView {
    val checkBox = get(1) as TextView
    checkBox.gravity = Gravity.CENTER
    checkBox.setTextColor(colorWhite)
    checkBox.textSize = size13
    return checkBox
}

internal fun FrameLayout.createGalleryItem(width: Int, height: Int): WeChatGalleryItem {
    return if (tag is WeChatGalleryItem) {
        tag as WeChatGalleryItem
    } else {
        WeChatGalleryItem(context).apply {
            this@createGalleryItem.tag = this
            this@createGalleryItem.addView(this, 0, FrameLayout.LayoutParams(width, height))
        }
    }
}

internal fun WeChatGalleryActivity.scrollView(textView: TextView) {
    val currentFragment = galleryFragment ?: return
    currentFragment.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            recyclerView.layoutManager ?: return
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val position: Int = layoutManager.findFirstCompletelyVisibleItemPosition()
            if (dx == 0 && dy == 0) {
                textView.hide()
            } else {
                textView.show()
            }
            currentFragment.allItem.getOrNull(position)?.let {
                textView.text = it.dateModified.formatTime()
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            textView.postDelayed(1000) { textView.hide() }
        }
    })

}