package com.gallery.compat

import android.graphics.Color
import android.os.Parcelable
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import kotlinx.parcelize.Parcelize

@Parcelize
data class GalleryCompatBundle(
    /**
     * 分割线宽度
     */
    val dividerWidth: Int = 8,
    /**
     * 滑动方向
     */
    val orientation: Int = RecyclerView.VERTICAL,
    /**
     * 列表管理器
     */
    val layoutManager: LayoutManagerTypes = LayoutManagerTypes.GRID,
    /**
     * 预览toolbar返回是否刷新数据
     */
    val preFinishRefresh: Boolean = true,
    /**
     * 预览back返回是否刷新数据
     */
    val preBackRefresh: Boolean = true,
    /**
     * RootView背景色
     */
    @ColorInt
    val galleryRootBackground: Int = Color.WHITE,
    /**
     * 预览背景色
     */
    @ColorInt
    val prevRootBackground: Int = Color.WHITE,
) : Parcelable