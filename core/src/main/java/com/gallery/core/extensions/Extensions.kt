package com.gallery.core.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.setMargins
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView

@ChecksSdkIntAtLeast(api = 29)
internal fun hasQ(): Boolean = SDK_INT >= 29

internal fun Context.square(count: Int): Int = resources.displayMetrics.widthPixels / count

internal fun Context.openVideo(uri: Uri, error: () -> Unit) {
    runCatching {
        val video = Intent(Intent.ACTION_VIEW)
        video.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        video.setDataAndType(uri, "video/*")
        startActivity(video)
    }.onFailure { error.invoke() }
}

internal fun RecyclerView.ViewHolder.setOnClick(action: (position: Int) -> Unit) = apply {
    itemView.setOnClickListener {
        val position = bindingAdapterPosition
        if (position == RecyclerView.NO_POSITION) return@setOnClickListener
        action.invoke(position)
    }
}

internal fun ViewGroup.verticalLinear(display: Int): LinearLayout {
    return LinearLayout(context).apply {
        orientation = LinearLayout.VERTICAL
        layoutParams = LinearLayout.LayoutParams(display, display)
    }
}

internal fun LinearLayout.imageView(divider: Int): AppCompatImageView {
    return AppCompatImageView(context).apply {
        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        ).apply {
            setMargins(divider, divider, divider, 0)
            weight = 1f
        }
    }
}

internal fun LinearLayout.textView(divider: Int): AppCompatTextView {
    return AppCompatTextView(context).apply {
        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(divider, 0, divider, divider)
            setPadding(0, 0, 0, 10)
        }
        gravity = Gravity.CENTER
    }
}

internal fun ViewGroup.frame(divider: Int, display: Int): FrameLayout {
    return FrameLayout(context).apply {
        layoutParams = FrameLayout.LayoutParams(display, display)
            .apply { setPadding(divider) }
    }
}

internal fun FrameLayout.checkBox(divider: Int, display: Int): AppCompatTextView {
    return AppCompatTextView(context).apply {
        layoutParams = FrameLayout.LayoutParams(display / 6, display / 6).apply {
            setPadding(divider)
            setMargins(divider)
            gravity = Gravity.END
        }
        hide()
    }
}