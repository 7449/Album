@file:Suppress("MemberVisibilityCanBePrivate")

package com.gallery.scan.args.audio

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ScanAudioEntity(
        val id: Long = 0,

        val size: Long = 0,
        val displayName: String = "",
        val title: String = "",
        val dateAdded: Long = 0,
        val dateModified: Long = 0,
        val mimeType: String = "",
        val width: Int = 0,
        val height: Int = 0) : Parcelable