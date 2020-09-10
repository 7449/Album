package com.gallery.scan

import android.os.Parcelable
import android.provider.MediaStore
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
        val mediaType: String = MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
        val width: Int = 0,
        val height: Int = 0,
        val dateAdded: Long = 0,
        val dateModified: Long = 0,
        val parent: Long = 0,
        val count: Int = 0,
        var isSelected: Boolean = false) : Parcelable