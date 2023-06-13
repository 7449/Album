package develop.file.gallery.ui.wechat.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gallery.ui.wechat.R
import com.gallery.ui.wechat.databinding.WechatGalleryActivityPrevBinding
import develop.file.gallery.compat.activity.PrevCompatActivity
import develop.file.gallery.compat.extensions.ActivityCompat.prevFragment
import develop.file.gallery.compat.extensions.ActivityCompat.requirePrevFragment
import develop.file.gallery.compat.finder.GalleryFinderAdapter
import develop.file.gallery.delegate.IPrevDelegate
import develop.file.gallery.entity.ScanEntity
import develop.file.gallery.ui.wechat.WeChatConfig
import develop.file.gallery.ui.wechat.adapter.WeChatPrevSelectAdapter
import develop.file.gallery.ui.wechat.alpha9
import develop.file.gallery.ui.wechat.args.WeChatPrevSaveArgs
import develop.file.gallery.ui.wechat.args.WeChatPrevSaveArgs.Companion.toBundle
import develop.file.gallery.ui.wechat.args.WeChatPrevSaveArgs.Companion.weChatPrevSaveArgs
import develop.file.gallery.ui.wechat.checkboxFImageResource
import develop.file.gallery.ui.wechat.colorBlack
import develop.file.gallery.ui.wechat.colorWhite
import develop.file.gallery.ui.wechat.extension.ResultCompat.weChatGalleryArgOrDefault
import develop.file.gallery.ui.wechat.maxVideoDuration
import develop.file.gallery.ui.wechat.rgb38
import develop.file.gallery.ui.wechat.size12
import develop.file.gallery.ui.wechat.size14
import develop.file.gallery.ui.wechat.textSelect
import develop.file.gallery.ui.wechat.textSend
import develop.file.gallery.ui.wechat.widget.WeChatGalleryPrevItem
import develop.file.gallery.ui.wechat.widget.WeChatGallerySelectItem

@SuppressLint("SetTextI18n")
internal class WeChatGalleryPrevActivity : PrevCompatActivity(),
    GalleryFinderAdapter.AdapterFinderListener {

    override val galleryFragmentId: Int get() = R.id.preWeChatFragment

    private val selectAdapter = WeChatPrevSelectAdapter(this)
    private val config by lazy { gapConfig.weChatGalleryArgOrDefault }

    private val idList: ArrayList<Long> = arrayListOf()
    private val binding by lazy { WechatGalleryActivityPrevBinding.inflate(layoutInflater) }

    private fun initViews() {
        window.statusBarColor = rgb38

        binding.prevWeChatToolbar.setBackgroundColor(rgb38)
        binding.prevWeChatToolbarBack.setOnClickListener { onGalleryFinish() }
        binding.prevWeChatToolbarSend.isEnabled = true
        binding.prevWeChatToolbarSend.textSize = size12
        binding.prevWeChatToolbarSend.text = textSend
        binding.prevWeChatToolbarSend.setOnClickListener { sendSelectItems() }

        binding.galleryPrevList.setBackgroundColor(rgb38)
        binding.galleryPrevList.alpha = alpha9
        binding.galleryPrevList.adapter = selectAdapter

        binding.prevWeChatBottomView.setBackgroundColor(rgb38)
        binding.prevWeChatFullImage.setButtonDrawable(checkboxFImageResource)
        binding.prevWeChatFullImage.isChecked = config.fullImageSelect
        binding.prevWeChatSelect.text = textSelect
        binding.prevWeChatSelect.textSize = size14
        binding.prevWeChatSelect.setTextColor(colorWhite)
        binding.prevWeChatSelect.setButtonDrawable(checkboxFImageResource)
        binding.prevWeChatSelect.setOnClickListener { prevFragment?.checkBoxClick(it) }
    }

    private fun sendSelectItems() {
        val fragment = requirePrevFragment
        if (fragment.isSelectEmpty) {
            fragment.selectItem.add(fragment.currentItem)
            fragment.selectItem.forEach { it.isSelected = true }
        }
        onGallerySelectEntities()
    }

    private fun updateToolbarSelectCount() {
        val fragment = prevFragment ?: return
        binding.prevWeChatToolbarText.text =
            (fragment.currentPosition + 1).toString() + " / " + fragment.itemCount
    }

    private fun updateToolbarSendCount() {
        val fragment = prevFragment ?: return
        binding.prevWeChatToolbarSend.text =
            textSend + if (fragment.isSelectEmpty) "" else "(${fragment.selectCount}/${galleryConfig.maxCount})"
    }

    private fun updateToolbarSendEnable() {
        val currentItem = prevFragment?.currentItem ?: return
        binding.prevWeChatToolbarSend.isEnabled =
            !currentItem.isVideo || currentItem.duration <= maxVideoDuration
    }

    private fun updateContainerVideoTip() {
        val currentItem = prevFragment?.currentItem ?: return
        binding.galleryPrevVideoTip.isVisible =
            currentItem.isVideo && currentItem.duration > maxVideoDuration
        if (currentItem.duration > maxVideoDuration) {
            binding.galleryPrevVideoTip.text =
                getString(R.string.wechat_gallery_select_video_prev_error).format(maxVideoDuration / 1000 / 60)
        } else {
            binding.galleryPrevVideoTip.text = ""
        }
    }

    private fun updateSelectItemVisible() {
        if (config.isPrev) return
        val selectItem = prevFragment?.selectItem ?: return
        binding.galleryPrevList.visibility =
            if (selectItem.isEmpty()) View.GONE else View.VISIBLE
        binding.galleryPrevListLine.visibility =
            if (selectItem.isEmpty()) View.GONE else View.VISIBLE
    }

    private fun updateFullImageVisible() {
        val currentItem = prevFragment?.currentItem ?: return
        binding.prevWeChatFullImage.visibility =
            if (currentItem.isGif || currentItem.isVideo) View.GONE else View.VISIBLE
    }

    private fun getFullImageChecked(): Boolean {
        return binding.prevWeChatFullImage.isChecked
    }

    private fun updateBottomSelectVisible() {
        val currentItem = prevFragment?.currentItem ?: return
        binding.prevWeChatSelect.isEnabled =
            !currentItem.isVideo || currentItem.duration <= maxVideoDuration
        binding.prevWeChatSelect.setTextColor(
            if (currentItem.isVideo && currentItem.duration > maxVideoDuration) {
                ContextCompat.getColor(this, R.color.wechat_gallery_color_999999)
            } else colorWhite
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViews()
        idList.clear()
        idList.addAll(savedInstanceState?.weChatPrevSaveArgs?.ids ?: arrayListOf())
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        WeChatPrevSaveArgs(idList).toBundle(outState)
    }

    override fun onGalleryAdapterItemClick(view: View, position: Int, item: ScanEntity) {
        val fragment = requirePrevFragment
        fragment.setCurrentItem(fragment.index(item.id))
    }

    override fun onGalleryFinderThumbnails(entity: ScanEntity, container: FrameLayout) {
        container.removeAllViews()
        val weChatSelectItem = WeChatGallerySelectItem(container.context)
        weChatSelectItem.update(entity, idList, config.isPrev)
        Glide.with(this).asBitmap().load(entity.uri).apply(
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
            .into(weChatPrevItem.imageView)
        container.addView(weChatPrevItem)
    }

    override fun onPageSelected(position: Int) {
        val fragment = requirePrevFragment
        val currentItem = fragment.currentItem
        updateToolbarSelectCount()
        updateToolbarSendEnable()
        updateContainerVideoTip()
        updateFullImageVisible()
        updateBottomSelectVisible()
        binding.prevWeChatSelect.isChecked = fragment.isCheckBox(position)
        selectAdapter.refreshItem(currentItem)
        binding.galleryPrevList.scrollToPosition(selectAdapter.index(currentItem))
    }

    override fun onPrevCreated(delegate: IPrevDelegate, saveState: Bundle?) {
        updateToolbarSendCount()
        updateSelectItemVisible()
        selectAdapter.updateSelect(if (config.isPrev) prevCompatArgs.prevArgs.selectList else delegate.selectItem)
        selectAdapter.refreshItem(delegate.currentItem)
        delegate.rootView.findViewById<View>(com.gallery.compat.R.id.gallery_prev_checkbox)?.visibility =
            View.GONE
        delegate.rootView.setBackgroundColor(colorBlack)
    }

    override fun onSelectMultipleFileChanged(position: Int, entity: ScanEntity) {
        val fragment = requirePrevFragment
        val currentItem = fragment.currentItem
        updateToolbarSendCount()
        updateSelectItemVisible()
        if (config.isPrev) {
            if (!currentItem.isSelected) {
                idList.add(currentItem.id)
            } else {
                idList.remove(currentItem.id)
            }
            selectAdapter.refreshItem(currentItem)
        } else {
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
        bundle.putBoolean(WeChatConfig.FULL_IMAGE, getFullImageChecked())
        return super.selectResult(bundle)
    }

    override fun toolbarResult(bundle: Bundle): Bundle {
        bundle.putBoolean(WeChatConfig.FULL_IMAGE, getFullImageChecked())
        return super.selectResult(bundle)
    }

    override fun selectResult(bundle: Bundle): Bundle {
        bundle.putBoolean(WeChatConfig.FULL_IMAGE, getFullImageChecked())
        return super.selectResult(bundle)
    }

}