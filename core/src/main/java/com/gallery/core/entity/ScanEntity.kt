package com.gallery.core.entity

import android.net.Uri
import android.os.Parcelable
import com.gallery.scan.extensions.*
import kotlinx.android.parcel.Parcelize

/** [ScanFileEntity]中介 */
@Parcelize
data class ScanEntity(
        val delegate: ScanFileEntity,
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
        get() = delegate.mimeType.isVideoExpand
    val isImage: Boolean
        get() = delegate.mimeType.isImageExpand
}