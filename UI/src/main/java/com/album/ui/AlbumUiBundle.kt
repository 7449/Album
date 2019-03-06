package com.album.ui

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class AlbumUiBundle(
        /**
         * 预览toolbar返回是否刷新数据
         */
        var previewFinishRefresh: Boolean = true,
        /**
         * 预览back返回是否刷新数据
         */
        var previewBackRefresh: Boolean = true,
        /**
         * 预览页选择之后是否销毁上一页面
         */
        var previewSelectOkFinish: Boolean = true,
        /**
         * 状态栏颜色
         */
        var statusBarColor: Int = R.color.colorAlbumStatusBarColor,
        /**
         * toolbar背景色
         */
        var toolbarBackground: Int = R.color.colorAlbumToolbarBackground,
        /**
         * toolbar返回图标
         */
        var toolbarIcon: Int = R.drawable.ic_action_back_day,
        /**
         * toolbar返回图片颜色
         */
        var toolbarIconColor: Int = R.color.colorAlbumToolbarIconColor,
        /**
         * toolbar title
         */
        var toolbarText: Int = R.string.album_name,
        /**
         * toolbar title颜色
         */
        var toolbarTextColor: Int = R.color.colorAlbumToolbarTextColor,
        /**
         * toolbar elevation
         */
        var toolbarElevation: Float = 6F,
        /**
         * 底部背景色
         */
        var bottomViewBackground: Int = R.color.colorAlbumBottomViewBackground,
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
        var bottomFinderTextColor: Int = R.color.colorAlbumBottomFinderTextColor,
        /**
         * 底部文件目录图标
         */
        var bottomFinderTextCompoundDrawable: Int = R.drawable.ic_action_album_finder_day,
        /**
         * 底部文件目录图标颜色
         */
        var bottomFinderTextDrawableColor: Int = R.color.colorAlbumBottomFinderTextDrawableColor,
        /**
         * 预览文字
         */
        var bottomPreViewText: Int = R.string.album_preview,
        /**
         * 预览文字大小
         */
        var bottomPreViewTextSize: Float = 16F,
        /**
         * 预览文字颜色
         */
        var bottomPreViewTextColor: Int = R.color.colorAlbumBottomPreViewTextColor,
        /**
         * 预览文字View背景色
         */
        var bottomPreviewTextBackground: Int = -1,
        /**
         * 选择文字
         */
        var bottomSelectText: Int = R.string.album_select,
        /**
         * 选择文字大小
         */
        var bottomSelectTextSize: Float = 16F,
        /**
         * 选择文字颜色
         */
        var bottomSelectTextColor: Int = R.color.colorAlbumBottomSelectTextColor,
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
        var listPopupBackground: Int = R.color.colorAlbumListPopupBackground,
        /**
         * 目录View字体颜色
         */
        var listPopupItemTextColor: Int = R.color.colorAlbumListPopupItemTextColor,
        /**
         * 预览页title
         */
        var previewTitle: Int = R.string.preview_title,
        /**
         * 预览页ViewPager背景色
         */
        var previewBackground: Int = R.color.colorAlbumContentViewBackgroundNight,
        /**
         * 预览页底部提示栏背景色
         */
        var previewBottomViewBackground: Int = R.color.colorAlbumPreviewBottomViewBackground,
        /**
         * 预览页底部提示栏确认文字
         */
        var previewBottomOkText: Int = R.string.preview_select,
        /**
         * 预览页底部提示栏确认文字颜色
         */
        var previewBottomOkTextColor: Int = R.color.colorAlbumPreviewBottomViewOkColor,
        /**
         * 预览页底部提示栏数字文字
         */
        var previewBottomCountTextColor: Int = R.color.colorAlbumPreviewBottomViewCountColor,
        /**
         * 预览页底部提示栏确认文字大小
         */
        var previewBottomOkTextSize: Float = 16F,
        /**
         * 预览页底部提示栏数字文字大小
         */
        var previewBottomCountTextSize: Float = 16F
) : Parcelable