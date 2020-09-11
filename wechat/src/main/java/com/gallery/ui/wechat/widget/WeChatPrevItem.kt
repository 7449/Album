package com.gallery.ui.wechat.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.kotlin.expand.content.openVideoExpand
import com.gallery.scan.args.file.ScanFileEntity
import com.gallery.scan.args.file.externalUriExpand
import com.gallery.scan.args.file.isGifExpand
import com.gallery.scan.args.file.isVideoExpand
import com.gallery.ui.wechat.R
import com.gallery.ui.wechat.engine.toFileSize
import kotlinx.android.synthetic.main.layout_prev_wechat_item.view.*

class WeChatPrevItem @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        addView(View.inflate(context, R.layout.layout_prev_wechat_item, null))
    }

    val imageView: ImageView
        get() = prevWeChatImageView

    private val videoPlayView: View
        get() = prevWeChatVideo

    private val gifView: TextView
        get() = prevWeChatGif

    fun update(galleryEntity: ScanFileEntity) {
        imageView.scaleType = if (galleryEntity.isVideoExpand) ImageView.ScaleType.FIT_XY else ImageView.ScaleType.FIT_CENTER
        gifView.visibility = if (galleryEntity.isGifExpand) View.VISIBLE else View.GONE
        gifView.text = context.getString(R.string.gallery_wechat_prev_gif_format).format(galleryEntity.size.toFileSize())
        videoPlayView.visibility = if (galleryEntity.isVideoExpand) View.VISIBLE else View.GONE
        videoPlayView.setOnClickListener { context.openVideoExpand(galleryEntity.externalUriExpand) {} }
    }

}