package com.gallery.ui.wechat.activity

import android.os.Bundle
import com.gallery.ui.activity.PrevBaseActivity
import com.gallery.ui.wechat.R
import com.gallery.ui.wechat.obtain


class GalleryWeChatPrevActivity : PrevBaseActivity(R.layout.gallery_activity_wechat_prev) {

    override val hideCheckBox: Boolean
        get() = true

    override val galleryFragmentId: Int
        get() = R.id.preWeChatFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        obtain(uiBundle)
    }

}