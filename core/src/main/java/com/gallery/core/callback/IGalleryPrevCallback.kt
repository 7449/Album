package com.gallery.core.callback

import android.content.Context
import android.os.Bundle
import androidx.annotation.Px
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.ScrollState
import com.gallery.core.GalleryConfigs
import com.gallery.core.delegate.IPrevDelegate
import com.gallery.core.entity.ScanEntity

interface IGalleryPrevCallback {

    /**
     * [ViewPager2.OnPageChangeCallback.onPageScrolled]
     */
    fun onPageScrolled(position: Int, positionOffset: Float, @Px positionOffsetPixels: Int)

    /**
     * [ViewPager2.OnPageChangeCallback.onPageSelected]
     */
    fun onPageSelected(position: Int)

    /**
     * [ViewPager2.OnPageChangeCallback.onPageScrollStateChanged]
     */
    fun onPageScrollStateChanged(@ScrollState state: Int)

    /**
     * [IPrevDelegate.onCreate]触发，必须实现
     */
    fun onPrevCreated(delegate: IPrevDelegate, configs: GalleryConfigs, saveState: Bundle?)

    /**
     * 点击图片时该文件已被删除
     * 适用场景:在图片选择页面返回桌面打开相册删除某张图片
     * [IPrevDelegate.selectPictureClick]
     */
    fun onClickItemFileNotExist(context: Context, configs: GalleryConfigs, scanEntity: ScanEntity)

    /**
     * 已达到选择最大数
     * [GalleryConfigs.maxCount]
     */
    fun onClickItemMaxCount(context: Context, configs: GalleryConfigs, scanEntity: ScanEntity)

    /**
     * [IPrevDelegate.selectPictureClick]
     */
    fun onCheckBoxChanged()

}