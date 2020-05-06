package com.gallery.ui.wechat.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.FrameLayout
import androidx.kotlin.expand.os.bundleBundleExpand
import androidx.kotlin.expand.os.bundleParcelableArrayListExpand
import androidx.kotlin.expand.os.getBooleanExpand
import com.gallery.core.callback.IGalleryPrev
import com.gallery.scan.ScanEntity
import com.gallery.ui.activity.PrevBaseActivity
import com.gallery.ui.wechat.R
import com.gallery.ui.wechat.WeChatUiResult
import com.gallery.ui.wechat.obtain
import com.gallery.ui.wechat.util.displayGalleryPrev
import kotlinx.android.synthetic.main.gallery_activity_wechat_prev.*
import java.util.*

@SuppressLint("SetTextI18n")
class GalleryWeChatPrevActivity : PrevBaseActivity(R.layout.gallery_activity_wechat_prev) {

    override val hideCheckBox: Boolean
        get() = true

    override val galleryFragmentId: Int
        get() = R.id.preWeChatFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        obtain(uiBundle)
        prevWeChatToolbarBack.setOnClickListener { onPrevFinish() }
        prevWeChatToolbarText.text = "%s / %s".format(0, galleryBundle.multipleMaxCount)
        val selectList: ArrayList<ScanEntity> = bundleParcelableArrayListExpand(IGalleryPrev.PREV_START_SELECT)
        val fullImageSelect: Boolean = bundleBundleExpand(IGalleryPrev.PREV_START_BUNDLE).getBooleanExpand(WeChatUiResult.GALLERY_WE_CHAT_RESULT_FULL_IMAGE)
        prevWeChatFullImage.isChecked = fullImageSelect
        prevWeChatToolbarSend.isEnabled = selectList.isNotEmpty()
        prevWeChatToolbarSend.text = uiBundle.selectText + if (selectList.isEmpty()) "" else "(${selectList.count()}/${galleryBundle.multipleMaxCount})"
    }

    override fun onDisplayGalleryPrev(galleryEntity: ScanEntity, container: FrameLayout) {
        container.displayGalleryPrev(galleryEntity)
    }

    override fun onPageSelected(position: Int) {
        prevWeChatToolbarText.text = (position + 1).toString() + "/" + prevFragment.itemCount
    }

    override fun onChangedCreated() {
        prevWeChatToolbarText.text = (prevFragment.currentPosition + 1).toString() + "/" + prevFragment.itemCount
    }

    override fun onChangedCheckBox() {
        prevWeChatToolbarSend.text = uiBundle.selectText + if (prevFragment.selectEmpty) "" else "(${prevFragment.selectCount}/${galleryBundle.multipleMaxCount})"
        prevWeChatToolbarText.text = "%s / %s".format(prevFragment.selectCount.toString(), galleryBundle.multipleMaxCount)
    }
}