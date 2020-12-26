package com.gallery.ui.wechat.activity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.gallery.compat.activity.PrevCompatActivity
import com.gallery.compat.activity.prevFragment
import com.gallery.compat.finder.GalleryFinderAdapter
import com.gallery.core.GalleryBundle
import com.gallery.core.entity.ScanEntity
import com.gallery.core.extensions.hasLExpand
import com.gallery.core.extensions.statusBarColorExpand
import com.gallery.ui.wechat.R
import com.gallery.ui.wechat.WeChatConfig
import com.gallery.ui.wechat.WeChatPrevArgs
import com.gallery.ui.wechat.WeChatPrevArgs.Companion.weChatPrevArgsOrDefault
import com.gallery.ui.wechat.WeChatPrevSaveArgs
import com.gallery.ui.wechat.WeChatPrevSaveArgs.Companion.putArgs
import com.gallery.ui.wechat.WeChatPrevSaveArgs.Companion.weChatPrevSaveArgs
import com.gallery.ui.wechat.adapter.WeChatPrevSelectAdapter
import com.gallery.ui.wechat.databinding.GalleryActivityWechatPrevBinding
import com.gallery.ui.wechat.extension.displayGalleryPrev
import com.gallery.ui.wechat.extension.displayGalleryPrevSelect

@SuppressLint("SetTextI18n")
class GalleryWeChatPrevActivity : PrevCompatActivity(), GalleryFinderAdapter.AdapterFinderListener {

    override val galleryFragmentId: Int
        get() = R.id.preWeChatFragment

    private val selectAdapter: WeChatPrevSelectAdapter = WeChatPrevSelectAdapter(this)
    private val weChatPrevArgs: WeChatPrevArgs by lazy { uiGapConfig.weChatPrevArgsOrDefault }
    private val idList: ArrayList<Long> = arrayListOf()
    private val viewBinding: GalleryActivityWechatPrevBinding by lazy { GalleryActivityWechatPrevBinding.inflate(layoutInflater) }

    private fun onUpdateVideoTip(scanEntity: ScanEntity) {
        if (!scanEntity.isVideo) {
            viewBinding.galleryPrevVideoTip.visibility = View.GONE
            viewBinding.prevWeChatToolbarSend.isEnabled = true
            viewBinding.prevWeChatSelect.isEnabled = true
            viewBinding.prevWeChatSelect.setTextColor(ContextCompat.getColor(this, android.R.color.white))
            return
        }
        if (scanEntity.duration > weChatPrevArgs.videoDuration) {
            viewBinding.galleryPrevVideoTip.visibility = View.VISIBLE
            viewBinding.galleryPrevVideoTip.text = getString(R.string.gallery_select_video_prev_error).format(weChatPrevArgs.videoDuration / 1000 / 60)
            viewBinding.prevWeChatSelect.setTextColor(ContextCompat.getColor(this, R.color.color_999999))
            viewBinding.prevWeChatToolbarSend.isEnabled = false
            viewBinding.prevWeChatSelect.isEnabled = false
        } else {
            viewBinding.prevWeChatSelect.isEnabled = true
            viewBinding.prevWeChatToolbarSend.isEnabled = true
            viewBinding.prevWeChatSelect.setTextColor(ContextCompat.getColor(this, android.R.color.white))
            viewBinding.galleryPrevVideoTip.visibility = View.GONE
            viewBinding.galleryPrevVideoTip.text = ""
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        WeChatPrevSaveArgs(idList).putArgs(outState)
    }

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        window.statusBarColorExpand(uiConfig.statusBarColor)
        if (hasLExpand()) {
            window.statusBarColor = uiConfig.statusBarColor
        }
        viewBinding.prevWeChatToolbar.setBackgroundColor(uiConfig.toolbarBackground)

        viewBinding.prevWeChatBottomView.setBackgroundColor(uiConfig.preBottomViewBackground)
        viewBinding.galleryPrevList.setBackgroundColor(uiConfig.preBottomViewBackground)
        viewBinding.galleryPrevList.alpha = 0.9.toFloat()
        viewBinding.prevWeChatSelect.text = uiConfig.preBottomOkText
        viewBinding.prevWeChatSelect.textSize = uiConfig.preBottomOkTextSize
        viewBinding.prevWeChatSelect.setTextColor(uiConfig.preBottomOkTextColor)

        viewBinding.prevWeChatFullImage.setButtonDrawable(R.drawable.wechat_selector_gallery_full_image_item_check)
        viewBinding.prevWeChatSelect.setButtonDrawable(R.drawable.wechat_selector_gallery_full_image_item_check)

        viewBinding.prevWeChatToolbarSend.textSize = uiConfig.selectTextSize
        viewBinding.prevWeChatToolbarSend.text = uiConfig.selectText

        idList.clear()
        idList.addAll(savedInstanceState?.weChatPrevSaveArgs?.ids ?: arrayListOf())
        viewBinding.prevWeChatToolbarBack.setOnClickListener { onGalleryFinish() }
        viewBinding.prevWeChatToolbarText.text = "%s / %s".format(0, galleryConfig.multipleMaxCount)
        viewBinding.prevWeChatFullImage.isChecked = weChatPrevArgs.fullImageSelect
        viewBinding.galleryPrevList.layoutManager = LinearLayoutManager(this)
        viewBinding.galleryPrevList.adapter = selectAdapter
        viewBinding.prevWeChatToolbarSend.isEnabled = true
        viewBinding.prevWeChatToolbarSend.setOnClickListener {
            val fragment = prevFragment
            if (fragment.selectEmpty) {
                fragment.selectEntities.add(fragment.currentItem)
                fragment.selectEntities.forEach { it.isSelected = true }
            }
            onGallerySelectEntities()
        }
    }

    override fun onGalleryAdapterItemClick(view: View, position: Int, item: ScanEntity) {
        val fragment = prevFragment
        fragment.setCurrentItem(fragment.allItem.indexOfFirst { it.id == item.id })
    }

    override fun onGalleryFinderThumbnails(finderEntity: ScanEntity, container: FrameLayout) {
        container.displayGalleryPrevSelect(finderEntity, idList, weChatPrevArgs.isPrev)
    }

    override fun onDisplayGalleryPrev(scanEntity: ScanEntity, container: FrameLayout) {
        container.displayGalleryPrev(scanEntity)
    }

    override fun onPageSelected(position: Int) {
        val fragment = prevFragment
        val currentItem: ScanEntity = fragment.currentItem
        onUpdateVideoTip(fragment.currentItem)
        viewBinding.prevWeChatToolbarText.text = (position + 1).toString() + "/" + fragment.itemCount
        viewBinding.prevWeChatSelect.isChecked = fragment.isCheckBox(position)
        viewBinding.prevWeChatFullImage.visibility = if (currentItem.isGif || currentItem.isVideo) View.GONE else View.VISIBLE
        selectAdapter.refreshItem(currentItem)
        viewBinding.galleryPrevList.scrollToPosition(selectAdapter.findPosition(currentItem))
    }

    override fun onPrevCreated(fragment: Fragment, galleryBundle: GalleryBundle, savedInstanceState: Bundle?) {
        super.onPrevCreated(fragment, galleryBundle, savedInstanceState)
        val prevFragment = prevFragment
        viewBinding.prevWeChatToolbarText.text = (prevFragment.currentPosition + 1).toString() + "/" + prevFragment.itemCount
        viewBinding.prevWeChatToolbarSend.text = uiConfig.selectText + if (prevFragment.selectEmpty) "" else "(${prevFragment.selectCount}/${galleryConfig.multipleMaxCount})"
        viewBinding.prevWeChatSelect.setOnClickListener { prevFragment.checkBoxClick(it) }
        viewBinding.galleryPrevList.visibility = if (prevFragment.selectEntities.isEmpty()) View.GONE else View.VISIBLE
        viewBinding.galleryPrevListLine.visibility = if (prevFragment.selectEntities.isEmpty()) View.GONE else View.VISIBLE
        selectAdapter.updateSelect(if (weChatPrevArgs.isPrev) uiPrevArgs.prevArgs.selectList else prevFragment.selectEntities)
        onUpdateVideoTip(prevFragment.currentItem)
        prevFragment.view?.findViewById<View>(R.id.gallery_prev_checkbox)?.visibility = View.GONE
    }

    override fun onChangedCheckBox() {
        val fragment = prevFragment
        val currentItem = fragment.currentItem
        viewBinding.prevWeChatToolbarSend.text = uiConfig.selectText + if (fragment.selectEmpty) "" else "(${fragment.selectCount}/${galleryConfig.multipleMaxCount})"
        if (weChatPrevArgs.isPrev) {
            if (!currentItem.isSelected) {
                idList.add(currentItem.id)
            } else {
                idList.remove(currentItem.id)
            }
            selectAdapter.refreshItem(currentItem)
        } else {
            viewBinding.galleryPrevList.visibility = if (fragment.selectEmpty) View.GONE else View.VISIBLE
            viewBinding.galleryPrevListLine.visibility = if (fragment.selectEmpty) View.GONE else View.VISIBLE
            selectAdapter.updateSelect(fragment.selectEntities)
            if (currentItem.isSelected) {
                selectAdapter.addSelect(currentItem)
            }
        }
    }

    override fun onClickCheckBoxMaxCount(context: Context, galleryBundle: GalleryBundle, scanEntity: ScanEntity) {
        super.onClickCheckBoxMaxCount(context, galleryBundle, scanEntity)
        viewBinding.prevWeChatSelect.isChecked = false
    }

    override fun onClickCheckBoxFileNotExist(context: Context, galleryBundle: GalleryBundle, scanEntity: ScanEntity) {
        super.onClickCheckBoxFileNotExist(context, galleryBundle, scanEntity)
        viewBinding.prevWeChatSelect.isChecked = false
    }

    override fun onKeyBackResult(bundle: Bundle): Bundle {
        bundle.putBoolean(WeChatConfig.GALLERY_WE_CHAT_RESULT_FULL_IMAGE, viewBinding.prevWeChatFullImage.isChecked)
        return super.onSelectEntitiesResult(bundle)
    }

    override fun onToolbarFinishResult(bundle: Bundle): Bundle {
        bundle.putBoolean(WeChatConfig.GALLERY_WE_CHAT_RESULT_FULL_IMAGE, viewBinding.prevWeChatFullImage.isChecked)
        return super.onSelectEntitiesResult(bundle)
    }

    override fun onSelectEntitiesResult(bundle: Bundle): Bundle {
        bundle.putBoolean(WeChatConfig.GALLERY_WE_CHAT_RESULT_FULL_IMAGE, viewBinding.prevWeChatFullImage.isChecked)
        return super.onSelectEntitiesResult(bundle)
    }
}