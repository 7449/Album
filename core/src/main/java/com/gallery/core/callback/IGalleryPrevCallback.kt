package com.gallery.core.callback

import androidx.annotation.Px
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.ScrollState
import com.gallery.core.GalleryBundle
import com.gallery.core.ui.fragment.PrevFragment

/**
 *
 */
interface IGalleryPrevCallback {

    /**
     * [ViewPager2.OnPageChangeCallback.onPageScrolled]
     */
    fun onPageScrolled(position: Int, positionOffset: Float, @Px positionOffsetPixels: Int) {}

    /**
     * [ViewPager2.OnPageChangeCallback.onPageSelected]
     */
    fun onPageSelected(position: Int) {}

    /**
     * [ViewPager2.OnPageChangeCallback.onPageScrollStateChanged]
     */
    fun onPageScrollStateChanged(@ScrollState state: Int) {}

    /**
     * 已达到选择最大数
     * [GalleryBundle.multipleMaxCount]
     */
    fun onClickCheckBoxMaxCount()

    /**
     * 点击图片时该文件已被删除
     * 适用场景:在图片选择页面返回桌面打开相册删除某张图片
     * [PrevFragment.checkBoxClick]
     */
    fun onClickCheckBoxFileNotExist()

    /**
     * [PrevFragment.onActivityCreated]
     */
    fun onChangedCreated()

    /**
     * [PrevFragment.checkBoxClick]
     */
    fun onChangedCheckBox()
}