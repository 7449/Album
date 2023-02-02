package com.gallery.core.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import androidx.annotation.ChecksSdkIntAtLeast

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