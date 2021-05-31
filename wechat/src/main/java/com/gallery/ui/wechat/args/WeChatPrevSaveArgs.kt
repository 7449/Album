package com.gallery.ui.wechat.args

import android.os.Bundle
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

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