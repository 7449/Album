package com.album.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.ortiz.touchview.TouchImageView

/**
 * @author y
 * @create 2019/2/27
 */
class ExtendedViewPager @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : ViewPager(context, attrs) {
    override fun canScroll(v: View, checkV: Boolean, dx: Int, x: Int, y: Int): Boolean = (v as? TouchImageView)?.canScrollHorizontallyFroyo(-dx)
            ?: super.canScroll(v, checkV, dx, x, y)
}