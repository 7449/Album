package com.gallery.ui.wechat.activity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gallery.compat.activity.PrevCompatActivity
import com.gallery.compat.extensions.requirePrevFragment
import com.gallery.compat.finder.GalleryFinderAdapter
import com.gallery.core.GalleryConfigs
import com.gallery.core.delegate.IPrevDelegate
import com.gallery.core.entity.ScanEntity
import com.gallery.ui.wechat.R
import com.gallery.ui.wechat.WeChatConfig
import com.gallery.ui.wechat.adapter.WeChatPrevSelectAdapter
import com.gallery.ui.wechat.args.WeChatGalleryBundle
import com.gallery.ui.wechat.args.WeChatPrevSaveArgs
import com.gallery.ui.wechat.args.WeChatPrevSaveArgs.Companion.toBundle
import com.gallery.ui.wechat.args.WeChatPrevSaveArgs.Companion.weChatPrevSaveArgs
import com.gallery.ui.wechat.databinding.WechatGalleryActivityPrevBinding
import com.gallery.ui.wechat.extension.weChatGalleryArgOrDefault
import com.gallery.ui.wechat.widget.WeChatGalleryPrevItem
import com.gallery.ui.wechat.widget.WeChatGallerySelectItem

@SuppressLint("SetTextI18n")
class WeChatGalleryPrevActivity : PrevCompatActivity(), GalleryFinderAdapter.AdapterFinderListener {

    override val galleryFragmentId: Int
        get() = R.id.preWeChatFragment

    private val selectAdapter: WeChatPrevSelectAdapter = WeChatPrevSelectAdapter(this)
    private val uiGalleryBundle: WeChatGalleryBundle by lazy { gapConfig.weChatGalleryArgOrDefault }

    //选中的item的ids
    private val idList: ArrayList<Long> = arrayListOf()
    private val binding: WechatGalleryActivityPrevBinding by lazy {
        WechatGalleryActivityPrevBinding.inflate(layoutInflater)
    }

    private fun onUpdateVideoTip(scanEntity: ScanEntity) {
        if (!scanEntity.isVideo) {
            binding.galleryPrevVideoTip.visibility = View.GONE
            binding.prevWeChatToolbarSend.isEnabled = true
            binding.prevWeChatSelect.isEnabled = true
            binding.prevWeChatSelect.setTextColor(
                ContextCompat.getColor(
                    this,
                    android.R.color.white
                )
            )
            return
        }
        if (scanEntity.duration > uiGalleryBundle.videoMaxDuration) {
            binding.galleryPrevVideoTip.visibility = View.VISIBLE
            binding.galleryPrevVideoTip.text =
                getString(R.string.wechat_gallery_select_video_prev_error).format(uiGalleryBundle.videoMaxDuration / 1000 / 60)
            binding.prevWeChatSelect.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.wechat_gallery_color_999999
                )
            )
            binding.prevWeChatToolbarSend.isEnabled = false
            binding.prevWeChatSelect.isEnabled = false
        } else {
            binding.prevWeChatSelect.isEnabled = true
            binding.prevWeChatToolbarSend.isEnabled = true
            binding.prevWeChatSelect.setTextColor(
                ContextCompat.getColor(
                    this,
                    android.R.color.white
                )
            )
            binding.galleryPrevVideoTip.visibility = View.GONE
            binding.galleryPrevVideoTip.text = ""
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        WeChatPrevSaveArgs(idList).toBundle(outState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.statusBarColor = uiGalleryBundle.statusBarColor
        binding.prevWeChatToolbar.setBackgroundColor(uiGalleryBundle.toolbarBackground)

        binding.prevWeChatBottomView.setBackgroundColor(uiGalleryBundle.preBottomViewBackground)
        binding.galleryPrevList.setBackgroundColor(uiGalleryBundle.preBottomViewBackground)
        binding.galleryPrevList.alpha = 0.9.toFloat()
        binding.prevWeChatSelect.text = uiGalleryBundle.preBottomOkText
        binding.prevWeChatSelect.textSize = uiGalleryBundle.preBottomOkTextSize
        binding.prevWeChatSelect.setTextColor(uiGalleryBundle.preBottomOkTextColor)

        binding.prevWeChatFullImage.setButtonDrawable(R.drawable.wechat_gallery_selector_gallery_full_image_item_check)
        binding.prevWeChatSelect.setButtonDrawable(R.drawable.wechat_gallery_selector_gallery_full_image_item_check)

        binding.prevWeChatToolbarSend.textSize = uiGalleryBundle.selectTextSize
        binding.prevWeChatToolbarSend.text = uiGalleryBundle.selectText

        idList.clear()
        idList.addAll(savedInstanceState?.weChatPrevSaveArgs?.ids ?: arrayListOf())
        binding.prevWeChatToolbarBack.setOnClickListener { onGalleryFinish() }
        binding.prevWeChatToolbarText.text = "%s / %s".format(0, galleryConfig.maxCount)
        binding.prevWeChatFullImage.isChecked = uiGalleryBundle.fullImageSelect
        binding.galleryPrevList.layoutManager = LinearLayoutManager(this).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }
        binding.galleryPrevList.adapter = selectAdapter
        binding.prevWeChatToolbarSend.isEnabled = true
        binding.prevWeChatToolbarSend.setOnClickListener {
            val fragment = requirePrevFragment
            if (fragment.isSelectEmpty) {
                fragment.selectItem.add(fragment.currentItem)
                fragment.selectItem.forEach { it.isSelected = true }
            }
            onGallerySelectEntities()
        }
    }

    override fun onGalleryAdapterItemClick(view: View, position: Int, item: ScanEntity) {
        val fragment = requirePrevFragment
        fragment.setCurrentItem(fragment.allItem.indexOfFirst { it.id == item.id })
    }

    override fun onGalleryFinderThumbnails(finderEntity: ScanEntity, container: FrameLayout) {
        container.removeAllViews()
        val weChatSelectItem = WeChatGallerySelectItem(container.context)
        weChatSelectItem.update(finderEntity, idList, uiGalleryBundle.isPrev)
        Glide.with(this).asBitmap().load(finderEntity.uri).apply(
            RequestOptions().fitCenter()
        ).into(weChatSelectItem.imageView)
        container.addView(weChatSelectItem)
    }

    override fun onDisplayGalleryPrev(scanEntity: ScanEntity, container: FrameLayout) {
        container.removeAllViews()
        val weChatPrevItem = WeChatGalleryPrevItem(container.context)
        weChatPrevItem.update(scanEntity)
        Glide.with(this)
            .load(scanEntity.uri)
            .apply {
                if (scanEntity.isVideo) {
                    apply(RequestOptions().centerCrop())
                }
            }
            .into(weChatPrevItem.imageView)
        container.addView(weChatPrevItem)
    }

    override fun onPageSelected(position: Int) {
        val fragment = requirePrevFragment
        val currentItem: ScanEntity = fragment.currentItem
        onUpdateVideoTip(fragment.currentItem)
        binding.prevWeChatToolbarText.text = (position + 1).toString() + "/" + fragment.itemCount
        binding.prevWeChatSelect.isChecked = fragment.isCheckBox(position)
        binding.prevWeChatFullImage.visibility =
            if (currentItem.isGif || currentItem.isVideo) View.GONE else View.VISIBLE
        selectAdapter.refreshItem(currentItem)
        binding.galleryPrevList.scrollToPosition(selectAdapter.index(currentItem))
    }

    override fun onPrevCreated(
        delegate: IPrevDelegate,
        bundle: GalleryConfigs,
        savedInstanceState: Bundle?
    ) {
        binding.prevWeChatToolbarText.text =
            (delegate.currentPosition + 1).toString() + "/" + delegate.itemCount
        binding.prevWeChatToolbarSend.text =
            uiGalleryBundle.selectText + if (delegate.isSelectEmpty) "" else "(${delegate.selectCount}/${galleryConfig.maxCount})"
        binding.prevWeChatSelect.setOnClickListener { delegate.selectPictureClick(it) }
        binding.galleryPrevList.visibility =
            if (delegate.selectItem.isEmpty()) View.GONE else View.VISIBLE
        binding.galleryPrevListLine.visibility =
            if (delegate.selectItem.isEmpty()) View.GONE else View.VISIBLE
        selectAdapter.updateSelect(if (uiGalleryBundle.isPrev) prevCompatArgs.prevArgs.selectList else delegate.selectItem)
        onUpdateVideoTip(delegate.currentItem)
        delegate.rootView.findViewById<View>(R.id.gallery_prev_checkbox)?.visibility = View.GONE
        delegate.rootView.setBackgroundColor(uiGalleryBundle.prevRootBackground)
    }

    override fun onCheckBoxChanged() {
        val fragment = requirePrevFragment
        val currentItem = fragment.currentItem
        binding.prevWeChatToolbarSend.text =
            uiGalleryBundle.selectText + if (fragment.isSelectEmpty) "" else "(${fragment.selectCount}/${galleryConfig.maxCount})"
        if (uiGalleryBundle.isPrev) {
            if (!currentItem.isSelected) {
                idList.add(currentItem.id)
            } else {
                idList.remove(currentItem.id)
            }
            selectAdapter.refreshItem(currentItem)
        } else {
            binding.galleryPrevList.visibility =
                if (fragment.isSelectEmpty) View.GONE else View.VISIBLE
            binding.galleryPrevListLine.visibility =
                if (fragment.isSelectEmpty) View.GONE else View.VISIBLE
            selectAdapter.updateSelect(fragment.selectItem)
            if (currentItem.isSelected) {
                selectAdapter.addSelect(currentItem)
            }
        }
    }

    override fun onClickItemMaxCount(
        context: Context,
        bundle: GalleryConfigs,
        scanEntity: ScanEntity
    ) {
        super.onClickItemMaxCount(context, bundle, scanEntity)
        binding.prevWeChatSelect.isChecked = false
    }

    override fun onClickItemFileNotExist(
        context: Context,
        bundle: GalleryConfigs,
        scanEntity: ScanEntity
    ) {
        super.onClickItemFileNotExist(context, bundle, scanEntity)
        binding.prevWeChatSelect.isChecked = false
    }

    override fun onKeyBackResult(bundle: Bundle): Bundle {
        bundle.putBoolean(
            WeChatConfig.FULL_IMAGE,
            binding.prevWeChatFullImage.isChecked
        )
        return super.onSelectEntitiesResult(bundle)
    }

    override fun onToolbarResult(bundle: Bundle): Bundle {
        bundle.putBoolean(
            WeChatConfig.FULL_IMAGE,
            binding.prevWeChatFullImage.isChecked
        )
        return super.onSelectEntitiesResult(bundle)
    }

    override fun onSelectEntitiesResult(bundle: Bundle): Bundle {
        bundle.putBoolean(
            WeChatConfig.FULL_IMAGE,
            binding.prevWeChatFullImage.isChecked
        )
        return super.onSelectEntitiesResult(bundle)
    }

}