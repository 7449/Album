package develop.file.gallery.ui.wechat.extension

import android.view.Gravity
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.get
import androidx.core.view.postDelayed
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import develop.file.gallery.compat.extensions.ActivityCompat.galleryFragment
import develop.file.gallery.compat.extensions.ViewCompat.hide
import develop.file.gallery.compat.extensions.ViewCompat.show
import develop.file.gallery.ui.wechat.activity.WeChatGalleryActivity
import develop.file.gallery.ui.wechat.colorWhite
import develop.file.gallery.ui.wechat.extension.TimeCompat.formatTime
import develop.file.gallery.ui.wechat.size13
import develop.file.gallery.ui.wechat.widget.WeChatGalleryItem

internal object ViewCompat {

    internal fun FrameLayout.getCheckBoxView(): TextView {
        val checkBox = get(1) as TextView
        checkBox.gravity = Gravity.CENTER
        checkBox.setTextColor(colorWhite)
        checkBox.textSize = size13
        return checkBox
    }

    internal fun FrameLayout.createGalleryItem(width: Int, height: Int): WeChatGalleryItem {
        return if (tag is WeChatGalleryItem) {
            tag as WeChatGalleryItem
        } else {
            WeChatGalleryItem(context).apply {
                this@createGalleryItem.tag = this
                this@createGalleryItem.addView(this, 0, FrameLayout.LayoutParams(width, height))
            }
        }
    }

    internal fun WeChatGalleryActivity.scrollView(textView: TextView) {
        val currentFragment = galleryFragment ?: return
        currentFragment.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                recyclerView.layoutManager ?: return
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val position: Int = layoutManager.findFirstCompletelyVisibleItemPosition()
                if (dx == 0 && dy == 0) {
                    textView.hide()
                } else {
                    textView.show()
                }
                currentFragment.allItem.getOrNull(position)?.let {
                    textView.text = it.dateModified.formatTime()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                textView.postDelayed(1000) { textView.hide() }
            }
        })

    }

}