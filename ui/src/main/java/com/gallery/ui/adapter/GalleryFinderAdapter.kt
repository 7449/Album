package com.gallery.ui.adapter

import android.view.View
import android.widget.FrameLayout
import com.gallery.scan.ScanEntity
import com.gallery.ui.GalleryUiBundle
import com.gallery.ui.activity.GalleryBaseActivity

interface GalleryFinderAdapter {

    interface AdapterFinderListener {

        /**
         * ui数据
         */
        val adapterGalleryUiBundle: GalleryUiBundle

        /**
         * 文件夹图片加载
         */
        fun onGalleryFinderThumbnails(finderEntity: ScanEntity, container: FrameLayout)

        /**
         * item点击
         */
        fun onGalleryAdapterItemClick(view: View, position: Int, item: ScanEntity)
    }

    /**
     * 初始化Adapter
     */
    fun onGalleryFinderInit(context: GalleryBaseActivity, anchor: View?)

    /**
     * 显示Adapter
     */
    fun onGalleryFinderShow()

    /**
     * 隐藏Adapter
     */
    fun onGalleryFinderHide()

    /**
     * 更新文件夹数据
     */
    fun onGalleryFinderUpdate(finderList: ArrayList<ScanEntity>)

    /**
     * 注册Adapter回调
     */
    fun setOnAdapterFinderListener(adapterFinderListener: AdapterFinderListener)

}
