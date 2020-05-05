package com.gallery.ui.page.wechat

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.Animation
import android.widget.FrameLayout
import androidx.kotlin.expand.text.toastExpand
import androidx.kotlin.expand.view.statusBarColorExpand
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gallery.core.GalleryBundle
import com.gallery.core.ext.externalUri
import com.gallery.core.ext.findFinder
import com.gallery.core.ext.isScanAll
import com.gallery.core.ui.widget.GalleryImageView
import com.gallery.scan.ScanEntity
import com.gallery.ui.*
import com.gallery.ui.activity.GalleryBaseActivity
import com.gallery.ui.adapter.GalleryFinderAdapter
import com.gallery.ui.adapter.WeChatFinderAdapter
import kotlinx.android.synthetic.main.gallery_activity_wechat_gallery.*

class GalleryWeChatActivity : GalleryBaseActivity(R.layout.gallery_activity_wechat_gallery), GalleryFinderAdapter.AdapterFinderListener, WeChatFinderAdapter.WeChatAdapterListener {

    private var selectParent: Long = 0
    private var selectFinder: Boolean = false
    private var selectBucketDisplayName: String = ""

    private val requestOptions: RequestOptions = RequestOptions().placeholder(R.drawable.ic_gallery_default_loading).error(R.drawable.ic_gallery_default_loading).centerCrop()

    private val newFinderAdapter: WeChatFinderAdapter by lazy {
        return@lazy WeChatFinderAdapter(galleryUiBundle, this, this)
    }

    private var rootViewHeight: Int = 0

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
        window.statusBarColorExpand(galleryUiBundle.statusBarColor)

        galleryWeChatRoot.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                galleryWeChatRoot.viewTreeObserver.removeOnPreDrawListener(this)
                rootViewHeight = galleryWeChatRoot.height
                return true
            }
        })

        galleryWeChatToolbar.setBackgroundColor(galleryUiBundle.toolbarBackground)
        galleryWeChatToolbarBack.setOnClickListener { onGalleryFinish() }
        galleryWeChatFinderRoot.setOnClickListener { hideFinderActionView() }

        galleryWeChatFinder.layoutManager = LinearLayoutManager(this)
        galleryWeChatFinder.adapter = newFinderAdapter
        galleryWeChatFinder.setBackgroundColor(galleryUiBundle.finderItemBackground)
        galleryWeChatFinder.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL))

        galleryWeChatBottomView.setBackgroundColor(galleryUiBundle.bottomViewBackground)
        galleryWeChatFullImage.setButtonDrawable(R.drawable.wechat_selector_gallery_full_image_item_check)

        galleryWeChatPrev.text = galleryUiBundle.preViewText
        galleryWeChatPrev.textSize = galleryUiBundle.preViewTextSize
        galleryWeChatPrev.setOnClickListener {
        }

        galleryWeChatToolbarSend.textSize = galleryUiBundle.selectTextSize
        galleryWeChatToolbarSend.text = galleryUiBundle.selectText
        galleryWeChatToolbarSend.setOnClickListener {
        }

        galleryWeChatToolbarFinderText.textSize = galleryUiBundle.finderTextSize
        galleryWeChatToolbarFinderText.setTextColor(galleryUiBundle.finderTextColor)
        galleryWeChatToolbarFinderText.text = finderName
        galleryWeChatToolbarFinderIcon.setImageResource(galleryUiBundle.finderTextCompoundDrawable)
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
        rotateAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation) {
            }

            override fun onAnimationEnd(animation: Animation) {
                AnimUtils.newInstance(rootViewHeight).openAnim(galleryWeChatFinderRoot) {}
            }

            override fun onAnimationStart(animation: Animation) {
            }
        })
        rotateAnimationResult.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation) {
            }

            override fun onAnimationEnd(animation: Animation) {
                AnimUtils.newInstance(rootViewHeight).closeAnimate(galleryWeChatFinderRoot) {
                    selectFinder = false
                    if (selectParent == galleryFragment.parentId) {
                        return@closeAnimate
                    }
                    galleryWeChatToolbarFinderText.text = selectBucketDisplayName
                    galleryFragment.onScanGallery(selectParent, result = false)
                    galleryWeChatFinderRoot.visibility = View.GONE
                }
            }

            override fun onAnimationStart(animation: Animation) {
            }
        })
    }

    override fun onGalleryAdapterItemClick(view: View, position: Int, item: ScanEntity) {
        this.selectParent = item.parent
        this.selectFinder = true
        this.selectBucketDisplayName = item.bucketDisplayName
        hideFinderActionView()
    }

    override fun onGalleryFinderThumbnails(finderEntity: ScanEntity, container: FrameLayout) {
        onDisplayGalleryThumbnails(finderEntity, container)
    }

    override fun onDisplayGallery(width: Int, height: Int, galleryEntity: ScanEntity, container: FrameLayout) {
        container.removeAllViews()
        val imageView = GalleryImageView(container.context)
        Glide.with(container.context).asBitmap().load(galleryEntity.externalUri()).apply(requestOptions.override(width, height)).into(imageView)
        container.addView(imageView, FrameLayout.LayoutParams(width, height))
    }

    override fun onDisplayGalleryThumbnails(finderEntity: ScanEntity, container: FrameLayout) {
        container.removeAllViews()
        val imageView = GalleryImageView(container.context)
        Glide.with(container.context).load(finderEntity.externalUri()).apply(requestOptions).into(imageView)
        container.addView(imageView)
    }

    override fun onPhotoItemClick(context: Context, galleryBundle: GalleryBundle, scanEntity: ScanEntity, position: Int, parentId: Long) {
    }

    @SuppressLint("SetTextI18n")
    override fun onChangedCheckBox(isSelect: Boolean, galleryBundle: GalleryBundle, scanEntity: ScanEntity) {
        if (galleryFragment.selectCount == 0) {
            galleryWeChatPrev.text = galleryUiBundle.preViewText
            galleryWeChatPrev.isEnabled = false

            galleryWeChatToolbarSend.text = galleryUiBundle.selectText
            galleryWeChatToolbarSend.isEnabled = false
            return
        }

        galleryWeChatToolbarSend.isEnabled = true
        galleryWeChatToolbarSend.text = galleryUiBundle.selectText + "(${galleryFragment.selectCount}/${galleryBundle.multipleMaxCount})"

        galleryWeChatPrev.isEnabled = true
        galleryWeChatPrev.text = galleryUiBundle.preViewText + "(${galleryFragment.selectCount})"
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