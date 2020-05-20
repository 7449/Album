package com.gallery.ui.wechat.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.kotlin.expand.os.bundleParcelableArrayListExpand
import androidx.kotlin.expand.os.getBooleanExpand
import androidx.recyclerview.widget.LinearLayoutManager
import com.gallery.core.GalleryConfig
import com.gallery.core.expand.isGif
import com.gallery.core.expand.isVideo
import com.gallery.scan.ScanEntity
import com.gallery.ui.activity.PrevBaseActivity
import com.gallery.ui.adapter.GalleryFinderAdapter
import com.gallery.ui.wechat.R
import com.gallery.ui.wechat.WeChatUiResult
import com.gallery.ui.wechat.adapter.WeChatPrevSelectAdapter
import com.gallery.ui.wechat.obtain
import com.gallery.ui.wechat.util.displayGalleryPrev
import com.gallery.ui.wechat.util.displayGalleryPrevSelect
import kotlinx.android.synthetic.main.gallery_activity_wechat_prev.*
import java.util.*

@SuppressLint("SetTextI18n")
class GalleryWeChatPrevActivity : PrevBaseActivity(R.layout.gallery_activity_wechat_prev), GalleryFinderAdapter.AdapterFinderListener {

    override val hideCheckBox: Boolean
        get() = true

    override val galleryFragmentId: Int
        get() = R.id.preWeChatFragment

    private val selectAdapter: WeChatPrevSelectAdapter = WeChatPrevSelectAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        obtain(uiBundle)
        prevWeChatToolbarBack.setOnClickListener { onPrevFinish() }
        prevWeChatToolbarText.text = "%s / %s".format(0, galleryBundle.multipleMaxCount)
        val selectList: ArrayList<ScanEntity> = bundleParcelableArrayListExpand(GalleryConfig.PREV_START_SELECT)
        val fullImageSelect: Boolean = prevOption.getBooleanExpand(WeChatUiResult.GALLERY_WE_CHAT_RESULT_FULL_IMAGE)
        prevWeChatFullImage.isChecked = fullImageSelect
        prevWeChatToolbarSend.isEnabled = true
        prevWeChatToolbarSend.text = uiBundle.selectText + if (selectList.isEmpty()) "" else "(${selectList.count()}/${galleryBundle.multipleMaxCount})"
        prevWeChatToolbarSend.setOnClickListener {
            if (prevFragment.selectEmpty) {
                prevFragment.selectEntities.add(prevFragment.currentItem)
            }
            onPrevSelectEntities()
        }
        prevWeChatSelect.setOnClickListener { prevFragment.checkBoxClick(it) }

        galleryPrevList.layoutManager = LinearLayoutManager(this).apply {
            this.orientation = LinearLayoutManager.HORIZONTAL
        }
        galleryPrevList.adapter = selectAdapter
        galleryPrevList.visibility = if (selectList.isEmpty()) View.GONE else View.VISIBLE
        galleryPrevListLine.visibility = if (selectList.isEmpty()) View.GONE else View.VISIBLE
        selectAdapter.updateFinder(selectList)
    }

    override fun onGalleryAdapterItemClick(view: View, position: Int, item: ScanEntity) {
        prevFragment.setCurrentItem(prevFragment.allItem.indexOfFirst { it.id == item.id })
    }

    override fun onGalleryFinderThumbnails(finderEntity: ScanEntity, container: FrameLayout) {
        container.displayGalleryPrevSelect(finderEntity)
    }

    override fun onDisplayGalleryPrev(galleryEntity: ScanEntity, container: FrameLayout) {
        container.displayGalleryPrev(galleryEntity)
    }

    override fun onPageSelected(position: Int) {
        val currentItem: ScanEntity = prevFragment.currentItem
        prevWeChatToolbarText.text = (position + 1).toString() + "/" + prevFragment.itemCount
        prevWeChatSelect.isChecked = prevFragment.isCheckBox(position)
        prevWeChatFullImage.visibility = if (currentItem.isGif() || currentItem.isVideo()) View.GONE else View.VISIBLE
        selectAdapter.refreshItem(currentItem)
    }

    override fun onPrevViewCreated(savedInstanceState: Bundle?) {
        prevWeChatToolbarText.text = (prevFragment.currentPosition + 1).toString() + "/" + prevFragment.itemCount
    }

    override fun onChangedCheckBox() {
        prevWeChatToolbarSend.text = uiBundle.selectText + if (prevFragment.selectEmpty) "" else "(${prevFragment.selectCount}/${galleryBundle.multipleMaxCount})"
        galleryPrevList.visibility = if (prevFragment.selectEmpty) View.GONE else View.VISIBLE
        galleryPrevListLine.visibility = if (prevFragment.selectEmpty) View.GONE else View.VISIBLE
        selectAdapter.updateFinder(prevFragment.selectEntities)
    }

    override fun onKeyBackResult(bundle: Bundle) {
        bundle.putBoolean(WeChatUiResult.GALLERY_WE_CHAT_RESULT_FULL_IMAGE, prevWeChatFullImage.isChecked)
    }

    override fun onToolbarFinishResult(bundle: Bundle) {
        bundle.putBoolean(WeChatUiResult.GALLERY_WE_CHAT_RESULT_FULL_IMAGE, prevWeChatFullImage.isChecked)
    }
}