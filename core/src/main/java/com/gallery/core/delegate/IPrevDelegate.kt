package com.gallery.core.delegate

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.gallery.core.entity.ScanEntity

interface IPrevDelegate {

    /*** 根View*/
    val rootView: View

    /*** 获取全部数据*/
    val allItem: ArrayList<ScanEntity>

    /*** 选中的数据*/
    val selectItem: ArrayList<ScanEntity>

    /*** 当前position*/
    val currentPosition: Int

    /*** [ViewPager2.getCurrentItem]*/
    val currentItem: ScanEntity
        get() = allItem[currentPosition]

    /*** 预览页选中的数据是否为空*/
    val isSelectEmpty: Boolean
        get() = selectItem.isEmpty()

    /*** 预览页选中的数据个数*/
    val selectCount: Int
        get() = selectItem.size

    /*** 预览页全部数据个数*/
    val itemCount: Int
        get() = allItem.size

    /*** 横竖屏保存数据*/
    fun onSaveInstanceState(outState: Bundle)

    /*** 初始化*/
    fun onCreate(savedInstanceState: Bundle?)

    /*** 更新数据*/
    fun updateEntity(savedInstanceState: Bundle?, arrayList: ArrayList<ScanEntity>)

    /*** 选中or未选中点击*/
    fun selectPictureClick(box: View)

    /*** 当前item是否是选中状态*/
    fun isSelected(position: Int): Boolean

    /*** [ViewPager2]跳转到指定页面*/
    fun setCurrentItem(position: Int) {
        setCurrentItem(position, false)
    }

    /*** [ViewPager2]跳转到指定页面*/
    fun setCurrentItem(position: Int, smoothScroll: Boolean)

    /*** 刷新单个Item*/
    fun notifyItemChanged(position: Int)

    /*** 刷新全部数据*/
    fun notifyDataSetChanged()

    /*** 获取预览销毁时[Activity.setResult]的Bundle* 可作为参数传递给[IScanDelegate.onUpdateResult]* 用于合并预览页改变的数据*/
    fun resultBundle(isRefresh: Boolean): Bundle

    /*** 销毁*/
    fun onDestroy()

}