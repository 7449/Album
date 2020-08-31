package com.gallery.core.delegate

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.gallery.core.ui.fragment.ScanFragment
import com.gallery.scan.ScanEntity

interface IPrevDelegate {

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
     * 横竖屏保存数据
     */
    fun onSaveInstanceState(outState: Bundle)

    /**
     * 初始化
     */
    fun onCreate(savedInstanceState: Bundle?)

    /**
     * 更新数据
     */
    fun updateEntity(savedInstanceState: Bundle?, arrayList: ArrayList<ScanEntity>)

    /**
     * 如果自定义checkBox调用这个方法是比较简单的方法
     */
    fun checkBoxClick(checkBox: View)

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
     * 刷新单个Item
     */
    fun notifyItemChanged(position: Int)

    /**
     * 刷新全部数据
     */
    fun notifyDataSetChanged()

    /**
     * 获取预览页销毁时[Activity.setResult]的Bundle
     * 可作为参数传递给[ScanFragment.onUpdateResult]
     * 用于合并预览页改变的数据
     */
    fun resultBundle(isRefresh: Boolean): Bundle

    /**
     * 销毁
     */
    fun onDestroy()
}