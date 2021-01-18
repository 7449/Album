package com.gallery.core.entity

import android.net.Uri
import android.os.Parcelable
import com.gallery.core.extensions.externalUriExpand
import com.gallery.core.extensions.isGifExpand
import com.gallery.core.extensions.isImageExpand
import com.gallery.core.extensions.isVideoExpand
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
        get() = delegate.id.externalUriExpand(delegate.mediaType)
    val isGif: Boolean
        get() = delegate.mimeType.isGifExpand
    val isVideo: Boolean
        get() = delegate.mediaType.isVideoExpand
    val isImage: Boolean
        get() = delegate.mediaType.isImageExpand
}