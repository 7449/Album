package com.gallery.scan

import android.os.Parcelable
import com.gallery.scan.args.Columns
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ScanEntity(
        val id: Long = 0,
        val size: Long = 0,
        val duration: Long = 0,
        val mimeType: String = "",
        val displayName: String = "",
        val orientation: Int = 0,
        val bucketId: String = "",
        val bucketDisplayName: String = "",
        val mediaType: String = Columns.IMAGE,
        val width: Int = 0,
        val height: Int = 0,
        val dataModified: Long = 0,
        val parent: Long = 0,
        val count: Int = 0,
        var isCheck: Boolean = false) : Parcelable