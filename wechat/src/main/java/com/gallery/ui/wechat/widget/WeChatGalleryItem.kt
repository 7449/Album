package com.gallery.ui.wechat.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.gallery.core.entity.ScanEntity
import com.gallery.ui.wechat.R
import com.gallery.ui.wechat.databinding.WechatGalleryLayoutItemBinding
import com.gallery.ui.wechat.extension.colorExpand
import com.gallery.ui.wechat.extension.formatTimeVideo

class WeChatGalleryItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val viewBinding: WechatGalleryLayoutItemBinding =
        WechatGalleryLayoutItemBinding.inflate(LayoutInflater.from(getContext()), this, true)

    val imageView: ImageView
        get() = viewBinding.viewWeChatImageView

    private val gifView: View
        get() = viewBinding.viewWeChatGif

    private val videoView: TextView
        get() = viewBinding.viewWeChatVideo

    private val bottomView: View
        get() = viewBinding.viewWeChatBottomView

    private val selectView: View
        get() = viewBinding.viewWeChatBackSelect

    fun update(scanEntity: ScanEntity) {
        selectView.visibility = if (scanEntity.isSelected) VISIBLE else GONE
        gifView.visibility = if (scanEntity.isGif) VISIBLE else GONE
        videoView.visibility = if (scanEntity.isVideo) VISIBLE else GONE
        bottomView.visibility = if (scanEntity.isVideo) VISIBLE else GONE
        bottomView.setBackgroundColor(
            if (scanEntity.isGif) Color.TRANSPARENT else context.colorExpand(
                R.color.wechat_gallery_color_B3000000
            )
        )
        bottomView.visibility = if (scanEntity.isVideo || scanEntity.isGif) VISIBLE else GONE
        videoView.text = if (scanEntity.isVideo) scanEntity.duration.formatTimeVideo() else ""
    }

}