package develop.file.gallery.ui.wechat.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.gallery.ui.wechat.R
import com.gallery.ui.wechat.databinding.WechatGalleryLayoutPrevItemBinding
import develop.file.gallery.compat.extensions.ContextCompat.openVideo
import develop.file.gallery.entity.ScanEntity
import develop.file.gallery.ui.wechat.extension.SizeCompat.toFileSize

internal class WeChatGalleryPrevItem @JvmOverloads constructor(
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

    fun update(entity: ScanEntity) {
        imageView.scaleType =
            if (entity.isVideo) ImageView.ScaleType.FIT_XY else ImageView.ScaleType.FIT_CENTER
        gifView.visibility = if (entity.isGif) View.VISIBLE else View.GONE
        gifView.text = context.getString(R.string.wechat_gallery_prev_gif_format)
            .format(entity.size.toFileSize())
        videoPlayView.visibility = if (entity.isVideo) View.VISIBLE else View.GONE
        videoPlayView.setOnClickListener { context.openVideo(entity.uri) {} }
    }

}