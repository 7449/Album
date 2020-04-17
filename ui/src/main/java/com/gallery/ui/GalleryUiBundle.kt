package com.gallery.ui

import android.os.Parcelable
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kotlinx.android.parcel.Parcelize

@Parcelize
class GalleryUiBundle(
        /**
         * 预览toolbar返回是否刷新数据
         */
        val preFinishRefresh: Boolean = true,
        /**
         * 预览back返回是否刷新数据
         */
        val preBackRefresh: Boolean = true,
        /**
         * 预览页选择之后是否销毁上一页面
         */
        val preSelectOkFinish: Boolean = true,
        /**
         * 状态栏颜色
         */
        @ColorRes
        val statusBarColor: Int = R.color.colorGalleryStatusBarColor,
        /**
         * toolbar背景色
         */
        @ColorRes
        val toolbarBackground: Int = R.color.colorGalleryToolbarBackground,
        /**
         * toolbar返回图标
         */
        @DrawableRes
        val toolbarIcon: Int = R.drawable.ic_gallery_ui_toolbar_back,
        /**
         * toolbar返回图片颜色
         */
        @ColorRes
        val toolbarIconColor: Int = R.color.colorGalleryToolbarIconColor,
        /**
         * toolbar title
         */
        val toolbarText: Int = R.string.gallery_name,
        /**
         * toolbar title颜色
         */
        @ColorRes
        val toolbarTextColor: Int = R.color.colorGalleryToolbarTextColor,
        /**
         * toolbar elevation
         */
        val toolbarElevation: Float = 4F,
        /**
         * 底部背景色
         */
        @ColorRes
        val bottomViewBackground: Int = R.color.colorGalleryBottomViewBackground,
        /**
         * 底部文件目录文字大小
         */
        val bottomFinderTextSize: Float = 16F,
        /**
         * 底部文件目录View背景色
         */
        @ColorRes
        val bottomFinderTextBackground: Int = View.NO_ID,
        /**
         * 底部文件目录文字颜色
         */
        @ColorRes
        val bottomFinderTextColor: Int = R.color.colorGalleryBottomFinderTextColor,
        /**
         * 底部文件目录图标
         */
        @DrawableRes
        val bottomFinderTextCompoundDrawable: Int = R.drawable.ic_gallery_ui_finder,
        /**
         * 底部文件目录图标颜色
         */
        @ColorRes
        val bottomFinderTextDrawableColor: Int = R.color.colorGalleryBottomFinderTextDrawableColor,
        /**
         * 预览文字
         */
        val bottomPreViewText: Int = R.string.gallery_preview,
        /**
         * 预览文字大小
         */
        val bottomPreViewTextSize: Float = 16F,
        /**
         * 预览文字颜色
         */
        @ColorRes
        val bottomPreViewTextColor: Int = R.color.colorGalleryBottomPreViewTextColor,
        /**
         * 预览文字View背景色
         */
        val bottomPreviewTextBackground: Int = -1,
        /**
         * 选择文字
         */
        val bottomSelectText: Int = R.string.gallery_select,
        /**
         * 选择文字大小
         */
        val bottomSelectTextSize: Float = 16F,
        /**
         * 选择文字颜色
         */
        @ColorRes
        val bottomSelectTextColor: Int = R.color.colorGalleryBottomSelectTextColor,
        /**
         * 选择文字View背景色
         */
        val bottomSelectTextBackground: Int = -1,
        /**
         * 目录View宽度
         */
        val listPopupWidth: Int = 600,
        /**
         * 目录View h 偏移量
         */
        val listPopupHorizontalOffset: Int = 0,
        /**
         * 目录View w 偏移量
         */
        val listPopupVerticalOffset: Int = 0,
        /**
         * 目录View背景色
         */
        @ColorRes
        val listPopupBackground: Int = R.color.colorGalleryListPopupBackground,
        /**
         * 目录View字体颜色
         */
        @ColorRes
        val listPopupItemTextColor: Int = R.color.colorGalleryListPopupItemTextColor,
        /**
         * 预览页title
         */
        val preTitle: Int = R.string.preview_title,
        /**
         * 预览页ViewPager背景色
         */
        @ColorRes
        val preBackground: Int = R.color.colorGalleryPreviewBackground,
        /**
         * 预览页底部提示栏背景色
         */
        @ColorRes
        val preBottomViewBackground: Int = R.color.colorGalleryPreviewBottomViewBackground,
        /**
         * 预览页底部提示栏确认文字
         */
        @StringRes
        val preBottomOkText: Int = R.string.preview_select,
        /**
         * 预览页底部提示栏确认文字颜色
         */
        @ColorRes
        val preBottomOkTextColor: Int = R.color.colorGalleryPreviewBottomViewOkColor,
        /**
         * 预览页底部提示栏数字文字
         */
        @ColorRes
        val preBottomCountTextColor: Int = R.color.colorGalleryPreviewBottomViewCountColor,
        /**
         * 预览页底部提示栏确认文字大小
         */
        val preBottomOkTextSize: Float = 16F,
        /**
         * 预览页底部提示栏数字文字大小
         */
        val preBottomCountTextSize: Float = 16F
) : Parcelable