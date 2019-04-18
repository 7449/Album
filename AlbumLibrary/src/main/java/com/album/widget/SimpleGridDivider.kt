package com.album.widget

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * @author y
 * @create 2019/2/27
 */
internal class SimpleGridDivider(private val divider: Int) : RecyclerView.ItemDecoration() {
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