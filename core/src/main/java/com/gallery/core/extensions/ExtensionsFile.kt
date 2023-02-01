package com.gallery.core.extensions

import android.app.Activity
import android.content.ContentResolver
import android.media.MediaScannerConnection
import android.net.Uri
import java.io.File

/** 创建文件(如果不存在则创建) */
fun String.mkdirsFile(child: String): File {
    val file = File(this)
    if (!file.exists()) {
        file.mkdirs()
    }
    return File(file, child)
}

/** 扫描文件 , content 开头的先获取path 再更新，file 开头的直接获取path更新
 *  刷新数据库要用到文件路径，所以要获取 path */
fun Activity.scanFile(uri: Uri, action: (uri: Uri) -> Unit) {
    scanFile(
        when (uri.scheme) {
            ContentResolver.SCHEME_CONTENT -> contentResolver.queryData(uri).orEmpty()
            ContentResolver.SCHEME_FILE -> uri.path.orEmpty()
            else -> throw RuntimeException("unsupported uri:$uri")
        }, action
    )
}

/** 扫描文件 */
fun Activity.scanFile(path: String, action: (uri: Uri) -> Unit) {
    MediaScannerConnection.scanFile(this, arrayOf(path), null) { _: String?, uri: Uri? ->
        runOnUiThread {
            uri ?: return@runOnUiThread
            action.invoke(uri)
        }
    }
}