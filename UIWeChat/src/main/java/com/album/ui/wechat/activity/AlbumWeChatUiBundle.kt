package com.album.ui.wechat.activity

import android.os.Parcelable
import com.album.ui.wechat.R
import kotlinx.android.parcel.Parcelize

@Parcelize
class AlbumWeChatUiBundle(
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
        var statusBarColor: Int = R.color.album_wechat_ui_white,
        /**
         * toolbar背景色
         */
        var toolbarBackground: Int = R.color.album_wechat_ui_white,
        /**
         * toolbar返回图标
         */
        var toolbarIcon: Int = R.drawable.ic_album_wechat_toolbar_back,
        /**
         * toolbar返回图片颜色
         */
        var toolbarIconColor: Int = R.color.album_wechat_ui_black,
        /**
         * toolbar title
         */
        var toolbarText: Int = R.string.album_wechat_title,
        /**
         * toolbar title颜色
         */
        var toolbarTextColor: Int = R.color.album_wechat_ui_black,
        /**
         * toolbar elevation
         */
        var toolbarElevation: Float = 0F
) : Parcelable