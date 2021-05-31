package com.gallery.compat.activity.args

import android.os.Bundle
import android.os.Parcelable
import com.gallery.compat.GalleryCompatBundle
import com.gallery.core.GalleryBundle
import kotlinx.parcelize.Parcelize

/**
 * 参数配置
 */
@Parcelize
data class GalleryCompatArgs(
    /**
     * 核心模块的参数配置
     */
    val bundle: GalleryBundle,
    /**
     * 简单的配置参数
     */
    val compatBundle: GalleryCompatBundle,
    /**
     * 自定义参数配置
     */
    val customBundle: Parcelable?,
) : Parcelable {
    companion object {
        private const val Key = "galleryCompatArgs"

        fun GalleryCompatArgs.putArgs(bundle: Bundle = Bundle()): Bundle {
            bundle.putParcelable(Key, this)
            return bundle
        }

        val Bundle.galleryCompatArgsOrDefault
            get() = getParcelable(Key)
                ?: GalleryCompatArgs(GalleryBundle(), GalleryCompatBundle(), Bundle())
    }
}
