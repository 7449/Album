package com.album.ui.dialog

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class AlbumDialogUiBundle(
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
        var toolbarElevation: Float = 6F
) : Parcelable