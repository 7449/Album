package develop.file.gallery.compat.activity.args

import android.os.Bundle
import android.os.Parcelable
import develop.file.gallery.entity.ScanEntity
import develop.file.gallery.extensions.ResultCompat.parcelableVersion
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class GallerySaveArgs(
    val finderName: String,
    val finderList: ArrayList<ScanEntity>,
) : Parcelable {
    companion object {
        private const val Key = "gallerySaveArgs"

        internal fun onSaveInstanceState(
            finderName: String,
            finderList: ArrayList<ScanEntity>
        ): GallerySaveArgs {
            return GallerySaveArgs(finderName, finderList)
        }

        internal fun GallerySaveArgs.toBundle(bundle: Bundle): Bundle {
            bundle.putParcelable(Key, this)
            return bundle
        }

        internal val Bundle.gallerySaveArgs
            get() = parcelableVersion<GallerySaveArgs>(Key)
    }
}