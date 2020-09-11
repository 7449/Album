package com.gallery.ui

import android.os.Bundle
import android.os.Parcelable
import com.gallery.core.GalleryBundle
import com.gallery.core.PrevArgs
import com.gallery.core.delegate.PrevDelegate
import com.gallery.scan.args.file.ScanFileEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UIGallerySaveArgs(
        val finderName: String,
        val finderList: ArrayList<ScanFileEntity>
) : Parcelable {
    companion object {
        private const val Key = "uiGallerySaveArgs"

        fun newSaveInstance(finderName: String, finderList: ArrayList<ScanFileEntity>): UIGallerySaveArgs {
            return UIGallerySaveArgs(finderName, finderList)
        }

        fun UIGallerySaveArgs.putArgs(bundle: Bundle = Bundle()): Bundle {
            bundle.putParcelable(Key, this)
            return bundle
        }

        val Bundle.uiGallerySaveArgs
            get() = getParcelable<UIGallerySaveArgs>(Key)
    }
}

@Parcelize
data class UIGalleryArgs(
        val galleryBundle: GalleryBundle,
        val galleryUiBundle: GalleryUiBundle,
        val galleryOption: Bundle,
        val galleryPrevOption: Bundle
) : Parcelable {
    companion object {
        private const val Key = "uiGalleryArgs"

        fun UIGalleryArgs.putArgs(bundle: Bundle = Bundle()): Bundle {
            bundle.putParcelable(Key, this)
            return bundle
        }

        fun Bundle.putUIGalleryArgs(uiGalleryArgs: Bundle) {
            putParcelable(Key, uiGalleryArgs)
        }

        val Bundle.uiGalleryArgs
            get() = getParcelable<UIGalleryArgs>(Key)

        val Bundle.uiGalleryArgsOrDefault
            get() = uiGalleryArgs
                    ?: UIGalleryArgs(GalleryBundle(), GalleryUiBundle(), Bundle(), Bundle())
    }
}

@Parcelize
data class UIPrevArgs(
        /**
         * UI配置参数
         */
        val uiBundle: GalleryUiBundle,
        /**
         * 预览页[PrevDelegate]需要的数据
         */
        val prevArgs: PrevArgs,
        /**
         * 暂存Bundle,用于自定义布局时[GalleryUiBundle]无法满足需要配置时携带数据
         */
        val option: Bundle
) : Parcelable {
    companion object {
        private const val Key = "uiPrevArgs"

        fun UIPrevArgs.putPrevArgs(bundle: Bundle = Bundle()): Bundle {
            bundle.putParcelable(Key, this)
            return bundle
        }

        fun Bundle.putPrevArgs(uiPrevArgs: UIPrevArgs) {
            putParcelable(Key, uiPrevArgs)
        }

        val Bundle.uiPrevArgs
            get() = getParcelable<UIPrevArgs>(Key)
    }
}