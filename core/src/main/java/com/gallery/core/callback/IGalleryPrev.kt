package com.gallery.core.callback

import android.app.Activity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.gallery.scan.ScanEntity

/**
 *
 */
interface IGalleryPrev {

    /**
     * [ViewPager2.getCurrentItem]
     */
    val currentItem: ScanEntity

    /**
     * 获取全部预览数据
     */
    val allItem: ArrayList<ScanEntity>

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
     * 当前item是否是选中状态
     */
    fun isCheckBox(position: Int): Boolean

    /**
     * [ViewPager2]跳转到指定页面
     */
    fun setCurrentItem(position: Int)

    /**
     * [ViewPager2]跳转到指定页面
     */
    fun setCurrentItem(position: Int, smoothScroll: Boolean)

    /**
     * 获取预览页销毁时[Activity.setResult]的Bundle
     */
    fun resultBundle(isRefresh: Boolean): Bundle

}