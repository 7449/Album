package com.gallery.ui.wechat.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.kotlin.expand.content.openVideoExpand
import com.gallery.core.entity.ScanEntity
import com.gallery.ui.wechat.R
import com.gallery.ui.wechat.toFileSize
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

    fun update(scanEntity: ScanEntity) {
        imageView.scaleType = if (scanEntity.isVideo) ImageView.ScaleType.FIT_XY else ImageView.ScaleType.FIT_CENTER
        gifView.visibility = if (scanEntity.isGif) View.VISIBLE else View.GONE
        gifView.text = context.getString(R.string.gallery_wechat_prev_gif_format).format(scanEntity.size.toFileSize())
        videoPlayView.visibility = if (scanEntity.isVideo) View.VISIBLE else View.GONE
        videoPlayView.setOnClickListener { context.openVideoExpand(scanEntity.uri) {} }
    }

}