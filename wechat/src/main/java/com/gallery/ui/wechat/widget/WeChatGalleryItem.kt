package com.gallery.ui.wechat.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.gallery.core.entity.ScanEntity
import com.gallery.core.extensions.colorExpand
import com.gallery.ui.wechat.R
import com.gallery.ui.wechat.extension.formatTimeVideo
import kotlinx.android.synthetic.main.layout_gallery_wechat_item.view.*

class WeChatGalleryItem @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        addView(View.inflate(context, R.layout.layout_gallery_wechat_item, null))
    }

    val imageView: ImageView
        get() = viewWeChatImageView

    private val gifView: View
        get() = viewWeChatGif

    private val videoView: TextView
        get() = viewWeChatVideo

    private val bottomView: View
        get() = viewWeChatBottomView

    private val selectView: View
        get() = viewWeChatBackSelect

    fun update(scanEntity: ScanEntity) {
        selectView.visibility = if (scanEntity.isSelected) VISIBLE else GONE
        gifView.visibility = if (scanEntity.isGif) VISIBLE else GONE
        videoView.visibility = if (scanEntity.isVideo) VISIBLE else GONE
        bottomView.visibility = if (scanEntity.isVideo) VISIBLE else GONE
        bottomView.setBackgroundColor(if (scanEntity.isGif) Color.TRANSPARENT else context.colorExpand(R.color.color_B3000000))
        bottomView.visibility = if (scanEntity.isVideo || scanEntity.isGif) VISIBLE else GONE
        videoView.text = if (scanEntity.isVideo) scanEntity.duration.formatTimeVideo() else ""
    }
}