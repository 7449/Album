package com.gallery.core.callback

import android.content.Context
import android.os.Bundle
import androidx.annotation.Px
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.ScrollState
import com.gallery.core.GalleryBundle
import com.gallery.core.R
import com.gallery.core.delegate.IPrevDelegate
import com.gallery.core.entity.ScanEntity
import com.gallery.core.extensions.safeToastExpand

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
     * [IPrevDelegate.onCreate]触发
     */
    fun onPrevCreated(
        delegate: IPrevDelegate,
        bundle: GalleryBundle,
        savedInstanceState: Bundle?
    ) {
    }

    /**
     * 点击图片时该文件已被删除
     * 适用场景:在图片选择页面返回桌面打开相册删除某张图片
     * [IPrevDelegate.itemViewClick]
     */
    fun onClickItemFileNotExist(
        context: Context,
        bundle: GalleryBundle,
        scanEntity: ScanEntity
    ) {
        context.getString(R.string.gallery_prev_check_file_deleted).safeToastExpand(context)
    }

    /**
     * 已达到选择最大数
     * [GalleryBundle.multipleMaxCount]
     */
    fun onClickItemBoxMaxCount(
        context: Context,
        bundle: GalleryBundle,
        scanEntity: ScanEntity
    ) {
        context.getString(R.string.gallery_check_max).safeToastExpand(context)
    }

    /**
     * [IPrevDelegate.itemViewClick]
     */
    fun onChangedCheckBox() {}

}