package com.gallery.ui.wechat.activity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.kotlin.expand.os.getBooleanExpand
import androidx.kotlin.expand.os.getSerializableOrDefault
import com.gallery.core.GalleryBundle
import com.gallery.core.delegate.prevFragment
import com.gallery.scan.ScanEntity
import com.gallery.scan.types.isGifExpand
import com.gallery.scan.types.isVideoExpand
import com.gallery.ui.activity.PrevBaseActivity
import com.gallery.ui.adapter.GalleryFinderAdapter
import com.gallery.ui.wechat.R
import com.gallery.ui.wechat.WeChatUiResult
import com.gallery.ui.wechat.adapter.WeChatPrevSelectAdapter
import com.gallery.ui.wechat.engine.displayGalleryPrev
import com.gallery.ui.wechat.engine.displayGalleryPrevSelect
import com.gallery.ui.wechat.obtain
import kotlinx.android.synthetic.main.gallery_activity_wechat_prev.*

@SuppressLint("SetTextI18n")
class GalleryWeChatPrevActivity : PrevBaseActivity(R.layout.gallery_activity_wechat_prev), GalleryFinderAdapter.AdapterFinderListener {

    override val galleryFragmentId: Int
        get() = R.id.preWeChatFragment

    private val selectAdapter: WeChatPrevSelectAdapter = WeChatPrevSelectAdapter(this)
    private val isPrev: Boolean by lazy { uiGapConfig.getBooleanExpand(WeChatUiResult.GALLERY_WE_CHAT_RESULT_PREV_IMAGE) }
    private val videoDuration: Int by lazy { uiGapConfig.getInt(WeChatUiResult.GALLERY_WE_CHAT_VIDEO_DURATION) }
    private val fullImageSelect: Boolean by lazy { uiGapConfig.getBooleanExpand(WeChatUiResult.GALLERY_WE_CHAT_RESULT_FULL_IMAGE) }
    private val idList: ArrayList<Long> = ArrayList()

    private fun onUpdateVideoTip(scanEntity: ScanEntity) {
        if (!scanEntity.isVideoExpand) {
            galleryPrevVideoTip.visibility = View.GONE
            prevWeChatToolbarSend.isEnabled = true
            prevWeChatSelect.isEnabled = true
            prevWeChatSelect.setTextColor(ContextCompat.getColor(this, android.R.color.white))
            return
        }
        if (scanEntity.duration > videoDuration) {
            galleryPrevVideoTip.visibility = View.VISIBLE
            galleryPrevVideoTip.text = getString(R.string.gallery_select_video_prev_error).format(videoDuration / 1000 / 60)
            prevWeChatSelect.setTextColor(ContextCompat.getColor(this, R.color.color_999999))
            prevWeChatToolbarSend.isEnabled = false
            prevWeChatSelect.isEnabled = false
        } else {
            prevWeChatSelect.isEnabled = true
            prevWeChatToolbarSend.isEnabled = true
            prevWeChatSelect.setTextColor(ContextCompat.getColor(this, android.R.color.white))
            galleryPrevVideoTip.visibility = View.GONE
            galleryPrevVideoTip.text = ""
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(WeChatUiResult.GALLERY_WE_CHAT_RESULT_PREV_IDS, idList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        obtain(uiConfig)
        idList.clear()
        @Suppress("UNCHECKED_CAST")
        idList.addAll(savedInstanceState.getSerializableOrDefault(WeChatUiResult.GALLERY_WE_CHAT_RESULT_PREV_IDS, ArrayList<Long>()) as ArrayList<Long>)
        prevWeChatToolbarBack.setOnClickListener { onGalleryFinish() }
        prevWeChatToolbarText.text = "%s / %s".format(0, galleryConfig.multipleMaxCount)
        prevWeChatFullImage.isChecked = fullImageSelect
        galleryPrevList.adapter = selectAdapter
        prevWeChatToolbarSend.isEnabled = true
        prevWeChatToolbarSend.setOnClickListener {
            if (prevFragment.selectEmpty) {
                prevFragment.selectEntities.add(prevFragment.currentItem)
                prevFragment.selectEntities.forEach { it.isSelected = true }
            }
            onGallerySelectEntities()
        }
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
        onUpdateVideoTip(prevFragment.currentItem)
        prevWeChatToolbarText.text = (position + 1).toString() + "/" + prevFragment.itemCount
        prevWeChatSelect.isChecked = prevFragment.isCheckBox(position)
        prevWeChatFullImage.visibility = if (currentItem.isGifExpand || currentItem.isVideoExpand) View.GONE else View.VISIBLE
        selectAdapter.refreshItem(currentItem)
        galleryPrevList.scrollToPosition(selectAdapter.findPosition(currentItem))
    }

    override fun onPrevViewCreated(savedInstanceState: Bundle?) {
        prevWeChatToolbarText.text = (prevFragment.currentPosition + 1).toString() + "/" + prevFragment.itemCount
        prevWeChatToolbarSend.text = uiConfig.selectText + if (prevFragment.selectEmpty) "" else "(${prevFragment.selectCount}/${galleryConfig.multipleMaxCount})"
        prevWeChatSelect.setOnClickListener { prevFragment.checkBoxClick(it) }
        galleryPrevList.visibility = if (prevFragment.selectEntities.isEmpty()) View.GONE else View.VISIBLE
        galleryPrevListLine.visibility = if (prevFragment.selectEntities.isEmpty()) View.GONE else View.VISIBLE
        selectAdapter.updateSelect(prevFragment.selectEntities)
        onUpdateVideoTip(prevFragment.currentItem)
        prevFragment.view?.findViewById<View>(R.id.preCheckBox)?.visibility = View.GONE
    }

    override fun onChangedCheckBox() {
        val currentItem = prevFragment.currentItem
        prevWeChatToolbarSend.text = uiConfig.selectText + if (prevFragment.selectEmpty) "" else "(${prevFragment.selectCount}/${galleryConfig.multipleMaxCount})"
        if (isPrev) {
            if (!currentItem.isSelected) {
                idList.add(currentItem.id)
            } else {
                idList.remove(currentItem.id)
            }
            selectAdapter.refreshItem(currentItem)
        } else {
            galleryPrevList.visibility = if (prevFragment.selectEmpty) View.GONE else View.VISIBLE
            galleryPrevListLine.visibility = if (prevFragment.selectEmpty) View.GONE else View.VISIBLE
            selectAdapter.updateSelect(prevFragment.selectEntities)
            if (currentItem.isSelected) {
                selectAdapter.addSelect(currentItem)
            }
        }
    }

    override fun onClickCheckBoxMaxCount(context: Context, galleryBundle: GalleryBundle, scanEntity: ScanEntity) {
        super.onClickCheckBoxMaxCount(context, galleryBundle, scanEntity)
        prevWeChatSelect.isChecked = false
    }

    override fun onClickCheckBoxFileNotExist(context: Context, galleryBundle: GalleryBundle, scanEntity: ScanEntity) {
        super.onClickCheckBoxFileNotExist(context, galleryBundle, scanEntity)
        prevWeChatSelect.isChecked = false
    }

    override fun onKeyBackResult(bundle: Bundle): Bundle {
        bundle.putBoolean(WeChatUiResult.GALLERY_WE_CHAT_RESULT_FULL_IMAGE, prevWeChatFullImage.isChecked)
        return super.onSelectEntitiesResult(bundle)
    }

    override fun onToolbarFinishResult(bundle: Bundle): Bundle {
        bundle.putBoolean(WeChatUiResult.GALLERY_WE_CHAT_RESULT_FULL_IMAGE, prevWeChatFullImage.isChecked)
        return super.onSelectEntitiesResult(bundle)
    }

    override fun onSelectEntitiesResult(bundle: Bundle): Bundle {
        bundle.putBoolean(WeChatUiResult.GALLERY_WE_CHAT_RESULT_FULL_IMAGE, prevWeChatFullImage.isChecked)
        return super.onSelectEntitiesResult(bundle)
    }
}