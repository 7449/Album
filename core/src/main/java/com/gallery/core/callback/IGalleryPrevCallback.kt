package com.gallery.core.callback

import android.content.Context
import androidx.annotation.Px
import androidx.kotlin.expand.toastExpand
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.ScrollState
import com.gallery.core.GalleryBundle
import com.gallery.core.R
import com.gallery.core.ui.fragment.PrevFragment
import com.gallery.scan.ScanEntity

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
    fun onClickCheckBoxMaxCount(context: Context, scanEntity: ScanEntity) {
        context.getString(R.string.gallery_check_max).toastExpand(context)
    }

    /**
     * 点击图片时该文件已被删除
     * 适用场景:在图片选择页面返回桌面打开相册删除某张图片
     * [PrevFragment.checkBoxClick]
     */
    fun onClickCheckBoxFileNotExist(context: Context, scanEntity: ScanEntity) {
        context.getString(R.string.gallery_prev_check_file_deleted).toastExpand(context)
    }

    /**
     * [PrevFragment.onActivityCreated]
     */
    fun onChangedCreated() {}

    /**
     * [PrevFragment.checkBoxClick]
     */
    fun onChangedCheckBox() {}
}