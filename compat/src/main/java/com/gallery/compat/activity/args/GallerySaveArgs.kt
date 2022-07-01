package com.gallery.compat.activity.args

import android.os.Bundle
import android.os.Parcelable
import com.gallery.compat.activity.GalleryCompatActivity
import com.gallery.core.entity.ScanEntity
import kotlinx.parcelize.Parcelize

/**
 * 功能较少，只是在[GalleryCompatActivity]中使用到
 * 作用:
 *  用于横竖屏切换时保存当前目录名和目录集合
 */
@Parcelize
internal data class GallerySaveArgs(
        val finderName: String,
        val finderList: ArrayList<ScanEntity>,
) : Parcelable {
    companion object {
        private const val Key = "gallerySaveArgs"

        fun newSaveInstance(
                finderName: String,
                finderList: ArrayList<ScanEntity>
        ): GallerySaveArgs {
            return GallerySaveArgs(finderName, finderList)
        }

        fun GallerySaveArgs.putArgs(bundle: Bundle = Bundle()): Bundle {
            bundle.putParcelable(Key, this)
            return bundle
        }

        val Bundle.gallerySaveArgs
            get() = getParcelable<GallerySaveArgs>(Key)
    }
}