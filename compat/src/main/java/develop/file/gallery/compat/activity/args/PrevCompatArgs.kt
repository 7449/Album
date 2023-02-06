package develop.file.gallery.compat.activity.args

import android.os.Bundle
import android.os.Parcelable
import androidx.core.os.bundleOf
import develop.file.gallery.delegate.args.PrevArgs
import develop.file.gallery.delegate.args.PrevArgs.Companion.prevArgsOrDefault
import develop.file.gallery.extensions.ResultCompat.parcelableVersion
import kotlinx.parcelize.Parcelize

@Parcelize
data class PrevCompatArgs(
    val prevArgs: PrevArgs,
    val gap: Parcelable?,
) : Parcelable {
    companion object {
        private const val Key = "prevCompatArgs"

        internal fun PrevCompatArgs.toBundle(): Bundle {
            return bundleOf(Key to this)
        }

        internal val Bundle.prevCompatArgsOrDefault
            get() = parcelableVersion(Key) ?: PrevCompatArgs(prevArgsOrDefault, bundleOf())
    }
}