package com.album.core.scan

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AlbumEntity(
        var id: Long = 0,
        var path: String = "",
        var size: Long = 0,
        var duration: Long = 0,
        var parent: Long = 0,
        var mimeType: String = "",
        var displayName: String = "",
        var orientation: Int = 0,
        var bucketId: String = "",
        var bucketDisplayName: String = "",
        var mediaType: String = AlbumColumns.IMAGE,
        var width: Int = 0,
        var height: Int = 0,
        var dataModified: Long = 0,
        var count: Int = 0,
        var isCheck: Boolean = false) : Parcelable

fun AlbumEntity.hasVideo(): Boolean = this.mediaType == AlbumColumns.VIDEO

fun AlbumEntity.hasGif(): Boolean = this.mimeType == "image/gif"

fun ArrayList<AlbumEntity>.mergeEntity(selectEntity: ArrayList<AlbumEntity>) = apply {
    forEach { it.isCheck = false }
    selectEntity.forEach { select -> this.find { it.path == select.path }?.isCheck = true }
}