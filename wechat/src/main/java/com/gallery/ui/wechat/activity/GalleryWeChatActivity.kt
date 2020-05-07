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
import com.gallery.core.GalleryBundle
import com.gallery.core.ext.findFinder
import com.gallery.core.ext.isScanAll
import com.gallery.scan.ScanEntity
import com.gallery.ui.GalleryUiBundle
import com.gallery.ui.UIResult
import com.gallery.ui.activity.GalleryBaseActivity
import com.gallery.ui.adapter.GalleryFinderAdapter
import com.gallery.ui.wechat.*
import com.gallery.ui.wechat.adapter.WeChatFinderAdapter
import com.gallery.ui.wechat.util.*
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
            onStartPrevPage(
                    galleryFragment.selectEntities,
                    0,
                    Bundle().apply {
                        putBoolean(WeChatUiResult.GALLERY_WE_CHAT_RESULT_FULL_IMAGE, galleryWeChatFullImage.isChecked)
                    },
                    GalleryWeChatPrevActivity::class.java
            )
        }
        galleryWeChatToolbarSend.setOnClickListener { onGalleryResources(galleryFragment.selectEntities) }
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

    override fun onPrevKeyBack(bundle: Bundle) {
        galleryWeChatFullImage.isChecked = bundle.getBooleanExpand(WeChatUiResult.GALLERY_WE_CHAT_RESULT_FULL_IMAGE)
        updateView()
    }

    override fun onPrevToolbarFinish(bundle: Bundle) {
        galleryWeChatFullImage.isChecked = bundle.getBooleanExpand(WeChatUiResult.GALLERY_WE_CHAT_RESULT_FULL_IMAGE)
        updateView()
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
        onStartPrevPage(galleryFragment.currentEntities,
                if (parentId.isScanAll() && !galleryBundle.hideCamera) position - 1 else position,
                Bundle().apply {
                    putBoolean(WeChatUiResult.GALLERY_WE_CHAT_RESULT_FULL_IMAGE, galleryWeChatFullImage.isChecked)
                },
                GalleryWeChatPrevActivity::class.java)
    }

    override fun onClickCheckBoxFileNotExist(context: Context, galleryBundle: GalleryBundle, scanEntity: ScanEntity) {
        // 文件被删后主动更新了选中文件,应更新checkBox count
        galleryFragment.notifyDataSetChanged()
    }

    override fun onChangedCheckBox(position: Int, isSelect: Boolean, galleryBundle: GalleryBundle, scanEntity: ScanEntity) {
        val selectEntities = galleryFragment.selectEntities
        if (scanEntity.isVideo() && scanEntity.duration > 300000) {
            scanEntity.isCheck = false
            selectEntities.remove(scanEntity)
            getString(R.string.gallery_select_video_max_length).toastExpand(this)
        } else if (scanEntity.isVideo() && scanEntity.duration <= 0) {
            scanEntity.isCheck = false
            selectEntities.remove(scanEntity)
            getString(R.string.gallery_select_video_error).toastExpand(this)
        } else {
            updateView()
        }
        galleryFragment.notifyItemChanged(position)
        if (!scanEntity.isCheck) {
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
        galleryWeChatToolbarSend.text = galleryUiBundle.selectText + if (galleryFragment.selectEmpty) "" else "(${galleryFragment.selectCount}/${galleryBundle.multipleMaxCount})"
        galleryWeChatPrev.text = galleryUiBundle.preViewText + if (galleryFragment.selectEmpty) "" else "(${galleryFragment.selectCount})"
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
        bundle.putParcelableArrayList(UIResult.GALLERY_RESULT_ENTITIES, entities)
        bundle.putBoolean(WeChatUiResult.GALLERY_WE_CHAT_RESULT_FULL_IMAGE, galleryWeChatFullImage.isChecked)
        intent.putExtras(bundle)
        setResult(UIResult.GALLERY_RESULT_RESOURCES, intent)
        finish()
    }

    /**
     * 扫描到的文件目录为空
     */
    private fun onGalleryFinderEmpty() {
        getString(R.string.gallery_finder_empty).toastExpand(this)
    }

}