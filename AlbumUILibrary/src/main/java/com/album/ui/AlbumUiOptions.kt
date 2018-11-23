package com.album.ui

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

const val EXTRA_OPTIONS = BuildConfig.APPLICATION_ID + "options"

fun AlBumUiNightTheme(): AlbumUiOptions {
    return AlbumUiOptions()
}

@Parcelize
class AlbumUiOptions : Parcelable