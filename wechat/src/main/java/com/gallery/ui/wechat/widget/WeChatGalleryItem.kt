package com.gallery.ui.wechat.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.kotlin.expand.content.colorExpand
import com.gallery.core.delegate.ScanEntity
import com.gallery.scan.args.file.isGifExpand
import com.gallery.scan.args.file.isVideoExpand
import com.gallery.ui.wechat.R
import com.gallery.ui.wechat.engine.formatTimeVideo
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

    fun update(galleryEntity: ScanEntity) {
        selectView.visibility = if (galleryEntity.isSelected) View.VISIBLE else View.GONE
        gifView.visibility = if (galleryEntity.delegate.isGifExpand) View.VISIBLE else View.GONE
        videoView.visibility = if (galleryEntity.delegate.isVideoExpand) View.VISIBLE else View.GONE
        bottomView.visibility = if (galleryEntity.delegate.isVideoExpand) View.VISIBLE else View.GONE
        bottomView.setBackgroundColor(if (galleryEntity.delegate.isGifExpand) Color.TRANSPARENT else context.colorExpand(R.color.color_B3000000))
        bottomView.visibility = if (galleryEntity.delegate.isVideoExpand || galleryEntity.delegate.isGifExpand) View.VISIBLE else View.GONE
        videoView.text = if (galleryEntity.delegate.isVideoExpand) galleryEntity.duration.formatTimeVideo() else ""
    }
}