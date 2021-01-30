package com.gallery.sample.scan

import android.net.Uri
import android.os.Parcelable
import android.provider.MediaStore
import androidx.lifecycle.ViewModelProvider
import com.gallery.scan.args.CursorLoaderArgs
import com.gallery.scan.args.ScanEntityFactory
import com.gallery.scan.extensions.getLongOrDefault
import com.gallery.scan.extensions.getStringOrDefault
import com.gallery.scan.extensions.scanImpl
import com.gallery.scan.impl.ScanImpl
import kotlinx.parcelize.Parcelize

@Parcelize
class ScanAudioArgs : CursorLoaderArgs(AudioColumns.uri, AudioColumns.columns)

@Parcelize
data class ScanAudioEntity(
        val id: Long = 0,
        val size: Long = 0,
        val displayName: String = "",
        val title: String = "",
        val dateAdded: Long = 0,
        val dateModified: Long = 0,
        val mimeType: String = "",
) : Parcelable

val String.isAudioExpand: Boolean
    get() = this == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()

fun ViewModelProvider.scanAudioImpl(): ScanImpl<ScanAudioEntity> = scanImpl()

object AudioColumns {

    val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

    val columns: Array<String> = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.SIZE,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATE_ADDED,
            MediaStore.Audio.Media.DATE_MODIFIED,
            MediaStore.Audio.Media.MIME_TYPE
    )
}

fun ScanEntityFactory.Companion.audioExpand(): ScanEntityFactory {
    return action {
        ScanAudioEntity(
                it.getLongOrDefault(MediaStore.Audio.Media._ID),
                it.getLongOrDefault(MediaStore.Audio.Media.SIZE),
                it.getStringOrDefault(MediaStore.Audio.Media.DISPLAY_NAME),
                it.getStringOrDefault(MediaStore.Audio.Media.TITLE),
                it.getLongOrDefault(MediaStore.Audio.Media.DATE_ADDED),
                it.getLongOrDefault(MediaStore.Audio.Media.DATE_MODIFIED),
                it.getStringOrDefault(MediaStore.Audio.Media.MIME_TYPE),
        )
    }
}