package com.album.ui.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

/**
 * by y on 11/08/2017.
 */

class AlbumImageView : AppCompatImageView {
    constructor(context: Context) : super(context) 

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) 

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) 

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measuredWidth, measuredWidth)
    }
}
