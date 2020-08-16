package com.gallery.ui.wechat.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.kotlin.expand.os.getBooleanExpand
import androidx.kotlin.expand.text.toastExpand
import androidx.kotlin.expand.view.hideExpand
import androidx.kotlin.expand.view.showExpand
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gallery.core.GalleryBundle
import com.gallery.core.GalleryConfig
import com.gallery.core.expand.findFinder
import com.gallery.core.expand.isScanAll
import com.gallery.core.expand.isVideo
import com.gallery.scan.ScanEntity
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
    private val videoList: ArrayList<ScanEntity> = ArrayList()

    private var selectScanEntity: ScanEntity? = null

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
            onStartPrevPage(
                    GalleryConfig.DEFAULT_PARENT_ID,
                    0,
                    Bundle().apply {
                        putBoolean(WeChatUiResult.GALLERY_WE_CHAT_RESULT_FULL_IMAGE, galleryWeChatFullImage.isChecked)
                        putBoolean(WeChatUiResult.GALLERY_WE_CHAT_RESULT_PREV_IMAGE, true)
                    },
                    GalleryWeChatPrevActivity::class.java
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
        rotateAnimation.doOnAnimationEnd {
            AnimUtils.newInstance(galleryWeChatRoot.height).openAnim(galleryWeChatFinderRoot) {}
        }
        rotateAnimationResult.doOnAnimationEnd {
            AnimUtils.newInstance(galleryWeChatRoot.height).closeAnimate(galleryWeChatFinderRoot) {
                selectScanEntity?.let {
                    if (it.parent == galleryFragment.parentId) {
                        return@closeAnimate
                    }
                    galleryWeChatToolbarFinderText.text = it.bucketDisplayName
                    if (it.parent == WeChatUiResult.GALLERY_WE_CHAT_ALL_VIDEO_PARENT) {
                        galleryFragment.scanSuccess(videoList)
                    } else {
                        galleryFragment.onScanGallery(it.parent)
                    }
                    selectScanEntity = null
                }
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
                galleryFragment.currentEntities[position].let {
                    galleryWeChatTime.text = it.dataModified.formatTime()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                galleryWeChatTime.postDelayed({ galleryWeChatTime.hideExpand() }, 1000)
            }
        })
    }

    override fun onScanSuccess(scanEntities: ArrayList<ScanEntity>) {
        if (galleryFragment.parentId.isScanAll() && scanEntities.isNotEmpty() && selectScanEntity?.parent != WeChatUiResult.GALLERY_WE_CHAT_ALL_VIDEO_PARENT) {
            videoList.clear()
            videoList.addAll(scanEntities.filter { it.isVideo() })
            finderList.clear()
            finderList.addAll(scanEntities.findFinder(
                    galleryBundle.sdName,
                    galleryBundle.allName
            ))
            scanEntities.find { it.isVideo() }?.let { it -> finderList.add(1, it.copy(parent = WeChatUiResult.GALLERY_WE_CHAT_ALL_VIDEO_PARENT, bucketDisplayName = "全部视频", count = scanEntities.count { it.isVideo() })) }
            finderList[0].isSelected = true
        }
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

    override fun onGalleryAdapterItemClick(view: View, position: Int, item: ScanEntity) {
        this.selectScanEntity = item
        hideFinderActionView()
        finderList.forEach { it.isSelected = it.parent == item.parent }
        newFinderAdapter.notifyDataSetChanged()
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

    override fun onPhotoItemClick(context: Context?, galleryBundle: GalleryBundle, scanEntity: ScanEntity, position: Int, parentId: Long) {
        onStartPrevPage(parentId,
                if (parentId.isScanAll() && !galleryBundle.hideCamera) position - 1 else position,
                Bundle().apply {
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
        if (scanEntity.isVideo() && scanEntity.duration > 300000) {
            scanEntity.isSelected = false
            selectEntities.remove(scanEntity)
            getString(R.string.gallery_select_video_max_length).toastExpand(this)
        } else if (scanEntity.isVideo() && scanEntity.duration <= 0) {
            scanEntity.isSelected = false
            selectEntities.remove(scanEntity)
            getString(R.string.gallery_select_video_error).toastExpand(this)
        } else {
            updateView()
        }
        galleryFragment.notifyItemChanged(position)
        if (!scanEntity.isSelected) {
            selectEntities.forEach { it ->
                galleryFragment.currentEntities.indexOf(it).let {
                    if (it != -1) {
                        galleryFragment.notifyItemChanged(it)
                    }
                }
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
        getString(R.string.gallery_finder_empty).toastExpand(this)
    }

}