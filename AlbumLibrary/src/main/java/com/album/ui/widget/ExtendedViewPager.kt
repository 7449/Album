package com.album.ui.widget

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.View


class ExtendedViewPager : ViewPager {

    constructor(context: Context) : super(context) 

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) 

    override fun canScroll(v: View, checkV: Boolean, dx: Int, x: Int, y: Int): Boolean {
        return (v as? TouchImageView)?.canScrollHorizontallyFroyo(-dx)
                ?: super.canScroll(v, checkV, dx, x, y)
    }

}
