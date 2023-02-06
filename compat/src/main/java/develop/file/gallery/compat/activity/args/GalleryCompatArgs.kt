package develop.file.gallery.compat.activity.args

import android.os.Bundle
import android.os.Parcelable
import androidx.core.os.bundleOf
import develop.file.gallery.args.GalleryConfigs
import develop.file.gallery.extensions.ResultCompat.parcelableVersion
import kotlinx.parcelize.Parcelize

@Parcelize
data class GalleryCompatArgs(
    val configs: GalleryConfigs,
    val gap: Parcelable?,
) : Parcelable {
    companion object {
        private const val Key = "galleryCompatArgs"

        internal fun GalleryCompatArgs.toBundle(): Bundle {
            return bundleOf(Key to this)
        }

        internal val Bundle.galleryCompatArgsOrDefault
            get() = parcelableVersion(Key) ?: GalleryCompatArgs(GalleryConfigs(), bundleOf())
    }
}
