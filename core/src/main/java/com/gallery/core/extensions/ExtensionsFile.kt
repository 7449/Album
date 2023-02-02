package com.gallery.core.extensions

import android.app.Activity
import android.content.ContentResolver
import android.media.MediaScannerConnection
import android.net.Uri
import java.io.File

internal fun String.mkdirsFile(child: String): File {
    val file = File(this)
    if (!file.exists()) {
        file.mkdirs()
    }
    return File(file, child)
}

internal fun Activity.scanFile(uri: Uri, action: (uri: Uri) -> Unit) {
    scanFile(
        when (uri.scheme) {
            ContentResolver.SCHEME_CONTENT -> contentResolver.queryData(uri).orEmpty()
            ContentResolver.SCHEME_FILE -> uri.path.orEmpty()
            else -> throw RuntimeException("unsupported uri:$uri")
        }, action
    )
}

internal fun Activity.scanFile(path: String, action: (uri: Uri) -> Unit) {
    MediaScannerConnection.scanFile(this, arrayOf(path), null) { _: String?, uri: Uri? ->
        runOnUiThread {
            uri ?: return@runOnUiThread
            action.invoke(uri)
        }
    }
}