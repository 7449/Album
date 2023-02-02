package com.gallery.core.callback

import android.os.Bundle
import androidx.annotation.Px
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.ScrollState
import com.gallery.core.GalleryConfigs
import com.gallery.core.delegate.IPrevDelegate
import com.gallery.core.entity.ScanEntity

interface IGalleryPrevCallback {

    /*** [ViewPager2.OnPageChangeCallback.onPageScrolled]*/
    fun onPageScrolled(position: Int, positionOffset: Float, @Px positionOffsetPixels: Int)

    /*** [ViewPager2.OnPageChangeCallback.onPageSelected]*/
    fun onPageSelected(position: Int)

    /*** [ViewPager2.OnPageChangeCallback.onPageScrollStateChanged]*/
    fun onPageScrollStateChanged(@ScrollState state: Int)

    /*** [IPrevDelegate.onCreate]触发，必须实现*/
    fun onPrevCreated(delegate: IPrevDelegate, configs: GalleryConfigs, saveState: Bundle?)

    /*** 选择图片时该文件已被删除*/
    fun onSelectMultipleFileNotExist(entity: ScanEntity)

    /*** 已达到选择最大数*/
    fun onSelectMultipleMaxCount()

    /*** [IPrevDelegate.selectPictureClick]*/
    fun onSelectMultipleFileChanged(position: Int, entity: ScanEntity)

}