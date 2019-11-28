package com.gallery.ui

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class GalleryUiBundle(
        /**
         * 预览toolbar返回是否刷新数据
         */
        var preFinishRefresh: Boolean = true,
        /**
         * 预览back返回是否刷新数据
         */
        var preBackRefresh: Boolean = true,
        /**
         * 预览页选择之后是否销毁上一页面
         */
        var preSelectOkFinish: Boolean = true,
        /**
         * 状态栏颜色
         */
        var statusBarColor: Int = R.color.colorGalleryStatusBarColor,
        /**
         * toolbar背景色
         */
        var toolbarBackground: Int = R.color.colorGalleryToolbarBackground,
        /**
         * toolbar返回图标
         */
        var toolbarIcon: Int = R.drawable.ic_gallery_ui_toolbar_back,
        /**
         * toolbar返回图片颜色
         */
        var toolbarIconColor: Int = R.color.colorGalleryToolbarIconColor,
        /**
         * toolbar title
         */
        var toolbarText: Int = R.string.gallery_name,
        /**
         * toolbar title颜色
         */
        var toolbarTextColor: Int = R.color.colorGalleryToolbarTextColor,
        /**
         * toolbar elevation
         */
        var toolbarElevation: Float = 4F,
        /**
         * 底部背景色
         */
        var bottomViewBackground: Int = R.color.colorGalleryBottomViewBackground,
        /**
         * 底部文件目录文字大小
         */
        var bottomFinderTextSize: Float = 16F,
        /**
         * 底部文件目录View背景色
         */
        var bottomFinderTextBackground: Int = -1,
        /**
         * 底部文件目录文字颜色
         */
        var bottomFinderTextColor: Int = R.color.colorGalleryBottomFinderTextColor,
        /**
         * 底部文件目录图标
         */
        var bottomFinderTextCompoundDrawable: Int = R.drawable.ic_gallery_ui_finder,
        /**
         * 底部文件目录图标颜色
         */
        var bottomFinderTextDrawableColor: Int = R.color.colorGalleryBottomFinderTextDrawableColor,
        /**
         * 预览文字
         */
        var bottomPreViewText: Int = R.string.gallery_preview,
        /**
         * 预览文字大小
         */
        var bottomPreViewTextSize: Float = 16F,
        /**
         * 预览文字颜色
         */
        var bottomPreViewTextColor: Int = R.color.colorGalleryBottomPreViewTextColor,
        /**
         * 预览文字View背景色
         */
        var bottomPreviewTextBackground: Int = -1,
        /**
         * 选择文字
         */
        var bottomSelectText: Int = R.string.gallery_select,
        /**
         * 选择文字大小
         */
        var bottomSelectTextSize: Float = 16F,
        /**
         * 选择文字颜色
         */
        var bottomSelectTextColor: Int = R.color.colorGalleryBottomSelectTextColor,
        /**
         * 选择文字View背景色
         */
        var bottomSelectTextBackground: Int = -1,
        /**
         * 目录View宽度
         */
        var listPopupWidth: Int = 600,
        /**
         * 目录View h 偏移量
         */
        var listPopupHorizontalOffset: Int = 0,
        /**
         * 目录View w 偏移量
         */
        var listPopupVerticalOffset: Int = 0,
        /**
         * 目录View背景色
         */
        var listPopupBackground: Int = R.color.colorGalleryListPopupBackground,
        /**
         * 目录View字体颜色
         */
        var listPopupItemTextColor: Int = R.color.colorGalleryListPopupItemTextColor,
        /**
         * 预览页title
         */
        var preTitle: Int = R.string.preview_title,
        /**
         * 预览页ViewPager背景色
         */
        var preBackground: Int = R.color.colorGalleryPreviewBackground,
        /**
         * 预览页底部提示栏背景色
         */
        var preBottomViewBackground: Int = R.color.colorGalleryPreviewBottomViewBackground,
        /**
         * 预览页底部提示栏确认文字
         */
        var preBottomOkText: Int = R.string.preview_select,
        /**
         * 预览页底部提示栏确认文字颜色
         */
        var preBottomOkTextColor: Int = R.color.colorGalleryPreviewBottomViewOkColor,
        /**
         * 预览页底部提示栏数字文字
         */
        var preBottomCountTextColor: Int = R.color.colorGalleryPreviewBottomViewCountColor,
        /**
         * 预览页底部提示栏确认文字大小
         */
        var preBottomOkTextSize: Float = 16F,
        /**
         * 预览页底部提示栏数字文字大小
         */
        var preBottomCountTextSize: Float = 16F
) : Parcelable