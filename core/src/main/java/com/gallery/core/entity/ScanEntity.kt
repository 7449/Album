package com.gallery.core.entity

import android.content.ContentUris
import android.net.Uri
import android.os.Parcelable
import android.provider.MediaStore
import com.gallery.scan.impl.file.FileScanEntity
import kotlinx.parcelize.Parcelize

/** [FileScanEntity]中介 */
@Parcelize
data class ScanEntity(
        val delegate: FileScanEntity,
        val count: Int = 0,
        var isSelected: Boolean = false,
) : Parcelable {
    val id: Long
        get() = delegate.id
    val parent: Long
        get() = delegate.parent
    val size: Long
        get() = delegate.size
    val duration: Long
        get() = delegate.duration
    val dateModified: Long
        get() = delegate.dateModified
    val bucketDisplayName: String
        get() = delegate.bucketDisplayName
    val uri: Uri
        get() = when (delegate.mediaType) {
            MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString() -> ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
            )
            MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString() -> ContentUris.withAppendedId(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    id
            )
            else -> Uri.EMPTY
        }
    val isGif: Boolean
        get() = delegate.mimeType.contains("gif") || delegate.mimeType.contains("GIF")
    val isVideo: Boolean
        get() = delegate.mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()
    val isImage: Boolean
        get() = delegate.mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString()
}