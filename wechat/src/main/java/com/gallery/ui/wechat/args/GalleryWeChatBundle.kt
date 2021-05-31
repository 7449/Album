package com.gallery.ui.wechat.args

import android.graphics.Color
import android.os.Parcelable
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.gallery.compat.activity.args.GalleryCompatArgs
import kotlinx.parcelize.Parcelize

/**
 * 通过[GalleryCompatArgs.customBundle]以及[GalleryCompatArgs.customBundle]获取
 */
@Parcelize
data class GalleryWeChatBundle(
    /**
     * 状态栏颜色
     */
    @ColorInt
    val statusBarColor: Int = Color.parseColor("#FF02A5D2"),
    /**
     * toolbar背景色
     */
    @ColorInt
    val toolbarBackground: Int = Color.parseColor("#FF04B3E4"),
    /**
     * 文件目录文字大小
     */
    val finderTextSize: Float = 16F,
    /**
     * 文件目录文字颜色
     */
    @ColorInt
    val finderTextColor: Int = Color.WHITE,
    /**
     * 文件目录图标
     */
    @DrawableRes
    val finderTextCompoundDrawable: Int = -1,
    /**
     * 文件目录图标颜色
     */
    @ColorInt
    val finderTextDrawableColor: Int = Color.WHITE,
    /**
     * 预览文字
     */
    val preViewText: String = "预览",
    /**
     * 预览文字大小
     */
    val preViewTextSize: Float = 16F,
    /**
     * 选择文字
     */
    val selectText: String = "确定",
    /**
     * 选择文字大小
     */
    val selectTextSize: Float = 16F,
    /**
     * 底部背景色
     */
    @ColorInt
    val bottomViewBackground: Int = Color.parseColor("#FF02A5D2"),
    /**
     * 目录View背景色
     */
    @ColorInt
    val finderItemBackground: Int = Color.WHITE,
    /**
     * 目录View字体颜色
     */
    @ColorInt
    val finderItemTextColor: Int = Color.parseColor("#FF3D4040"),
    /**
     * 目录View字体个数颜色
     */
    @ColorInt
    val finderItemTextCountColor: Int = Color.parseColor("#FF3D4040"),
    /**
     * 预览页底部提示栏背景色
     */
    @ColorInt
    val preBottomViewBackground: Int = Color.parseColor("#FF02A5D2"),
    /**
     * 预览页底部提示栏确认文字
     */
    val preBottomOkText: String = "确定",
    /**
     * 预览页底部提示栏确认文字颜色
     */
    @ColorInt
    val preBottomOkTextColor: Int = Color.WHITE,
    /**
     * 预览页底部提示栏确认文字大小
     */
    val preBottomOkTextSize: Float = 16F,
    /**
     * 限制的视频时长
     */
    val videoMaxDuration: Int = 300000,
    /**
     * 全部视频的Finder名称
     */
    val videoAllFinderName: String = "全部视频",
    /**
     * 是否是点击预览进入
     * 传参忽略,只是为了传递预览页所需的数据
     */
    val isPrev: Boolean = false,
    /**
     * 是否选择原图
     * 传参忽略,只是为了传递预览页所需的数据
     */
    val fullImageSelect: Boolean = false,
) : Parcelable