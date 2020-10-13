package com.gallery.core.callback

import android.content.Context
import androidx.annotation.Px
import androidx.kotlin.expand.text.safeToastExpand
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.ScrollState
import com.gallery.core.GalleryBundle
import com.gallery.core.R
import com.gallery.core.delegate.IPrevDelegate
import com.gallery.core.delegate.entity.ScanEntity

/**
 *
 */
interface IGalleryPrevCallback {

    /**
     * [IPrevDelegate.onCreate]触发
     * 预览涉及到了扫描图库数据库,所以这个方法在
     * 点击item进入预览的情况下不会及时触发
     */
    fun onPrevCreated() {}

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
     * 点击图片时该文件已被删除
     * 适用场景:在图片选择页面返回桌面打开相册删除某张图片
     * [IPrevDelegate.checkBoxClick]
     */
    fun onClickCheckBoxFileNotExist(context: Context, galleryBundle: GalleryBundle, scanEntity: ScanEntity) {
        context.getString(R.string.gallery_prev_check_file_deleted).safeToastExpand(context)
    }

    /**
     * 已达到选择最大数
     * [GalleryBundle.multipleMaxCount]
     */
    fun onClickCheckBoxMaxCount(context: Context, galleryBundle: GalleryBundle, scanEntity: ScanEntity) {
        context.getString(R.string.gallery_check_max).safeToastExpand(context)
    }

    /**
     * [IPrevDelegate.checkBoxClick]
     */
    fun onChangedCheckBox() {}
}