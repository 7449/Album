@file:Suppress("MemberVisibilityCanBePrivate")

package com.gallery.scan.args

import android.database.Cursor
import android.os.Parcelable
import android.provider.MediaStore
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ScanMinimumEntity(
        override val id: Long = 0,

        override val size: Long = 0,
        override val displayName: String = "",
        override val title: String = "",
        override val dateAdded: Long = 0,
        override val dateModified: Long = 0,
        override val mimeType: String = "",
        override val width: Int = 0,
        override val height: Int = 0,

        override val parent: Long = 0,
        override val mediaType: String = MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),

        override val orientation: Int = 0,
        override val bucketId: String = "",
        override val bucketDisplayName: String = "",

        override val duration: Long = 0,

        val count: Int = 0,
        var isSelected: Boolean = false) : ScanBaseEntity(id, size, displayName, title, dateAdded, dateModified, mimeType, width, height, parent, mediaType, orientation, bucketId, bucketDisplayName, duration), Parcelable, IScanEntityFactory {
    override fun onCreateCursor(cursor: Cursor): IScanEntityFactory = this
}