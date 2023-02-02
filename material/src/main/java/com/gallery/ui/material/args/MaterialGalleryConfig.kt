package com.gallery.ui.material.args

import android.graphics.Color
import android.os.Parcelable
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.gallery.compat.activity.args.GalleryCompatArgs
import com.gallery.ui.material.R
import kotlinx.parcelize.Parcelize

/**
 * 通过[GalleryCompatArgs.gap]或者[GalleryCompatArgs.gap]获取
 */
@Parcelize
data class MaterialGalleryConfig(
    /*** toolbar title*/
    val toolbarTextConfig: MaterialTextConfig = MaterialTextConfig("图片选择", 0f, Color.WHITE),
    /*** toolbar elevation*/
    val toolbarElevation: Float = 4F,
    /*** toolbar返回图标*/
    @DrawableRes val toolbarIcon: Int = R.drawable.ic_material_gallery_back,
    /*** toolbar背景色*/
    @ColorInt val toolbarBackground: Int = Color.parseColor("#FF04B3E4"),
    /*** 状态栏颜色*/
    @ColorInt val statusBarColor: Int = Color.parseColor("#FF02A5D2"),
    /*** RootView背景色*/
    @ColorInt val galleryRootBackground: Int = Color.WHITE,
    /*** 底部背景色*/
    @ColorInt val bottomViewBackground: Int = Color.parseColor("#FF02A5D2"),
    /*** 文件目录文字*/
    val finderTextConfig: MaterialTextConfig = MaterialTextConfig("", 16F, Color.WHITE),
    /*** 文件目录图标*/
    @DrawableRes val finderIcon: Int = R.drawable.ic_material_gallery_finder,
    /*** 预览文字*/
    val prevTextConfig: MaterialTextConfig = MaterialTextConfig("预览", 16F, Color.WHITE),
    /*** 选择文字*/
    val selectTextConfig: MaterialTextConfig = MaterialTextConfig("确定", 16F, Color.WHITE),
    /*** 目录View宽度*/
    val listPopupWidth: Int = 600,
    /*** 目录View h 偏移量*/
    val listPopupHorizontalOffset: Int = 0,
    /*** 目录View w 偏移量*/
    val listPopupVerticalOffset: Int = 0,
    /*** 目录View背景色*/
    @ColorInt val finderItemBackground: Int = Color.WHITE,
    /*** 目录View字体颜色*/
    @ColorInt val finderItemTextColor: Int = Color.BLACK,
    /*** 预览页底部确认*/
    val preBottomOkConfig: MaterialTextConfig = MaterialTextConfig("确定", 16F, Color.WHITE),
    /*** 预览页底部选择数*/
    val preBottomCountConfig: MaterialTextConfig = MaterialTextConfig("", 16F, Color.WHITE),
) : Parcelable