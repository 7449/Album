package com.gallery.ui.wechat.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.gallery.core.expand.isGif
import com.gallery.core.expand.isVideo
import com.gallery.scan.ScanEntity
import com.gallery.ui.wechat.R
import com.gallery.ui.wechat.util.formatTime
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
        selectView.visibility = if (galleryEntity.isCheck) View.VISIBLE else View.GONE
        gifView.visibility = if (galleryEntity.isGif()) View.VISIBLE else View.GONE
        videoView.visibility = if (galleryEntity.isVideo()) View.VISIBLE else View.GONE
        bottomView.visibility = if (galleryEntity.isGif() || galleryEntity.isVideo()) View.VISIBLE else View.GONE
        videoView.text = if (galleryEntity.isVideo()) galleryEntity.duration.formatTime() else ""
    }
}