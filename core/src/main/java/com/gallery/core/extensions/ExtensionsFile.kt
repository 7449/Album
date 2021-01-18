package com.gallery.core.extensions

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import java.io.File

/** 创建文件夹(如果不存在则创建) */
fun String.mkdirsFileExpand(child: String): File {
    val pathFile = File(this, child)
    if (!pathFile.exists()) {
        pathFile.mkdirs()
    }
    return pathFile
}

/** 获取文件输出路径 */
fun Context.lowerVersionFileExpand(
        fileName: String,
        relativePath: String = Environment.DIRECTORY_DCIM,
): File = File(
        if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
                || !Environment.isExternalStorageRemovable()) {
            @Suppress("DEPRECATION")
            Environment.getExternalStoragePublicDirectory(relativePath).path
        } else {
            externalCacheDir?.path ?: cacheDir.path
        }, fileName
)

/** 扫描文件 , content 开头的先获取path 再更新，file 开头的直接获取path更新
 *  刷新数据库要用到文件路径，所以要获取 path */
fun Activity.scanFileExpand(uri: Uri, action: (uri: Uri) -> Unit) {
    scanFileExpand(when (uri.scheme) {
        ContentResolver.SCHEME_CONTENT -> contentResolver.queryDataExpand(uri).orEmpty()
        ContentResolver.SCHEME_FILE -> uri.path.orEmpty()
        else -> throw RuntimeException("unsupported uri:$uri")
    }, action)
}

/** 扫描文件 */
fun Activity.scanFileExpand(path: String, action: (uri: Uri) -> Unit) {
    MediaScannerConnection.scanFile(this, arrayOf(path), null) { _: String?, uri: Uri? ->
        runOnUiThread {
            uri ?: return@runOnUiThread
            action.invoke(uri)
        }
    }
}