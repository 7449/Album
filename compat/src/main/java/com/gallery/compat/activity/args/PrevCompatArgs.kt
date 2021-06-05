package com.gallery.compat.activity.args

import android.os.Bundle
import android.os.Parcelable
import com.gallery.core.GalleryBundle
import com.gallery.core.delegate.args.PrevArgs
import com.gallery.core.delegate.args.PrevArgs.Companion.prevArgsOrDefault
import com.gallery.core.delegate.impl.PrevDelegateImpl
import kotlinx.parcelize.Parcelize

/**
 * 为预览页提供参数配置
 * [GalleryBundle]在[prevArgs]中，所以这里不必要传递
 */
@Parcelize
data class PrevCompatArgs(
    /**
     * 预览[PrevDelegateImpl]需要的数据
     */
    val prevArgs: PrevArgs,
    /**
     * 自定义参数配置
     */
    val customBundle: Parcelable?,
) : Parcelable {
    companion object {
        private const val Key = "prevCompatArgs"

        fun PrevCompatArgs.putPrevArgs(bundle: Bundle = Bundle()): Bundle {
            bundle.putParcelable(Key, this)
            return bundle
        }

        val Bundle.prevCompatArgsOrDefault
            get() = getParcelable(Key) ?: PrevCompatArgs(
                prevArgsOrDefault,
                Bundle()
            )
    }
}