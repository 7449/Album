package com.album.widget

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * @author y
 * @create 2019/2/27
 */
internal class LoadMoreRecyclerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RecyclerView(context, attrs, defStyleAttr) {

    private var lastVisibleItemPosition: Int = 0
    private lateinit var loadingListener: LoadMoreListener

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
        if (visibleItemCount > 0 && state == SCROLL_STATE_IDLE && lastVisibleItemPosition == totalItemCount - 1) {
            loadingListener.onLoadMore()
        }
    }

    interface LoadMoreListener {
        fun onLoadMore()
    }
}