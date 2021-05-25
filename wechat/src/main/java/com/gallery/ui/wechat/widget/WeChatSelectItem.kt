package com.gallery.ui.wechat.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.gallery.core.entity.ScanEntity
import com.gallery.core.extensions.hideExpand
import com.gallery.core.extensions.showExpand
import com.gallery.ui.wechat.databinding.LayoutSelectWechatItemBinding

class WeChatSelectItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val viewBinding: LayoutSelectWechatItemBinding =
        LayoutSelectWechatItemBinding.inflate(LayoutInflater.from(getContext()), this, true)

    init {
        setBackgroundColor(Color.BLACK)
    }

    val imageView: ImageView
        get() = viewBinding.selectWeChatImageView

    private val gifView: ImageView
        get() = viewBinding.selectWeChatGif

    private val videoView: ImageView
        get() = viewBinding.selectWeChatVideo

    private val view: View
        get() = viewBinding.selectWeChatView

    fun update(scanEntity: ScanEntity, idList: List<Long>, isPrev: Boolean) {
        gifView.visibility = if (scanEntity.isGif) View.VISIBLE else View.GONE
        videoView.visibility = if (scanEntity.isVideo) View.VISIBLE else View.GONE
        if (!isPrev) {
            view.hideExpand()
            return
        }
        idList.find { it == scanEntity.id }?.let {
            view.showExpand()
        } ?: view.hideExpand()
    }

}