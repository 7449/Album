package com.gallery.sample.crop

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.gallery.core.extensions.hasQExpand
import com.gallery.core.extensions.insertImageUriExpand
import java.io.InputStream
import java.io.OutputStream

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