package com.gallery.sample.scan

import android.net.Uri
import android.os.Build
import android.os.Parcelable
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.gallery.scan.args.CursorLoaderArgs
import com.gallery.scan.args.ScanEntityFactory
import com.gallery.scan.extensions.getIntOrDefault
import com.gallery.scan.extensions.getLongOrDefault
import com.gallery.scan.extensions.getStringOrDefault
import com.gallery.scan.extensions.scanImpl
import com.gallery.scan.impl.ScanImpl
import kotlinx.parcelize.Parcelize

@Parcelize
class ScanPictureArgs : CursorLoaderArgs(PictureColumns.uri, PictureColumns.columns)

object PictureColumns {

    val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

    @RequiresApi(Build.VERSION_CODES.Q)
    val columns: Array<String> = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.TITLE,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.DATE_MODIFIED,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.WIDTH,
            MediaStore.Images.Media.HEIGHT,
            MediaStore.Images.Media.ORIENTATION,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME
    )
}

@Parcelize
data class ScanPictureEntity(
        val id: Long = 0,
        val size: Long = 0,
        val displayName: String = "",
        val title: String = "",
        val dateAdded: Long = 0,
        val dateModified: Long = 0,
        val mimeType: String = "",
        val width: Int = 0,
        val height: Int = 0,
        val orientation: Int = 0,
        val bucketId: String = "",
        val bucketDisplayName: String = "",
) : Parcelable

fun ViewModelProvider.scanPictureImpl(): ScanImpl<ScanPictureEntity> = scanImpl()

@RequiresApi(Build.VERSION_CODES.Q)
fun ScanEntityFactory.Companion.pictureExpand(): ScanEntityFactory {
    return action {
        ScanPictureEntity(
                it.getLongOrDefault(MediaStore.Images.Media._ID),
                it.getLongOrDefault(MediaStore.Images.Media.SIZE),
                it.getStringOrDefault(MediaStore.Images.Media.DISPLAY_NAME),
                it.getStringOrDefault(MediaStore.Images.Media.TITLE),
                it.getLongOrDefault(MediaStore.Images.Media.DATE_ADDED),
                it.getLongOrDefault(MediaStore.Images.Media.DATE_MODIFIED),
                it.getStringOrDefault(MediaStore.Images.Media.MIME_TYPE),
                it.getIntOrDefault(MediaStore.Images.Media.WIDTH),
                it.getIntOrDefault(MediaStore.Images.Media.HEIGHT),
                it.getIntOrDefault(MediaStore.Images.Media.ORIENTATION),
                it.getStringOrDefault(MediaStore.Images.Media.BUCKET_ID),
                it.getStringOrDefault(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        )
    }
}