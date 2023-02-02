package com.gallery.ui.wechat.activity

import android.annotation.SuppressLint
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
import com.gallery.ui.wechat.args.WeChatGalleryConfig
import com.gallery.ui.wechat.args.WeChatPrevSaveArgs
import com.gallery.ui.wechat.args.WeChatPrevSaveArgs.Companion.toBundle
import com.gallery.ui.wechat.args.WeChatPrevSaveArgs.Companion.weChatPrevSaveArgs
import com.gallery.ui.wechat.databinding.WechatGalleryActivityPrevBinding
import com.gallery.ui.wechat.extension.weChatGalleryArgOrDefault
import com.gallery.ui.wechat.widget.WeChatGalleryPrevItem
import com.gallery.ui.wechat.widget.WeChatGallerySelectItem

@SuppressLint("SetTextI18n")
internal class WeChatGalleryPrevActivity : PrevCompatActivity(),
    GalleryFinderAdapter.AdapterFinderListener {

    override val galleryFragmentId: Int
        get() = R.id.preWeChatFragment

    private val selectAdapter: WeChatPrevSelectAdapter = WeChatPrevSelectAdapter(this)
    private val config: WeChatGalleryConfig by lazy { gapConfig.weChatGalleryArgOrDefault }

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
        if (scanEntity.duration > config.videoMaxDuration) {
            binding.galleryPrevVideoTip.visibility = View.VISIBLE
            binding.galleryPrevVideoTip.text =
                getString(R.string.wechat_gallery_select_video_prev_error).format(config.videoMaxDuration / 1000 / 60)
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
        window.statusBarColor = config.statusBarColor
        binding.prevWeChatToolbar.setBackgroundColor(config.toolbarBackground)

        binding.prevWeChatBottomView.setBackgroundColor(config.preBottomViewBackground)
        binding.galleryPrevList.setBackgroundColor(config.preBottomViewBackground)
        binding.galleryPrevList.alpha = 0.9.toFloat()
        binding.prevWeChatSelect.text = config.preBottomOkText
        binding.prevWeChatSelect.textSize = config.preBottomOkTextSize
        binding.prevWeChatSelect.setTextColor(config.preBottomOkTextColor)

        binding.prevWeChatFullImage.setButtonDrawable(R.drawable.wechat_gallery_selector_gallery_full_image_item_check)
        binding.prevWeChatSelect.setButtonDrawable(R.drawable.wechat_gallery_selector_gallery_full_image_item_check)

        binding.prevWeChatToolbarSend.textSize = config.selectTextSize
        binding.prevWeChatToolbarSend.text = config.selectText

        idList.clear()
        idList.addAll(savedInstanceState?.weChatPrevSaveArgs?.ids ?: arrayListOf())
        binding.prevWeChatToolbarBack.setOnClickListener { onGalleryFinish() }
        binding.prevWeChatToolbarText.text = "%s / %s".format(0, galleryConfig.maxCount)
        binding.prevWeChatFullImage.isChecked = config.fullImageSelect
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
        weChatSelectItem.update(finderEntity, idList, config.isPrev)
        Glide.with(this).asBitmap().load(finderEntity.uri).apply(
            RequestOptions().fitCenter()
        ).into(weChatSelectItem.imageView)
        container.addView(weChatSelectItem)
    }

    override fun onDisplayPrevGallery(entity: ScanEntity, container: FrameLayout) {
        container.removeAllViews()
        val weChatPrevItem = WeChatGalleryPrevItem(container.context)
        weChatPrevItem.update(entity)
        Glide.with(this)
            .load(entity.uri)
            .apply {
                if (entity.isVideo) {
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
        configs: GalleryConfigs,
        saveState: Bundle?
    ) {
        binding.prevWeChatToolbarText.text =
            (delegate.currentPosition + 1).toString() + "/" + delegate.itemCount
        binding.prevWeChatToolbarSend.text =
            config.selectText + if (delegate.isSelectEmpty) "" else "(${delegate.selectCount}/${galleryConfig.maxCount})"
        binding.prevWeChatSelect.setOnClickListener { delegate.selectPictureClick(it) }
        binding.galleryPrevList.visibility =
            if (delegate.selectItem.isEmpty()) View.GONE else View.VISIBLE
        binding.galleryPrevListLine.visibility =
            if (delegate.selectItem.isEmpty()) View.GONE else View.VISIBLE
        selectAdapter.updateSelect(if (config.isPrev) prevCompatArgs.prevArgs.selectList else delegate.selectItem)
        selectAdapter.refreshItem(delegate.currentItem)
        onUpdateVideoTip(delegate.currentItem)
        delegate.rootView.findViewById<View>(R.id.gallery_prev_checkbox)?.visibility = View.GONE
        delegate.rootView.setBackgroundColor(config.prevRootBackground)
    }

    override fun onSelectMultipleFileChanged(position: Int, entity: ScanEntity) {
        val fragment = requirePrevFragment
        val currentItem = fragment.currentItem
        binding.prevWeChatToolbarSend.text =
            config.selectText + if (fragment.isSelectEmpty) "" else "(${fragment.selectCount}/${galleryConfig.maxCount})"
        if (config.isPrev) {
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

    override fun onSelectMultipleMaxCount() {
        binding.prevWeChatSelect.isChecked = false
    }

    override fun onSelectMultipleFileNotExist(entity: ScanEntity) {
        binding.prevWeChatSelect.isChecked = false
    }

    override fun backResult(bundle: Bundle): Bundle {
        bundle.putBoolean(
            WeChatConfig.FULL_IMAGE,
            binding.prevWeChatFullImage.isChecked
        )
        return super.selectResult(bundle)
    }

    override fun toolbarResult(bundle: Bundle): Bundle {
        bundle.putBoolean(
            WeChatConfig.FULL_IMAGE,
            binding.prevWeChatFullImage.isChecked
        )
        return super.selectResult(bundle)
    }

    override fun selectResult(bundle: Bundle): Bundle {
        bundle.putBoolean(
            WeChatConfig.FULL_IMAGE,
            binding.prevWeChatFullImage.isChecked
        )
        return super.selectResult(bundle)
    }

}