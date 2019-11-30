package com.gallery.scan

import android.os.Parcelable
import com.gallery.scan.args.Columns
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ScanEntity(
        var id: Long = 0,
        var size: Long = 0,
        var duration: Long = 0,
        var parent: Long = 0,
        var mimeType: String = "",
        var displayName: String = "",
        var orientation: Int = 0,
        var bucketId: String = "",
        var bucketDisplayName: String = "",
        var mediaType: String = Columns.IMAGE,
        var width: Int = 0,
        var height: Int = 0,
        var dataModified: Long = 0,
        var count: Int = 0,
        var isCheck: Boolean = false) : Parcelable