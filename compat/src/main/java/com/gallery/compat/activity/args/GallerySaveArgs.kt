package com.gallery.compat.activity.args

import android.os.Bundle
import android.os.Parcelable
import com.gallery.core.entity.ScanEntity
import com.gallery.core.extensions.parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class GallerySaveArgs(
    val finderName: String,
    val finderList: ArrayList<ScanEntity>,
) : Parcelable {
    companion object {
        private const val Key = "gallerySaveArgs"

        fun onSaveInstanceState(
            finderName: String,
            finderList: ArrayList<ScanEntity>
        ): GallerySaveArgs {
            return GallerySaveArgs(finderName, finderList)
        }

        fun GallerySaveArgs.toBundle(bundle: Bundle): Bundle {
            bundle.putParcelable(Key, this)
            return bundle
        }

        val Bundle.gallerySaveArgs
            get() = parcelable<GallerySaveArgs>(Key)
    }
}