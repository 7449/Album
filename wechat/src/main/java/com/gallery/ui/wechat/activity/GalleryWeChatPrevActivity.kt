package com.gallery.ui.wechat.activity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.gallery.core.GalleryBundle
import com.gallery.core.delegate.prevFragment
import com.gallery.scan.args.file.ScanFileEntity
import com.gallery.scan.args.file.isGifExpand
import com.gallery.scan.args.file.isVideoExpand
import com.gallery.ui.activity.PrevBaseActivity
import com.gallery.ui.adapter.GalleryFinderAdapter
import com.gallery.ui.wechat.*
import com.gallery.ui.wechat.WeChatPrevArgs.Companion.weChatPrevArgsOrDefault
import com.gallery.ui.wechat.WeChatPrevSaveArgs.Companion.putArgs
import com.gallery.ui.wechat.WeChatPrevSaveArgs.Companion.weChatPrevSaveArgs
import com.gallery.ui.wechat.adapter.WeChatPrevSelectAdapter
import com.gallery.ui.wechat.engine.displayGalleryPrev
import com.gallery.ui.wechat.engine.displayGalleryPrevSelect
import kotlinx.android.synthetic.main.gallery_activity_wechat_prev.*

@SuppressLint("SetTextI18n")
class GalleryWeChatPrevActivity : PrevBaseActivity(R.layout.gallery_activity_wechat_prev), GalleryFinderAdapter.AdapterFinderListener {

    override val galleryFragmentId: Int
        get() = R.id.preWeChatFragment

    private val selectAdapter: WeChatPrevSelectAdapter = WeChatPrevSelectAdapter(this)
    private val weChatPrevArgs: WeChatPrevArgs by lazy { uiGapConfig.weChatPrevArgsOrDefault }
    private val idList: ArrayList<Long> = ArrayList()

    private fun onUpdateVideoTip(scanEntity: ScanFileEntity) {
        if (!scanEntity.isVideoExpand) {
            galleryPrevVideoTip.visibility = View.GONE
            prevWeChatToolbarSend.isEnabled = true
            prevWeChatSelect.isEnabled = true
            prevWeChatSelect.setTextColor(ContextCompat.getColor(this, android.R.color.white))
            return
        }
        if (scanEntity.duration > weChatPrevArgs.videoDuration) {
            galleryPrevVideoTip.visibility = View.VISIBLE
            galleryPrevVideoTip.text = getString(R.string.gallery_select_video_prev_error).format(weChatPrevArgs.videoDuration / 1000 / 60)
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
        WeChatPrevSaveArgs(idList).putArgs(outState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        uiPrevArgs
        super.onCreate(savedInstanceState)
        obtain(uiConfig)
        idList.clear()
        idList.addAll(savedInstanceState?.weChatPrevSaveArgs?.ids ?: arrayListOf())
        prevWeChatToolbarBack.setOnClickListener { onGalleryFinish() }
        prevWeChatToolbarText.text = "%s / %s".format(0, galleryConfig.multipleMaxCount)
        prevWeChatFullImage.isChecked = weChatPrevArgs.fullImageSelect
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

    override fun onGalleryAdapterItemClick(view: View, position: Int, item: ScanFileEntity) {
        prevFragment.setCurrentItem(prevFragment.allItem.indexOfFirst { it.id == item.id })
    }

    override fun onGalleryFinderThumbnails(finderEntity: ScanFileEntity, container: FrameLayout) {
        container.displayGalleryPrevSelect(finderEntity, idList, weChatPrevArgs.isPrev)
    }

    override fun onDisplayGalleryPrev(scanFileEntity: ScanFileEntity, container: FrameLayout) {
        container.displayGalleryPrev(scanFileEntity)
    }

    override fun onPageSelected(position: Int) {
        val currentItem: ScanFileEntity = prevFragment.currentItem
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
        selectAdapter.updateSelect(if (weChatPrevArgs.isPrev) uiPrevArgs.prevArgs.selectList else prevFragment.selectEntities)
        onUpdateVideoTip(prevFragment.currentItem)
        prevFragment.view?.findViewById<View>(R.id.preCheckBox)?.visibility = View.GONE
    }

    override fun onChangedCheckBox() {
        val currentItem = prevFragment.currentItem
        prevWeChatToolbarSend.text = uiConfig.selectText + if (prevFragment.selectEmpty) "" else "(${prevFragment.selectCount}/${galleryConfig.multipleMaxCount})"
        if (weChatPrevArgs.isPrev) {
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

    override fun onClickCheckBoxMaxCount(context: Context, galleryBundle: GalleryBundle, scanFileEntity: ScanFileEntity) {
        super.onClickCheckBoxMaxCount(context, galleryBundle, scanFileEntity)
        prevWeChatSelect.isChecked = false
    }

    override fun onClickCheckBoxFileNotExist(context: Context, galleryBundle: GalleryBundle, scanFileEntity: ScanFileEntity) {
        super.onClickCheckBoxFileNotExist(context, galleryBundle, scanFileEntity)
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