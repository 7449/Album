@file:Suppress("MemberVisibilityCanBePrivate")

package com.gallery.scan.args.picture

import android.database.Cursor
import android.os.Parcelable
import com.gallery.scan.args.ScanEntityFactory
import kotlinx.android.parcel.Parcelize

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
        val bucketDisplayName: String = "") : Parcelable, ScanEntityFactory {
    override fun cursorMoveToNext(cursor: Cursor): ScanEntityFactory = this
}