package com.gallery.core

import android.graphics.Color
import android.os.Environment
import android.os.Parcelable
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.gallery.core.ui.fragment.ScanFragment
import com.gallery.scan.ScanEntity
import com.gallery.scan.ScanType
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GalleryBundle(
        /**
         * 默认选中的数据
         */
        val selectEntities: ArrayList<ScanEntity> = ArrayList(),
        /**
         * 扫描类型
         * [ScanType.IMAGE]
         * [ScanType.VIDEO]
         * [ScanType.MIX]
         */
        val scanType: ScanType = ScanType.IMAGE,
        /**
         * 隐藏相机
         */
        val hideCamera: Boolean = false,
        /**
         * 是否单选
         */
        val radio: Boolean = false,
        /**
         * 是否裁剪
         * 只在[radio]true的情况下生效
         */
        val crop: Boolean = true,
        /**
         * 拍照之后是否立即裁剪
         */
        val cameraCrop: Boolean = false,
        /**
         * 多选最多数
         */
        val multipleMaxCount: Int = 9,
        /**
         * 文件输出路径
         * [Environment.DIRECTORY_DCIM]
         * [Environment.DIRECTORY_PICTURES]
         */
        val relativePath: String = Environment.DIRECTORY_DCIM,
        /**
         * 拍照路径
         * 支持在Android10版本以下
         */
        val cameraPath: String? = null,
        /**
         * 摄像或拍照文件名称
         */
        val cameraName: String = System.currentTimeMillis().toString(),
        /**
         * 摄像或拍照文件名称后缀
         * cameraName.cameraNameSuffix
         * simple: photo.jpg
         */
        val cameraNameSuffix: String = if (scanType == ScanType.VIDEO) "mp4" else "jpg",
        /**
         * 裁剪路径
         * 只在未拦截自定义裁剪生效,或使用者自定义裁剪使用
         * 支持在Android10版本以下
         */
        val cropPath: String? = null,
        /**
         * 最低支持Android10
         * Android10及以上Crop输出路径默认为
         * externalCacheDir
         * 如果需要保存可设置
         * relativePath
         */
        val cropSuccessSave: Boolean = true,
        /**
         * 裁剪文件名称
         */
        val cropName: String = System.currentTimeMillis().toString(),
        /**
         * 裁剪文件后缀
         */
        val cropNameSuffix: String = "jpg",
        /**
         * 根目录名称,有的会出现0所以设置下
         */
        val sdName: String = "根目录",
        /**
         * 全部图片
         */
        val allName: String = "全部",
        /**
         * 相机提示文字
         */
        val cameraText: String = "",
        /**
         * 相机提示文字大小
         */
        val cameraTextSize: Float = 16F,
        /**
         * 每行几张图片
         */
        val spanCount: Int = 4,
        /**
         * 分割线宽度
         */
        val dividerWidth: Int = 8,
        /**
         * 相机图片
         */
        @DrawableRes
        val cameraDrawable: Int = R.drawable.ic_camera_drawable,
        /**
         * 选择框
         */
        @DrawableRes
        val checkBoxDrawable: Int = R.drawable.selector_gallery_item_check,
        /**
         * 没有图片显示的占位图片
         */
        @DrawableRes
        val photoEmptyDrawable: Int = R.drawable.ic_camera_drawable,
        /**
         * 相机提示文字颜色
         */
        @ColorInt
        val cameraTextColor: Int = Color.WHITE,
        /**
         * 相机图片背景色
         */
        @ColorInt
        val cameraDrawableColor: Int = Color.parseColor("#FF02A5D2"),
        /**
         * 相机背景色
         */
        @ColorInt
        val cameraBackgroundColor: Int = Color.parseColor("#FFB0C9C9"),
        /**
         * 图片背景色
         */
        @ColorInt
        val photoBackgroundColor: Int = Color.WHITE,
        /**
         * 预览图片背景色
         */
        @ColorInt
        val prevPhotoBackgroundColor: Int = Color.WHITE,
        /**
         * [ScanFragment]RootView背景色
         */
        @ColorInt
        val galleryRootBackground: Int = Color.WHITE
) : Parcelable

