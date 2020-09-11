package com.gallery.ui.wechat.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.kotlin.expand.os.getBooleanExpand
import androidx.kotlin.expand.text.safeToastExpand
import androidx.kotlin.expand.view.hideExpand
import androidx.kotlin.expand.view.showExpand
import androidx.kotlin.expand.widget.doOnAnimationEndExpand
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gallery.core.GalleryBundle
import com.gallery.core.delegate.galleryFragment
import com.gallery.core.expand.findFinder
import com.gallery.scan.args.ScanMinimumEntity
import com.gallery.scan.types.SCAN_ALL
import com.gallery.scan.types.SCAN_NONE
import com.gallery.scan.types.isScanAllExpand
import com.gallery.scan.types.isVideoExpand
import com.gallery.ui.UIResult
import com.gallery.ui.activity.GalleryBaseActivity
import com.gallery.ui.adapter.GalleryFinderAdapter
import com.gallery.ui.wechat.*
import com.gallery.ui.wechat.WeChatPrevArgs.Companion.putArgs
import com.gallery.ui.wechat.adapter.WeChatFinderAdapter
import com.gallery.ui.wechat.engine.AnimEngine
import com.gallery.ui.wechat.engine.displayGalleryThumbnails
import com.gallery.ui.wechat.engine.displayGalleryWeChat
import com.gallery.ui.wechat.engine.formatTime
import kotlinx.android.synthetic.main.gallery_activity_wechat_gallery.*

class GalleryWeChatActivity : GalleryBaseActivity(R.layout.gallery_activity_wechat_gallery), GalleryFinderAdapter.AdapterFinderListener {

    private val newFinderAdapter: WeChatFinderAdapter by lazy { WeChatFinderAdapter(uiConfig, this) }
    private val videoDuration: Int by lazy { uiGapConfig.getInt(WeChatUiResult.GALLERY_WE_CHAT_VIDEO_DURATION, 300000) }
    private val videoDes: String by lazy { uiGapConfig.getString(WeChatUiResult.GALLERY_WE_CHAT_VIDEO_DES, "全部视频") }
    private val videoList: ArrayList<ScanMinimumEntity> = ArrayList()
    private val tempVideoList: ArrayList<ScanMinimumEntity> = ArrayList()

    override val currentFinderName: String
        get() = galleryWeChatToolbarFinderText.text.toString()

    override val galleryFragmentId: Int
        get() = R.id.galleryWeChatFragment

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(WeChatUiResult.GALLERY_WE_CHAT_VIDEO_ALL, videoList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        obtain(uiConfig)
        tempVideoList.clear()
        tempVideoList.addAll(savedInstanceState?.getParcelableArrayList(WeChatUiResult.GALLERY_WE_CHAT_VIDEO_ALL) ?: arrayListOf())
        videoList.clear()
        videoList.addAll(ArrayList(tempVideoList))
        galleryWeChatToolbarBack.setOnClickListener { onGalleryFinish() }
        galleryWeChatFinderRoot.setOnClickListener { hideFinderActionView() }
        galleryWeChatFinder.adapter = newFinderAdapter
        galleryWeChatPrev.setOnClickListener {
            onStartPrevPage(SCAN_NONE, 0,
                    option = WeChatPrevArgs(true, videoDuration, galleryWeChatFullImage.isChecked).putArgs(Bundle()),
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
        rotateAnimation.doOnAnimationEndExpand { AnimEngine.newInstance(galleryWeChatRoot.height).openAnim(galleryWeChatFinderRoot) }
        rotateAnimationResult.doOnAnimationEndExpand {
            AnimEngine.newInstance(galleryWeChatRoot.height).closeAnimate(galleryWeChatFinderRoot) {
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

    override fun onScanSuccess(scanEntities: ArrayList<ScanMinimumEntity>) {
        if (galleryFragment.parentId.isScanAllExpand() && scanEntities.isNotEmpty()) {
            videoList.clear()
            videoList.addAll(scanEntities.filter { it.isVideoExpand })
            finderList.clear()
            finderList.addAll(scanEntities.findFinder(galleryConfig.sdName, galleryConfig.allName))
            scanEntities.find { it.isVideoExpand }?.let { it ->
                finderList.add(1, it.copy(parent = WeChatUiResult.GALLERY_WE_CHAT_ALL_VIDEO_PARENT, bucketDisplayName = videoDes, count = videoList.size))
            }
            finderList.firstOrNull()?.isSelected = true
        } else if (galleryFragment.parentId == WeChatUiResult.GALLERY_WE_CHAT_ALL_VIDEO_PARENT && tempVideoList.isNotEmpty()) {
            //这里判断下，parentId 如果等于视频parentId且tempVideoList不为空的情况下则是横竖屏切换的时候是全部视频，扫描的是 GALLERY_WE_CHAT_ALL_VIDEO_PARENT
            //数据肯定为空，这里再重新调用下 scanSuccess 赋值数据
            //为了避免重复调用的问题，tempVideoList需要clone后立马清空
            //延时是因为onScanSuccess是在赋值数据addAll之前调用，如果不延时，实际上最后得到的还是空的 scanEntities
            val arrayList = ArrayList(tempVideoList)
            tempVideoList.clear()
            galleryWeChatFullImage.postDelayed({ galleryFragment.scanSuccess(arrayList) }, 50)
        }
        //每次扫描成功之后需要更新下activity的UI
        updateView()
    }

    override fun onGalleryAdapterItemClick(view: View, position: Int, item: ScanMinimumEntity) {
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

    override fun onGalleryFinderThumbnails(finderEntity: ScanMinimumEntity, container: FrameLayout) {
        onDisplayGalleryThumbnails(finderEntity, container)
    }

    override fun onDisplayGallery(width: Int, height: Int, galleryEntity: ScanMinimumEntity, container: FrameLayout, selectView: TextView) {
        container.displayGalleryWeChat(width, height, galleryFragment.selectEntities, galleryEntity, selectView)
    }

    override fun onDisplayGalleryThumbnails(finderEntity: ScanMinimumEntity, container: FrameLayout) {
        container.displayGalleryThumbnails(finderEntity)
    }

    override fun onPhotoItemClick(context: Context, galleryBundle: GalleryBundle, scanEntity: ScanMinimumEntity, position: Int, parentId: Long) {
        onStartPrevPage(if (parentId == WeChatUiResult.GALLERY_WE_CHAT_ALL_VIDEO_PARENT) SCAN_ALL else parentId,
                if (parentId.isScanAllExpand() && !galleryBundle.hideCamera) position - 1 else position,
                if (parentId == WeChatUiResult.GALLERY_WE_CHAT_ALL_VIDEO_PARENT) MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO else MediaStore.Files.FileColumns.MEDIA_TYPE_NONE,
                WeChatPrevArgs(false, videoDuration, galleryWeChatFullImage.isChecked).putArgs(),
                GalleryWeChatPrevActivity::class.java)
    }

    override fun onClickCheckBoxFileNotExist(context: Context, galleryBundle: GalleryBundle, scanEntity: ScanMinimumEntity) {
        super.onClickCheckBoxFileNotExist(context, galleryBundle, scanEntity)
        galleryFragment.notifyDataSetChanged()
    }

    override fun onChangedCheckBox(position: Int, isSelect: Boolean, galleryBundle: GalleryBundle, scanEntity: ScanMinimumEntity) {
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
        val fragment = galleryFragment
        galleryWeChatToolbarSend.isEnabled = !fragment.selectEmpty
        galleryWeChatPrev.isEnabled = !fragment.selectEmpty
        galleryWeChatToolbarSend.text = uiConfig.selectText + if (fragment.selectEmpty) "" else "(${fragment.selectCount}/${galleryConfig.multipleMaxCount})"
        galleryWeChatPrev.text = uiConfig.preViewText + if (fragment.selectEmpty) "" else "(${fragment.selectCount})"
    }

    private fun showFinderActionView() {
        galleryWeChatToolbarFinderIcon.clearAnimation()
        galleryWeChatToolbarFinderIcon.startAnimation(rotateAnimation)
    }

    private fun hideFinderActionView() {
        galleryWeChatToolbarFinderIcon.clearAnimation()
        galleryWeChatToolbarFinderIcon.startAnimation(rotateAnimationResult)
    }

    override fun onGalleryResources(entities: ArrayList<ScanMinimumEntity>) {
        val intent = Intent()
        val bundle = Bundle()
        bundle.putParcelableArrayList(UIResult.GALLERY_MULTIPLE_DATA, entities)
        bundle.putBoolean(WeChatUiResult.GALLERY_WE_CHAT_RESULT_FULL_IMAGE, galleryWeChatFullImage.isChecked)
        intent.putExtras(bundle)
        setResult(UIResult.RESULT_CODE_MULTIPLE_DATA, intent)
        finish()
    }

    /**
     * 扫描到的文件目录为空
     */
    private fun onGalleryFinderEmpty() {
        getString(R.string.gallery_finder_empty).safeToastExpand(this)
    }

}