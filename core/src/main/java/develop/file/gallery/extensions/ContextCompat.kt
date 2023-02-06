package develop.file.gallery.extensions

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import develop.file.gallery.args.GalleryConfigs
import develop.file.gallery.extensions.FileCompat.mkdirsFile
import java.io.File

internal object ContextCompat {

    internal fun Context.drawable(@DrawableRes id: Int): Drawable? =
        if (id == 0) null else ContextCompat.getDrawable(this, id)

    internal fun Context.square(count: Int): Int =
        resources.displayMetrics.widthPixels / count

    internal fun Context.openVideo(uri: Uri, error: () -> Unit) {
        runCatching {
            val video = Intent(Intent.ACTION_VIEW)
            video.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            video.setDataAndType(uri, "video/*")
            startActivity(video)
        }.onFailure { error.invoke() }
    }

    internal fun Context.takePictureUri(configs: GalleryConfigs): Uri? {
        val file: File = when {
            ResultCompat.hasQ() -> File(configs.fileConfig.picturePath + File.separator + configs.takePictureName)
            else -> Environment.getExternalStoragePublicDirectory(configs.fileConfig.picturePath).absolutePath
                .mkdirsFile(configs.takePictureName)
        }
        return if (configs.isScanVideoMedia) insertVideoUri(file) else insertImageUri(file)
    }

    internal fun Context.takeCropUri(configs: GalleryConfigs): Uri? {
        val file: File = when {
            ResultCompat.hasQ() -> File(configs.fileConfig.cropPath + File.separator + configs.takeCropName)
            else -> Environment.getExternalStoragePublicDirectory(configs.fileConfig.cropPath).absolutePath
                .mkdirsFile(configs.takeCropName)
        }
        return insertImageUri(file)
    }

    private fun Context.insertImageUri(file: File): Uri? = insertImageUri(ContentValues().apply {
        if (ResultCompat.hasQ()) {
            put(MediaStore.MediaColumns.RELATIVE_PATH, file.parent)
            put(MediaStore.MediaColumns.DISPLAY_NAME, file.name)
        } else {
            put(MediaStore.MediaColumns.DATA, file.path)
        }
    })

    private fun Context.insertVideoUri(file: File): Uri? = insertVideoUri(ContentValues().apply {
        if (ResultCompat.hasQ()) {
            put(MediaStore.MediaColumns.RELATIVE_PATH, file.parent)
            put(MediaStore.MediaColumns.DISPLAY_NAME, file.name)
        } else {
            put(MediaStore.MediaColumns.DATA, file.path)
        }
    })

    private fun Context.insertImageUri(contentValues: ContentValues): Uri? =
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        } else {
            null
        }

    private fun Context.insertVideoUri(contentValues: ContentValues): Uri? =
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues)
        } else {
            null
        }

}