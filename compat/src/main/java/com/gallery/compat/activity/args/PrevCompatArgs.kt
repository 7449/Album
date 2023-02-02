package com.gallery.compat.activity.args

import android.os.Bundle
import android.os.Parcelable
import androidx.core.os.bundleOf
import com.gallery.core.delegate.args.PrevArgs
import com.gallery.core.delegate.args.PrevArgs.Companion.prevArgsOrDefault
import com.gallery.core.extensions.parcelableVersion
import kotlinx.parcelize.Parcelize

@Parcelize
data class PrevCompatArgs(
    val prevArgs: PrevArgs,
    val gap: Parcelable?,
) : Parcelable {
    companion object {
        private const val Key = "prevCompatArgs"

        fun PrevCompatArgs.toBundle(): Bundle {
            return bundleOf(Key to this)
        }

        val Bundle.prevCompatArgsOrDefault
            get() = parcelableVersion(Key) ?: PrevCompatArgs(prevArgsOrDefault, bundleOf())
    }
}