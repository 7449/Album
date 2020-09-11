package com.gallery.ui.adapter

import android.view.View
import android.widget.FrameLayout
import com.gallery.scan.args.ScanMinimumEntity
import com.gallery.ui.GalleryUiBundle
import com.gallery.ui.activity.GalleryBaseActivity

interface GalleryFinderAdapter {

    interface AdapterFinderListener {
        /** 文件夹图片加载 */
        fun onGalleryFinderThumbnails(finderEntity: ScanMinimumEntity, container: FrameLayout)

        /** item点击 */
        fun onGalleryAdapterItemClick(view: View, position: Int, item: ScanMinimumEntity)
    }

    /** adapter 初始化 */
    fun adapterInit(activity: GalleryBaseActivity, uiBundle: GalleryUiBundle, anchorView: View?)

    /** 注册Adapter回调 */
    fun setOnAdapterFinderListener(listener: AdapterFinderListener)

    /** 更新文件夹数据 */
    fun finderUpdate(finderList: ArrayList<ScanMinimumEntity>)

    /** 显示Adapter */
    fun show()

    /** 隐藏Adapter  */
    fun hide()

}
