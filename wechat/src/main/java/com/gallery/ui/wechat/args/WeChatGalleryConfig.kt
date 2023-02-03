package com.gallery.ui.wechat.args

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class WeChatGalleryConfig(
    val isPrev: Boolean = false,
    val fullImageSelect: Boolean = false,
) : Parcelable