package com.gallery.core.callback

import android.app.Activity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.gallery.core.GalleryBundle
import com.gallery.core.ui.fragment.PrevFragment
import com.gallery.scan.ScanEntity

/**
 *
 */
interface IGalleryPrev {

    companion object {

        /**
         * [GalleryBundle] Bundle key
         * [Bundle.putParcelable]
         * 配置文件
         */
        const val PREV_START_CONFIG = "prevStartConfig"

        /**
         * [ArrayList<ScanEntity>] Bundle key
         * [Bundle.putParcelableArrayList]
         * 全部数据
         */
        const val PREV_START_ALL = "prevStartAllEntity"

        /**
         * [ArrayList<ScanEntity>] Bundle key
         * [Bundle.putParcelableArrayList]
         * 选中的数据
         */
        const val PREV_START_SELECT = "prevStartSelectEntities"

        /**
         * [ArrayList<ScanEntity>] Bundle key
         * [Bundle.putInt]
         * 默认的position,[ViewPager2.setCurrentItem]
         */
        const val PREV_START_POSITION = "prevStartPosition"

        /**
         * [ArrayList<ScanEntity>] Bundle key
         * [Bundle.putParcelableArrayList]
         * 预览页返回选中的数据
         */
        const val PREV_RESULT_SELECT = "prevResultSelectEntities"

        /**
         * [ArrayList<ScanEntity>] Bundle key
         * [Bundle.putBoolean]
         * 预览页返回是否刷新
         */
        const val PREV_RESULT_REFRESH = "prevResultRefresh"

        /**
         * 启动[PrevFragment] 的 request_code
         */
        const val PREV_START_REQUEST_CODE = 462
    }

    /**
     * [ViewPager2.getCurrentItem]
     */
    val currentItem: ScanEntity

    /**
     * 预览页选中的数据
     */
    val selectEntities: ArrayList<ScanEntity>

    /**
     * 预览页选中的数据是否为空
     */
    val selectEmpty: Boolean

    /**
     * 预览页选中的数据个数
     */
    val selectCount: Int

    /**
     * 预览页全部数据个数
     */
    val itemCount: Int

    /**
     * 当前position
     * [ViewPager2.getCurrentItem]
     */
    val currentPosition: Int

    /**
     * [ViewPager2]跳转到某个页面
     */
    fun setCurrentItem(position: Int)

    /**
     * [ViewPager2]跳转到某个页面
     */
    fun setCurrentItem(position: Int, flag: Boolean)

    /**
     * 获取预览页销毁时[Activity.setResult]的Bundle
     */
    fun resultBundle(isRefresh: Boolean): Bundle

}