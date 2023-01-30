package com.gallery.ui.wechat.args

import android.os.Bundle
import android.os.Parcelable
import androidx.core.os.bundleOf
import com.gallery.core.extensions.parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class WeChatPrevSaveArgs(
    val ids: ArrayList<Long>,
) : Parcelable {
    companion object {
        private const val Key = "weChatPrevSaveArgs"
        fun WeChatPrevSaveArgs.toBundle(bundle: Bundle = bundleOf()): Bundle {
            bundle.putParcelable(Key, this)
            return bundle
        }

        val Bundle.weChatPrevSaveArgs
            get() = parcelable<WeChatPrevSaveArgs>(Key)
    }
}