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
import com.gallery.compat.extensions.prevFragment
import com.gallery.compat.finder.GalleryFinderAdapter
import com.gallery.core.GalleryBundle
import com.gallery.core.delegate.IPrevDelegate
import com.gallery.core.entity.ScanEntity
import com.gallery.ui.wechat.R
import com.gallery.ui.wechat.WeChatConfig
import com.gallery.ui.wechat.WeChatPrevArgs
import com.gallery.ui.wechat.WeChatPrevArgs.Companion.weChatPrevArgsOrDefault
import com.gallery.ui.wechat.WeChatPrevSaveArgs
import com.gallery.ui.wechat.WeChatPrevSaveArgs.Companion.putArgs
import com.gallery.ui.wechat.WeChatPrevSaveArgs.Companion.weChatPrevSaveArgs
import com.gallery.ui.wechat.adapter.WeChatPrevSelectAdapter
import com.gallery.ui.wechat.databinding.GalleryActivityWechatPrevBinding
import com.gallery.ui.wechat.widget.WeChatPrevItem
import com.gallery.ui.wechat.widget.WeChatSelectItem

@SuppressLint("SetTextI18n")
class GalleryWeChatPrevActivity : PrevCompatActivity(), GalleryFinderAdapter.AdapterFinderListener {

    override val galleryFragmentId: Int
        get() = R.id.preWeChatFragment

    private val selectAdapter: WeChatPrevSelectAdapter = WeChatPrevSelectAdapter(this)
    private val weChatPrevArgs: WeChatPrevArgs by lazy { uiGapConfig.weChatPrevArgsOrDefault }

    //选中的item的ids
    private val idList: ArrayList<Long> = arrayListOf()
    private val binding: GalleryActivityWechatPrevBinding by lazy {
        GalleryActivityWechatPrevBinding.inflate(layoutInflater)
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
        if (scanEntity.duration > weChatPrevArgs.videoDuration) {
            binding.galleryPrevVideoTip.visibility = View.VISIBLE
            binding.galleryPrevVideoTip.text =
                getString(R.string.gallery_select_video_prev_error).format(weChatPrevArgs.videoDuration / 1000 / 60)
            binding.prevWeChatSelect.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.color_999999
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
        WeChatPrevSaveArgs(idList).putArgs(outState)
    }

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.statusBarColor = compatConfig.statusBarColor
        binding.prevWeChatToolbar.setBackgroundColor(compatConfig.toolbarBackground)

        binding.prevWeChatBottomView.setBackgroundColor(compatConfig.preBottomViewBackground)
        binding.galleryPrevList.setBackgroundColor(compatConfig.preBottomViewBackground)
        binding.galleryPrevList.alpha = 0.9.toFloat()
        binding.prevWeChatSelect.text = compatConfig.preBottomOkText
        binding.prevWeChatSelect.textSize = compatConfig.preBottomOkTextSize
        binding.prevWeChatSelect.setTextColor(compatConfig.preBottomOkTextColor)

        binding.prevWeChatFullImage.setButtonDrawable(R.drawable.wechat_selector_gallery_full_image_item_check)
        binding.prevWeChatSelect.setButtonDrawable(R.drawable.wechat_selector_gallery_full_image_item_check)

        binding.prevWeChatToolbarSend.textSize = compatConfig.selectTextSize
        binding.prevWeChatToolbarSend.text = compatConfig.selectText

        idList.clear()
        idList.addAll(savedInstanceState?.weChatPrevSaveArgs?.ids ?: arrayListOf())
        binding.prevWeChatToolbarBack.setOnClickListener { onGalleryFinish() }
        binding.prevWeChatToolbarText.text = "%s / %s".format(0, galleryConfig.multipleMaxCount)
        binding.prevWeChatFullImage.isChecked = weChatPrevArgs.fullImageSelect
        binding.galleryPrevList.layoutManager = LinearLayoutManager(this).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }
        binding.galleryPrevList.adapter = selectAdapter
        binding.prevWeChatToolbarSend.isEnabled = true
        binding.prevWeChatToolbarSend.setOnClickListener {
            val fragment = prevFragment
            if (fragment.isSelectEmpty) {
                fragment.selectItem.add(fragment.currentItem)
                fragment.selectItem.forEach { it.isSelected = true }
            }
            onGallerySelectEntities()
        }
    }

    override fun onGalleryAdapterItemClick(view: View, position: Int, item: ScanEntity) {
        val fragment = prevFragment
        fragment.setCurrentItem(fragment.allItem.indexOfFirst { it.id == item.id })
    }

    override fun onGalleryFinderThumbnails(finderEntity: ScanEntity, container: FrameLayout) {
        container.removeAllViews()
        val weChatSelectItem = WeChatSelectItem(container.context)
        weChatSelectItem.update(finderEntity, idList, weChatPrevArgs.isPrev)
        Glide.with(this).asBitmap().load(finderEntity.uri).apply(
            RequestOptions().fitCenter()
        ).into(weChatSelectItem.imageView)
        container.addView(weChatSelectItem)
    }

    override fun onDisplayGalleryPrev(scanEntity: ScanEntity, container: FrameLayout) {
        container.removeAllViews()
        val weChatPrevItem = WeChatPrevItem(container.context)
        weChatPrevItem.update(scanEntity)
        Glide.with(this).load(scanEntity.uri).into(weChatPrevItem.imageView)
        container.addView(weChatPrevItem)
    }

    override fun onPageSelected(position: Int) {
        val fragment = prevFragment
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
        bundle: GalleryBundle,
        savedInstanceState: Bundle?
    ) {
        super.onPrevCreated(delegate, bundle, savedInstanceState)
        val prevFragment = prevFragment
        binding.prevWeChatToolbarText.text =
            (prevFragment.currentPosition + 1).toString() + "/" + prevFragment.itemCount
        binding.prevWeChatToolbarSend.text =
            compatConfig.selectText + if (prevFragment.isSelectEmpty) "" else "(${prevFragment.selectCount}/${galleryConfig.multipleMaxCount})"
        binding.prevWeChatSelect.setOnClickListener { prevFragment.checkBoxClick(it) }
        binding.galleryPrevList.visibility =
            if (prevFragment.selectItem.isEmpty()) View.GONE else View.VISIBLE
        binding.galleryPrevListLine.visibility =
            if (prevFragment.selectItem.isEmpty()) View.GONE else View.VISIBLE
        selectAdapter.updateSelect(if (weChatPrevArgs.isPrev) uiPrevArgs.prevArgs.selectList else prevFragment.selectItem)
        onUpdateVideoTip(prevFragment.currentItem)
        prevFragment.view?.findViewById<View>(R.id.gallery_prev_checkbox)?.visibility = View.GONE
    }

    override fun onChangedCheckBox() {
        val fragment = prevFragment
        val currentItem = fragment.currentItem
        binding.prevWeChatToolbarSend.text =
            compatConfig.selectText + if (fragment.isSelectEmpty) "" else "(${fragment.selectCount}/${galleryConfig.multipleMaxCount})"
        if (weChatPrevArgs.isPrev) {
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

    override fun onClickItemBoxMaxCount(
        context: Context,
        bundle: GalleryBundle,
        scanEntity: ScanEntity
    ) {
        super.onClickItemBoxMaxCount(context, bundle, scanEntity)
        binding.prevWeChatSelect.isChecked = false
    }

    override fun onClickItemFileNotExist(
        context: Context,
        bundle: GalleryBundle,
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

    override fun onToolbarFinishResult(bundle: Bundle): Bundle {
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