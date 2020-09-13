@file:Suppress("MemberVisibilityCanBePrivate")

package com.gallery.scan.args.file

import android.database.Cursor
import android.os.Parcelable
import android.provider.MediaStore
import com.gallery.scan.args.ScanEntityFactory
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ScanFileEntity(
        val id: Long = 0,

        val size: Long = 0,
        val displayName: String = "",
        val title: String = "",
        val dateAdded: Long = 0,
        val dateModified: Long = 0,
        val mimeType: String = "",
        val width: Int = 0,
        val height: Int = 0,

        val parent: Long = 0,
        val mediaType: String = MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),

        val orientation: Int = 0,
        val bucketId: String = "",
        val bucketDisplayName: String = "",

        val duration: Long = 0,

        val count: Int = 0,
        var isSelected: Boolean = false) : Parcelable, ScanEntityFactory {
    override fun cursorMoveToNext(cursor: Cursor): ScanEntityFactory = this
}