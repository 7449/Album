package com.gallery.ui.wechat.args

import android.graphics.Color
import android.os.Parcelable
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.gallery.compat.activity.args.GalleryCompatArgs
import com.gallery.ui.wechat.R
import com.gallery.ui.wechat.rgb19
import com.gallery.ui.wechat.rgb38
import kotlinx.parcelize.Parcelize

/**
 * 通过[GalleryCompatArgs.gap]或者[GalleryCompatArgs.gap]获取
 */
@Parcelize
data class WeChatGalleryConfig(
    /*** 状态栏颜色*/
    @ColorInt
    val statusBarColor: Int = rgb38,
    /*** toolbar背景色*/
    @ColorInt
    val toolbarBackground: Int = rgb38,
    /*** RootView背景色*/
    @ColorInt
    val galleryRootBackground: Int = rgb38,
    /*** 文件目录文字大小*/
    val finderTextSize: Float = 14f,
    /*** 文件目录文字颜色*/
    @ColorInt
    val finderTextColor: Int = Color.WHITE,
    /*** 文件目录图标*/
    @DrawableRes
    val finderTextCompoundDrawable: Int = R.drawable.ic_wechat_gallery_finder_action,
    /*** 文件目录图标颜色*/
    @ColorInt
    val finderTextDrawableColor: Int = Color.WHITE,
    /*** 预览文字*/
    val preViewText: String = "预览",
    /*** 预览文字大小*/
    val preViewTextSize: Float = 14f,
    /*** 选择文字*/
    val selectText: String = "发送",
    /*** 选择文字大小*/
    val selectTextSize: Float = 12f,
    /*** 底部背景色*/
    @ColorInt
    val bottomViewBackground: Int = rgb19,
    /*** 目录View背景色*/
    @ColorInt
    val finderItemBackground: Int = rgb38,
    /*** 目录View字体颜色*/
    @ColorInt
    val finderItemTextColor: Int = Color.WHITE,
    /*** 目录View字体个数颜色*/
    @ColorInt
    val finderItemTextCountColor: Int = Color.parseColor("#767676"),
    /*** 预览背景色*/
    @ColorInt
    val prevRootBackground: Int = Color.BLACK,
    /*** 预览页底部提示栏背景色*/
    @ColorInt
    val preBottomViewBackground: Int = rgb38,
    /*** 预览页底部提示栏确认文字*/
    val preBottomOkText: String = "选择",
    /*** 预览页底部提示栏确认文字颜色*/
    @ColorInt
    val preBottomOkTextColor: Int = Color.WHITE,
    /*** 预览页底部提示栏确认文字大小*/
    val preBottomOkTextSize: Float = 14f,
    /*** 限制的视频时长*/
    val videoMaxDuration: Int = 500000,
    /*** 全部视频的Finder名称*/
    val videoAllFinderName: String = "全部视频",
    /*** 是否是点击预览进入* 传参忽略,只是为了传递预览页所需的数据*/
    val isPrev: Boolean = false,
    /*** 是否选择原图* 传参忽略,只是为了传递预览页所需的数据*/
    val fullImageSelect: Boolean = false,
) : Parcelable