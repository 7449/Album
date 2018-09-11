package com.album.ui.widget

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet

/**
 * by y on 22/08/2017.
 */

class LoadMoreRecyclerView : RecyclerView {


    private var lastVisibleItemPosition: Int = 0
    private var loadingListener: LoadMoreListener? = null

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
        val layoutManager = layoutManager
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        if (loadingListener != null && visibleItemCount > 0 &&
                state == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition == totalItemCount - 1) {
            loadingListener!!.onLoadMore()
        }
    }


    interface LoadMoreListener {
        fun onLoadMore()
    }
}
