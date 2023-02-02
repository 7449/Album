package com.gallery.compat.activity.args

import android.os.Bundle
import android.os.Parcelable
import androidx.core.os.bundleOf
import com.gallery.core.args.GalleryConfigs
import com.gallery.core.extensions.parcelableVersion
import kotlinx.parcelize.Parcelize

@Parcelize
data class GalleryCompatArgs(
    val configs: GalleryConfigs,
    val gap: Parcelable?,
) : Parcelable {
    companion object {
        private const val Key = "galleryCompatArgs"

        fun GalleryCompatArgs.toBundle(): Bundle {
            return bundleOf(Key to this)
        }

        val Bundle.galleryCompatArgsOrDefault
            get() = parcelableVersion(Key) ?: GalleryCompatArgs(GalleryConfigs(), bundleOf())
    }
}
