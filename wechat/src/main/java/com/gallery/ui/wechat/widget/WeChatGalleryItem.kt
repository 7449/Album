package com.gallery.ui.wechat.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gallery.core.entity.ScanEntity
import com.gallery.ui.wechat.R
import com.gallery.ui.wechat.databinding.WechatGalleryLayoutItemBinding
import com.gallery.ui.wechat.extension.color
import com.gallery.ui.wechat.extension.formatTimeVideo

internal class WeChatGalleryItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val viewBinding: WechatGalleryLayoutItemBinding =
        WechatGalleryLayoutItemBinding.inflate(LayoutInflater.from(getContext()), this, true)

    private val imageView: ImageView
        get() = viewBinding.viewWeChatImageView

    private val gifView: View
        get() = viewBinding.viewWeChatGif

    private val videoView: TextView
        get() = viewBinding.viewWeChatVideo

    private val bottomView: View
        get() = viewBinding.viewWeChatBottomView

    private val selectView: View
        get() = viewBinding.viewWeChatBackSelect

    fun update(entity: ScanEntity) {
        selectView.visibility = if (entity.isSelected) VISIBLE else GONE
        gifView.visibility = if (entity.isGif) VISIBLE else GONE
        videoView.visibility = if (entity.isVideo) VISIBLE else GONE
        bottomView.visibility = if (entity.isVideo) VISIBLE else GONE
        bottomView.setBackgroundColor(
            if (entity.isGif) Color.TRANSPARENT else context.color(
                R.color.wechat_gallery_color_B3000000
            )
        )
        bottomView.visibility = if (entity.isVideo || entity.isGif) VISIBLE else GONE
        videoView.text = if (entity.isVideo) entity.duration.formatTimeVideo() else ""
        Glide.with(this).load(entity.uri).apply(
            RequestOptions().centerCrop()
                .override(width, height)
        ).into(imageView)
    }

}