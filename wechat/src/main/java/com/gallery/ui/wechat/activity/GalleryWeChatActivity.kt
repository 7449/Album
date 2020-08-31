package com.gallery.ui.wechat.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.kotlin.expand.os.getBooleanExpand
import androidx.kotlin.expand.view.hideExpand
import androidx.kotlin.expand.view.showExpand
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gallery.core.GalleryBundle
import com.gallery.core.delegate.galleryFragment
import com.gallery.core.expand.findFinder
import com.gallery.core.expand.safeToastExpand
import com.gallery.scan.ScanEntity
import com.gallery.scan.types.*
import com.gallery.ui.UIResult
import com.gallery.ui.activity.GalleryBaseActivity
import com.gallery.ui.adapter.GalleryFinderAdapter
import com.gallery.ui.wechat.*
import com.gallery.ui.wechat.adapter.WeChatFinderAdapter
import com.gallery.ui.wechat.util.*
import kotlinx.android.synthetic.main.gallery_activity_wechat_gallery.*

class GalleryWeChatActivity : GalleryBaseActivity(R.layout.gallery_activity_wechat_gallery), GalleryFinderAdapter.AdapterFinderListener,
        WeChatFinderAdapter.WeChatAdapterListener {

    private val newFinderAdapter: WeChatFinderAdapter by lazy { WeChatFinderAdapter(uiConfig, this, this) }
    private val videoDuration: Int by lazy { uiGalleryConfig.getInt(WeChatUiResult.GALLERY_WE_CHAT_VIDEO_DURATION, 300000) }
    private val videoDes: String by lazy { uiGalleryConfig.getString(WeChatUiResult.GALLERY_WE_CHAT_VIDEO_DES, "全部视频") }
    private val videoList: ArrayList<ScanEntity> = ArrayList()

    override val currentFinderId: Long
        get() = galleryFragment.parentId

    override val currentFinderName: String
        get() = galleryWeChatToolbarFinderText.text.toString()

    override val galleryFragmentId: Int
        get() = R.id.galleryWeChatFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        obtain(uiConfig)
        galleryWeChatToolbarBack.setOnClickListener { onGalleryFinish() }
        galleryWeChatFinderRoot.setOnClickListener { hideFinderActionView() }
        galleryWeChatFinder.adapter = newFinderAdapter
        galleryWeChatPrev.setOnClickListener {
            onStartPrevPage(SCAN_NONE, 0,
                    option = Bundle().apply {
                        putInt(WeChatUiResult.GALLERY_WE_CHAT_VIDEO_DURATION, videoDuration)
                        putBoolean(WeChatUiResult.GALLERY_WE_CHAT_RESULT_FULL_IMAGE, galleryWeChatFullImage.isChecked)
                        putBoolean(WeChatUiResult.GALLERY_WE_CHAT_RESULT_PREV_IMAGE, true)
                    },
                    cla = GalleryWeChatPrevActivity::class.java
            )
        }
        galleryWeChatToolbarSend.setOnClickListener { onGalleryResources(galleryFragment.selectEntities) }
        galleryWeChatToolbarFinder.setOnClickListener {
            if (finderList.isNullOrEmpty()) {
                onGalleryFinderEmpty()
                return@setOnClickListener
            }
            newFinderAdapter.updateFinder(finderList)
            galleryWeChatToolbarFinderIcon.animation?.let {
                if (it == rotateAnimationResult) {
                    showFinderActionView()
                } else {
                    hideFinderActionView()
                }
            } ?: showFinderActionView()
        }
        rotateAnimation.doOnAnimationEnd { AnimUtils.newInstance(galleryWeChatRoot.height).openAnim(galleryWeChatFinderRoot) }
        rotateAnimationResult.doOnAnimationEnd {
            AnimUtils.newInstance(galleryWeChatRoot.height).closeAnimate(galleryWeChatFinderRoot) {
                if (finderList.find { it.isSelected }?.parent == galleryFragment.parentId) {
                    return@closeAnimate
                }
                val find = finderList.find { it.parent == galleryFragment.parentId }
                galleryWeChatToolbarFinderText.text = find?.bucketDisplayName
                if (find?.parent == WeChatUiResult.GALLERY_WE_CHAT_ALL_VIDEO_PARENT) {
                    galleryFragment.parentId = WeChatUiResult.GALLERY_WE_CHAT_ALL_VIDEO_PARENT
                    galleryFragment.scanSuccess(videoList)
                } else {
                    galleryFragment.onScanGallery(find?.parent ?: SCAN_ALL)
                }
                finderList.forEach { it.isSelected = it.parent == galleryFragment.parentId }
                newFinderAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onGalleryViewCreated(savedInstanceState: Bundle?) {
        galleryFragment.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                recyclerView.layoutManager ?: return
                val layoutManager: GridLayoutManager = recyclerView.layoutManager as GridLayoutManager
                val position: Int = layoutManager.findFirstCompletelyVisibleItemPosition()
                if (dx == 0 && dy == 0) {
                    galleryWeChatTime.hideExpand()
                } else {
                    galleryWeChatTime.showExpand()
                }
                if (galleryFragment.currentEntities.isNotEmpty()) {
                    galleryFragment.currentEntities[if (position < 0) 0 else position].let { galleryWeChatTime.text = it.dateModified.formatTime() }
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                galleryWeChatTime.postDelayed({ galleryWeChatTime.hideExpand() }, 1000)
            }
        })
    }

    override fun onScanSuccess(scanEntities: ArrayList<ScanEntity>) {
        if (galleryFragment.parentId.isScanAllExpand() && scanEntities.isNotEmpty()) {
            videoList.clear()
            videoList.addAll(scanEntities.filter { it.isVideoExpand })
            finderList.clear()
            finderList.addAll(scanEntities.findFinder(galleryBundle.sdName, galleryBundle.allName))
            scanEntities.find { it.isVideoExpand }?.let { it ->
                finderList.add(1, it.copy(parent = WeChatUiResult.GALLERY_WE_CHAT_ALL_VIDEO_PARENT, bucketDisplayName = videoDes, count = videoList.size))
            }
            finderList.firstOrNull()?.isSelected = true
        }
    }

    override fun onGalleryAdapterItemClick(view: View, position: Int, item: ScanEntity) {
        galleryFragment.parentId = item.parent
        hideFinderActionView()
    }

    override fun onResultBack(bundle: Bundle) {
        galleryWeChatFullImage.isChecked = bundle.getBooleanExpand(WeChatUiResult.GALLERY_WE_CHAT_RESULT_FULL_IMAGE)
        updateView()
    }

    override fun onResultToolbar(bundle: Bundle) {
        onResultBack(bundle)
    }

    override fun onResultSelect(bundle: Bundle) {
        //https://github.com/7449/Album/issues/3
        galleryWeChatFullImage.isChecked = bundle.getBooleanExpand(WeChatUiResult.GALLERY_WE_CHAT_RESULT_FULL_IMAGE)
        super.onResultSelect(bundle)
    }

    override fun onGalleryFinderThumbnails(finderEntity: ScanEntity, container: FrameLayout) {
        onDisplayGalleryThumbnails(finderEntity, container)
    }

    override fun onDisplayGallery(width: Int, height: Int, galleryEntity: ScanEntity, container: FrameLayout, selectView: TextView) {
        container.displayGalleryWeChat(width, height, galleryFragment.selectEntities, galleryEntity, selectView)
    }

    override fun onDisplayGalleryThumbnails(finderEntity: ScanEntity, container: FrameLayout) {
        container.displayGalleryThumbnails(finderEntity)
    }

    override fun onPhotoItemClick(context: Context, galleryBundle: GalleryBundle, scanEntity: ScanEntity, position: Int, parentId: Long) {
        onStartPrevPage(if (parentId == WeChatUiResult.GALLERY_WE_CHAT_ALL_VIDEO_PARENT) SCAN_ALL else parentId,
                if (parentId.isScanAllExpand() && !galleryBundle.hideCamera) position - 1 else position,
                if (parentId == WeChatUiResult.GALLERY_WE_CHAT_ALL_VIDEO_PARENT) ScanType.VIDEO else ScanType.NONE,
                Bundle().apply {
                    putInt(WeChatUiResult.GALLERY_WE_CHAT_VIDEO_DURATION, videoDuration)
                    putBoolean(WeChatUiResult.GALLERY_WE_CHAT_RESULT_FULL_IMAGE, galleryWeChatFullImage.isChecked)
                    putBoolean(WeChatUiResult.GALLERY_WE_CHAT_RESULT_PREV_IMAGE, false)
                },
                GalleryWeChatPrevActivity::class.java)
    }

    override fun onClickCheckBoxFileNotExist(context: Context, galleryBundle: GalleryBundle, scanEntity: ScanEntity) {
        super.onClickCheckBoxFileNotExist(context, galleryBundle, scanEntity)
        galleryFragment.notifyDataSetChanged()
    }

    override fun onChangedCheckBox(position: Int, isSelect: Boolean, galleryBundle: GalleryBundle, scanEntity: ScanEntity) {
        val selectEntities = galleryFragment.selectEntities
        if (scanEntity.isVideoExpand && scanEntity.duration > videoDuration) {
            scanEntity.isSelected = false
            selectEntities.remove(scanEntity)
            getString(R.string.gallery_select_video_max_length).safeToastExpand(this)
        } else if (scanEntity.isVideoExpand && scanEntity.duration <= 0) {
            scanEntity.isSelected = false
            selectEntities.remove(scanEntity)
            getString(R.string.gallery_select_video_error).safeToastExpand(this)
        } else {
            updateView()
        }
        galleryFragment.notifyItemChanged(position)
        if (!scanEntity.isSelected) {
            galleryFragment.currentEntities.mapIndexedNotNull { index, item -> if (item.isSelected) index else null }.forEach {
                galleryFragment.notifyItemChanged(it)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateView() {
        galleryWeChatToolbarSend.isEnabled = !galleryFragment.selectEmpty
        galleryWeChatPrev.isEnabled = !galleryFragment.selectEmpty
        galleryWeChatToolbarSend.text = uiConfig.selectText + if (galleryFragment.selectEmpty) "" else "(${galleryFragment.selectCount}/${galleryBundle.multipleMaxCount})"
        galleryWeChatPrev.text = uiConfig.preViewText + if (galleryFragment.selectEmpty) "" else "(${galleryFragment.selectCount})"
    }

    private fun showFinderActionView() {
        galleryWeChatToolbarFinderIcon.clearAnimation()
        galleryWeChatToolbarFinderIcon.startAnimation(rotateAnimation)
    }

    private fun hideFinderActionView() {
        galleryWeChatToolbarFinderIcon.clearAnimation()
        galleryWeChatToolbarFinderIcon.startAnimation(rotateAnimationResult)
    }

    override fun onGalleryResources(entities: ArrayList<ScanEntity>) {
        val intent = Intent()
        val bundle = Bundle()
        bundle.putParcelableArrayList(UIResult.GALLERY_MULTIPLE_DATA, entities)
        bundle.putBoolean(WeChatUiResult.GALLERY_WE_CHAT_RESULT_FULL_IMAGE, galleryWeChatFullImage.isChecked)
        intent.putExtras(bundle)
        setResult(UIResult.GALLERY_MULTIPLE_RESULT_CODE, intent)
        finish()
    }

    /**
     * 扫描到的文件目录为空
     */
    private fun onGalleryFinderEmpty() {
        getString(R.string.gallery_finder_empty).safeToastExpand(this)
    }

}