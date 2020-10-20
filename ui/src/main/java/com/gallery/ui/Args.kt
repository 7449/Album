package com.gallery.ui

import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.gallery.core.GalleryBundle
import com.gallery.core.PrevArgs
import com.gallery.core.delegate.entity.ScanEntity
import com.gallery.core.delegate.impl.PrevDelegateImpl
import com.gallery.ui.result.CropType
import com.gallery.ui.result.FinderType
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UIGallerySaveArgs(
        val finderName: String,
        val finderList: ArrayList<ScanEntity>,
) : Parcelable {
    companion object {
        private const val Key = "uiGallerySaveArgs"

        fun newSaveInstance(finderName: String, finderList: ArrayList<ScanEntity>): UIGallerySaveArgs {
            return UIGallerySaveArgs(finderName, finderList)
        }

        fun UIGallerySaveArgs.putArgs(bundle: Bundle = Bundle()): Bundle {
            bundle.putParcelable(Key, this)
            return bundle
        }

        val Bundle.uiGallerySaveArgs
            get() = getParcelable<UIGallerySaveArgs>(Key)
    }
}

@Parcelize
data class UIGalleryArgs(
        val galleryBundle: GalleryBundle,
        val galleryUiBundle: GalleryUiBundle,
        val galleryOption: Bundle,
        val galleryPrevOption: Bundle,
) : Parcelable {
    companion object {
        private const val Key = "uiGalleryArgs"

        fun UIGalleryArgs.putArgs(bundle: Bundle = Bundle()): Bundle {
            bundle.putParcelable(Key, this)
            return bundle
        }

        val Bundle.uiGalleryArgsOrDefault
            get() = getParcelable(Key)
                    ?: UIGalleryArgs(GalleryBundle(), GalleryUiBundle(), Bundle(), Bundle())
    }
}

@Parcelize
data class UIPrevArgs(
        /**
         * UI配置参数
         */
        val uiBundle: GalleryUiBundle,
        /**
         * 预览页[PrevDelegateImpl]需要的数据
         */
        val prevArgs: PrevArgs,
        /**
         * 暂存Bundle,用于自定义布局时[GalleryUiBundle]无法满足需要配置时携带数据
         */
        val option: Bundle,
) : Parcelable {
    companion object {
        private const val Key = "uiPrevArgs"

        fun UIPrevArgs.putPrevArgs(bundle: Bundle = Bundle()): Bundle {
            bundle.putParcelable(Key, this)
            return bundle
        }

        val Bundle.uiPrevArgs
            get() = getParcelable<UIPrevArgs>(Key)
    }
}

@Parcelize
data class GalleryUiBundle(
        /**
         * 预览toolbar返回是否刷新数据
         */
        val preFinishRefresh: Boolean = true,
        /**
         * 预览back返回是否刷新数据
         */
        val preBackRefresh: Boolean = true,
        /**
         * 文件夹样式
         */
        val finderType: FinderType = FinderType.POPUP,
        /**
         * 裁剪
         */
        val cropType: CropType = CropType.CROPPER,
        /**
         * toolbar返回图标
         */
        @DrawableRes
        val toolbarIcon: Int = R.drawable.ic_gallery_ui_toolbar_back,
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
         * toolbar返回图片颜色
         */
        @ColorInt
        val toolbarIconColor: Int = Color.WHITE,
        /**
         * toolbar title
         */
        val toolbarText: String = "图片选择",
        /**
         * toolbar title颜色
         */
        @ColorInt
        val toolbarTextColor: Int = Color.WHITE,
        /**
         * toolbar elevation
         */
        val toolbarElevation: Float = 4F,
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
        val finderTextCompoundDrawable: Int = R.drawable.ic_gallery_ui_finder,
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
         * 预览文字颜色
         */
        @ColorInt
        val preViewTextColor: Int = Color.WHITE,
        /**
         * 选择文字
         */
        val selectText: String = "确定",
        /**
         * 选择文字大小
         */
        val selectTextSize: Float = 16F,
        /**
         * 选择文字颜色
         */
        @ColorInt
        val selectTextColor: Int = Color.WHITE,
        /**
         * 底部背景色
         */
        @ColorInt
        val bottomViewBackground: Int = Color.parseColor("#FF02A5D2"),
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
         * 预览页title
         */
        val preTitle: String = "选择",
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
         * 预览页底部提示栏数字文字颜色
         */
        @ColorInt
        val preBottomCountTextColor: Int = Color.WHITE,
        /**
         * 预览页底部提示栏确认文字大小
         */
        val preBottomOkTextSize: Float = 16F,
        /**
         * 预览页底部提示栏数字文字大小
         */
        val preBottomCountTextSize: Float = 16F,
        /**
         * 携带的参数
         */
        val args: Bundle = Bundle.EMPTY,
) : Parcelable