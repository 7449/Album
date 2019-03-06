package com.album.ui.wechat.activity

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import com.album.ui.wechat.R
import kotlinx.android.synthetic.main.album_wechat_image_view.view.*

/**
 * @author y
 * @create 2019/3/6
 */
class AlbumWeChatImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        val inflate = View.inflate(context, R.layout.album_wechat_image_view, null)
        addView(inflate)
    }

    fun imageView(): AppCompatImageView {
        return album_wechat_image_view
    }

    fun gifTipView(): AppCompatImageView {
        return album_wechat_gif
    }
}