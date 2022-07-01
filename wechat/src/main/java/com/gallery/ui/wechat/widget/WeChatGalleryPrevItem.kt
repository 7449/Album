package com.gallery.ui.wechat.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.gallery.core.entity.ScanEntity
import com.gallery.core.extensions.openVideoExpand
import com.gallery.ui.wechat.R
import com.gallery.ui.wechat.databinding.WechatGalleryLayoutPrevItemBinding
import com.gallery.ui.wechat.extension.toFileSize

class WeChatGalleryPrevItem @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val viewBinding: WechatGalleryLayoutPrevItemBinding =
            WechatGalleryLayoutPrevItemBinding.inflate(LayoutInflater.from(getContext()), this, true)

    val imageView: ImageView
        get() = viewBinding.prevWeChatImageView

    private val videoPlayView: View
        get() = viewBinding.prevWeChatVideo

    private val gifView: TextView
        get() = viewBinding.prevWeChatGif

    fun update(scanEntity: ScanEntity) {
        imageView.scaleType =
                if (scanEntity.isVideo) ImageView.ScaleType.FIT_XY else ImageView.ScaleType.FIT_CENTER
        gifView.visibility = if (scanEntity.isGif) View.VISIBLE else View.GONE
        gifView.text = context.getString(R.string.wechat_gallery_prev_gif_format)
                .format(scanEntity.size.toFileSize())
        videoPlayView.visibility = if (scanEntity.isVideo) View.VISIBLE else View.GONE
        videoPlayView.setOnClickListener { context.openVideoExpand(scanEntity.uri) {} }
    }

}