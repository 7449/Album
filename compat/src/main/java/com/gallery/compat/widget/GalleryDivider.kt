package com.gallery.compat.widget

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * @author y
 * @create 2019/2/27
 */
class GalleryDivider(private val divider: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position: Int = parent.getChildAdapterPosition(view)
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            val spanCount: Int = layoutManager.spanCount
            val i: Int = position % spanCount
            val first: Boolean = position / spanCount + 1 == 1
            val top: Int = if (first) divider / 2 else 0
            val left: Int = if (i + 1 == 1) divider else divider / 2
            val right: Int = if (i + 1 == spanCount) divider else divider / 2
            outRect.set(left, top, right, divider)
        } else if (layoutManager is LinearLayoutManager) {
            outRect.set(divider / 2, divider / 2, divider / 2, divider / 2)
        }
    }
}