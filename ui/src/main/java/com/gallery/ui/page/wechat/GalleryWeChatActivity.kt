package com.gallery.ui.page.wechat

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.kotlin.expand.text.toastExpand
import com.gallery.core.GalleryBundle
import com.gallery.core.ext.findFinder
import com.gallery.core.ext.isScanAll
import com.gallery.scan.ScanEntity
import com.gallery.ui.GalleryUiBundle
import com.gallery.ui.R
import com.gallery.ui.activity.GalleryBaseActivity
import com.gallery.ui.adapter.GalleryFinderAdapter
import com.gallery.ui.adapter.WeChatFinderAdapter
import com.gallery.ui.obtain
import com.gallery.ui.util.*
import kotlinx.android.synthetic.main.gallery_activity_wechat_gallery.*

class GalleryWeChatActivity : GalleryBaseActivity(R.layout.gallery_activity_wechat_gallery), GalleryFinderAdapter.AdapterFinderListener, WeChatFinderAdapter.WeChatAdapterListener {

    private val newFinderAdapter: WeChatFinderAdapter by lazy { WeChatFinderAdapter(galleryUiBundle, this, this) }

    private var rootViewHeight: Int = 0
    private var selectScanEntity: ScanEntity? = null

    override val currentFinderId: Long
        get() = galleryFragment.parentId

    override val adapterGalleryUiBundle: GalleryUiBundle
        get() = galleryUiBundle

    override val currentFinderName: String
        get() = galleryWeChatToolbarFinderText.text.toString()

    override val galleryFragmentId: Int
        get() = R.id.galleryWeChatFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        obtain(galleryUiBundle)
        galleryWeChatRoot.addOnPreDrawListener { rootViewHeight = galleryWeChatRoot.height }
        galleryWeChatToolbarBack.setOnClickListener { onGalleryFinish() }
        galleryWeChatFinderRoot.setOnClickListener { hideFinderActionView() }
        galleryWeChatFinder.adapter = newFinderAdapter
        galleryWeChatPrev.setOnClickListener {
        }
        galleryWeChatToolbarSend.setOnClickListener {
        }
        galleryWeChatToolbarFinder.setOnClickListener {
            if (galleryFragment.parentId.isScanAll()) {
                finderList.clear()
                finderList.addAll(galleryFragment.currentEntities.findFinder(
                        galleryBundle.sdName,
                        galleryBundle.allName
                ))
            }
            if (finderList.isNullOrEmpty()) {
                onGalleryFinderEmpty()
                return@setOnClickListener
            }
            newFinderAdapter.updateFinder(finderList)
            if (galleryWeChatToolbarFinderIcon.animation == null || galleryWeChatToolbarFinderIcon.animation == rotateAnimationResult) {
                showFinderActionView()
            } else {
                hideFinderActionView()
            }
        }
        rotateAnimation.doOnAnimationEnd {
            AnimUtils.newInstance(rootViewHeight).openAnim(galleryWeChatFinderRoot) {}
        }
        rotateAnimationResult.doOnAnimationEnd {
            AnimUtils.newInstance(rootViewHeight).closeAnimate(galleryWeChatFinderRoot) {
                selectScanEntity?.let {
                    if (it.parent == galleryFragment.parentId) {
                        return@closeAnimate
                    }
                    galleryWeChatToolbarFinderText.text = it.bucketDisplayName
                    galleryFragment.onScanGallery(it.parent, result = false)
                    galleryWeChatFinderRoot.visibility = View.GONE
                    selectScanEntity = null
                }
            }
        }
    }

    override fun onGalleryAdapterItemClick(view: View, position: Int, item: ScanEntity) {
        this.selectScanEntity = item
        hideFinderActionView()
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
    }

    @SuppressLint("SetTextI18n")
    override fun onChangedCheckBox(position: Int, isSelect: Boolean, galleryBundle: GalleryBundle, scanEntity: ScanEntity) {
        if (scanEntity.isVideo() && scanEntity.duration > 300000) {
            scanEntity.isCheck = false
            galleryFragment.selectEntities.remove(scanEntity)
            getString(R.string.gallery_select_video_max_length).toastExpand(this)
        } else if (scanEntity.isVideo() && scanEntity.duration <= 0) {
            scanEntity.isCheck = false
            galleryFragment.selectEntities.remove(scanEntity)
            getString(R.string.gallery_select_video_error).toastExpand(this)
        } else {
            galleryWeChatToolbarSend.isEnabled = !galleryFragment.selectEmpty
            galleryWeChatPrev.isEnabled = !galleryFragment.selectEmpty
            galleryWeChatToolbarSend.text = galleryUiBundle.selectText + if (galleryFragment.selectEmpty) "" else "(${galleryFragment.selectCount}/${galleryBundle.multipleMaxCount})"
            galleryWeChatPrev.text = galleryUiBundle.preViewText + if (galleryFragment.selectEmpty) "" else "(${galleryFragment.selectCount})"
        }
        //这里可以进行优化,找到选中item对应adapter的position,刷新局部即可
        galleryFragment.notifyDataSetChanged()
    }

    private fun showFinderActionView() {
        galleryWeChatToolbarFinderIcon.clearAnimation()
        galleryWeChatToolbarFinderIcon.startAnimation(rotateAnimation)
    }

    private fun hideFinderActionView() {
        galleryWeChatToolbarFinderIcon.clearAnimation()
        galleryWeChatToolbarFinderIcon.startAnimation(rotateAnimationResult)
    }

    /**
     * 扫描到的文件目录为空
     */
    private fun onGalleryFinderEmpty() {
        getString(R.string.gallery_finder_empty).toastExpand(this)
    }

}