package develop.file.gallery.ui.material.extensions

import android.os.Parcelable
import develop.file.gallery.ui.material.args.MaterialGalleryConfig

internal object ResultCompat {

    internal val Parcelable?.materialGalleryConfigArgOrDefault: MaterialGalleryConfig
        get() = this as? MaterialGalleryConfig ?: MaterialGalleryConfig()

}