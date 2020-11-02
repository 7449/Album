package com.gallery.ui.wechat

import android.os.Bundle
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
internal data class WeChatPrevSaveArgs(
        val ids: ArrayList<Long>,
) : Parcelable {
    companion object {
        private const val Key = "weChatPrevSaveArgs"
        fun WeChatPrevSaveArgs.putArgs(bundle: Bundle = Bundle()): Bundle {
            bundle.putParcelable(Key, this)
            return bundle
        }

        val Bundle.weChatPrevSaveArgs
            get() = getParcelable<WeChatPrevSaveArgs>(Key)
    }
}

@Parcelize
internal data class WeChatPrevArgs(
        /** 是否是点击预览进入 */
        val isPrev: Boolean,
        /** 限制的视频时长 */
        val videoDuration: Int,
        /** 是否选择原图 */
        val fullImageSelect: Boolean,
) : Parcelable {
    companion object {
        private const val Key = "weChatPrevArgs"
        fun WeChatPrevArgs.putArgs(bundle: Bundle = Bundle()): Bundle {
            bundle.putParcelable(Key, this)
            return bundle
        }

        val Bundle.weChatPrevArgsOrDefault
            get() = apply { classLoader = WeChatPrevArgs::class.java.classLoader }.getParcelable(Key)
                    ?: WeChatPrevArgs(false, 0, false)
    }
}