package com.gallery.ui.wechat.activity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.kotlin.expand.os.bundleParcelableArrayListExpand
import androidx.kotlin.expand.os.getBooleanExpand
import androidx.kotlin.expand.os.getSerializableOrDefault
import androidx.recyclerview.widget.LinearLayoutManager
import com.gallery.core.GalleryBundle
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

@SuppressLint("SetTextI18n")
class GalleryWeChatPrevActivity : PrevBaseActivity(R.layout.gallery_activity_wechat_prev), GalleryFinderAdapter.AdapterFinderListener {

    override val hideCheckBox: Boolean
        get() = true

    override val galleryFragmentId: Int
        get() = R.id.preWeChatFragment

    private val selectAdapter: WeChatPrevSelectAdapter = WeChatPrevSelectAdapter(this)
    private val isPrev: Boolean by lazy { prevOption.getBooleanExpand(WeChatUiResult.GALLERY_WE_CHAT_RESULT_PREV_IMAGE) }
    private val idList: ArrayList<Long> = ArrayList()

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(WeChatUiResult.GALLERY_WE_CHAT_RESULT_PREV_IDS, idList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        obtain(uiBundle)
        idList.clear()
        @Suppress("UNCHECKED_CAST")
        idList.addAll(savedInstanceState.getSerializableOrDefault(WeChatUiResult.GALLERY_WE_CHAT_RESULT_PREV_IDS, ArrayList<Long>()) as ArrayList<Long>)
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
        selectAdapter.updateSelect(selectList)
    }

    override fun onGalleryAdapterItemClick(view: View, position: Int, item: ScanEntity) {
        prevFragment.setCurrentItem(prevFragment.allItem.indexOfFirst { it.id == item.id })
    }

    override fun onGalleryFinderThumbnails(finderEntity: ScanEntity, container: FrameLayout) {
        container.displayGalleryPrevSelect(finderEntity, idList, isPrev)
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
        galleryPrevList.scrollToPosition(selectAdapter.findPosition(currentItem))
    }

    override fun onPrevViewCreated(savedInstanceState: Bundle?) {
        prevWeChatToolbarText.text = (prevFragment.currentPosition + 1).toString() + "/" + prevFragment.itemCount
    }

    override fun onChangedCheckBox() {
        val currentItem = prevFragment.currentItem
        prevWeChatToolbarSend.text = uiBundle.selectText + if (prevFragment.selectEmpty) "" else "(${prevFragment.selectCount}/${galleryBundle.multipleMaxCount})"
        if (isPrev) {
            if (!currentItem.isCheck) {
                idList.add(currentItem.id)
            } else {
                idList.remove(currentItem.id)
            }
            selectAdapter.refreshItem(currentItem)
        } else {
            galleryPrevList.visibility = if (prevFragment.selectEmpty) View.GONE else View.VISIBLE
            galleryPrevListLine.visibility = if (prevFragment.selectEmpty) View.GONE else View.VISIBLE
            selectAdapter.updateSelect(prevFragment.selectEntities)
            if (currentItem.isCheck) {
                selectAdapter.addSelect(currentItem)
            }
        }
    }

    override fun onClickCheckBoxMaxCount(context: Context?, galleryBundle: GalleryBundle, scanEntity: ScanEntity) {
        super.onClickCheckBoxMaxCount(context, galleryBundle, scanEntity)
        prevWeChatSelect.isChecked = false
    }

    override fun onClickCheckBoxFileNotExist(context: Context?, galleryBundle: GalleryBundle, scanEntity: ScanEntity) {
        super.onClickCheckBoxFileNotExist(context, galleryBundle, scanEntity)
        prevWeChatSelect.isChecked = false
    }

    override fun onKeyBackResult(bundle: Bundle) {
        bundle.putBoolean(WeChatUiResult.GALLERY_WE_CHAT_RESULT_FULL_IMAGE, prevWeChatFullImage.isChecked)
    }

    override fun onToolbarFinishResult(bundle: Bundle) {
        bundle.putBoolean(WeChatUiResult.GALLERY_WE_CHAT_RESULT_FULL_IMAGE, prevWeChatFullImage.isChecked)
    }
}