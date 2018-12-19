package com.album.ui.widget

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager

class AlbumImageView : AppCompatImageView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measuredWidth, measuredWidth)
    }
}

class ExtendedViewPager : ViewPager {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun canScroll(v: View, checkV: Boolean, dx: Int, x: Int, y: Int): Boolean = (v as? TouchImageView)?.canScrollHorizontallyFroyo(-dx)
            ?: super.canScroll(v, checkV, dx, x, y)
}

class SimpleGridDivider(private val divider: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        val layoutManager = parent.layoutManager as GridLayoutManager
        val spanCount = layoutManager.spanCount
        val i = position % spanCount
        val first = position / spanCount + 1 == 1
        val top = if (first) divider / 2 else 0
        val left = if (i + 1 == 1) divider else divider / 2
        val right = if (i + 1 == spanCount) divider else divider / 2
        outRect.set(left, top, right, divider)
    }
}

class LoadMoreRecyclerView : RecyclerView {

    private var lastVisibleItemPosition: Int = 0
    private lateinit var loadingListener: LoadMoreListener

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    fun setLoadingListener(loadingListener: LoadMoreListener) {
        this.loadingListener = loadingListener
    }

    override fun onScrolled(dx: Int, dy: Int) {
        super.onScrolled(dx, dy)
        val layoutManager = layoutManager as GridLayoutManager
        lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
    }

    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)
        val visibleItemCount = layoutManager?.childCount ?: 0
        val totalItemCount = layoutManager?.itemCount ?: 0
        if (visibleItemCount > 0 && state == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition == totalItemCount - 1) {
            loadingListener.onLoadMore()
        }
    }

    interface LoadMoreListener {
        fun onLoadMore()
    }
}
