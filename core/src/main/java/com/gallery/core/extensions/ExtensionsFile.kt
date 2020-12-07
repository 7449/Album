package com.gallery.core.extensions

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import java.io.File
import java.io.InputStream
import java.io.OutputStream

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
            Environment.getExternalStoragePublicDirectory(relativePath).path
        } else {
            externalCacheDir?.path ?: cacheDir.path
        }, fileName
)

/** 扫描文件 */
fun Activity.scanFileExpand(uri: Uri, action: (uri: Uri) -> Unit) {
    scanFileExpand(uri.filePathExpand(this), action)
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

/** 复制文件 */
fun Context.copyImageExpand(
        inputUri: Uri,
        displayName: String,
        relativePath: String = Environment.DIRECTORY_DCIM,
): Uri? {
    if (!hasQExpand()) {
        return null
    }
    val contentValues = ContentValues()
    contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
    contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath)
    val outPutUri: Uri? = insertImageUriExpand(contentValues)
    outPutUri ?: return null
    return copyFileExpand(inputUri, outPutUri)
}

/** 复制文件 */
fun Context.copyFileExpand(inputUri: Uri, outPutUri: Uri): Uri? {
    val outStream: OutputStream = contentResolver.openOutputStream(outPutUri) ?: return null
    val inStream: InputStream = contentResolver.openInputStream(inputUri) ?: return null
    outStream.use { out -> inStream.use { input -> input.copyTo(out) } }
    return outPutUri
}