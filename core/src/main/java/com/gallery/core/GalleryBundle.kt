package com.gallery.core

import android.graphics.Color
import android.os.Parcelable
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
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
         */
        val scanType: ScanType = ScanType.IMAGE,
        /**
         * 摄像或拍照文件名称
         */
        val cameraName: String = System.currentTimeMillis().toString(),
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
         * 拍照路径
         * 支持在Android10版本以下
         */
        val cameraPath: String? = null,
        /**
         * 裁剪路径
         * 只在未拦截自定义裁剪生效,或使用者自定义裁剪使用
         */
        val uCropPath: String? = null,
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
         * 每行几张图片
         */
        val spanCount: Int = 3,
        /**
         * 分割线宽度
         */
        val dividerWidth: Int = 8,
        /**
         * 相机提示文字大小
         */
        val cameraTextSize: Float = 16F,
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
        val cameraTextColor: Int = Color.parseColor("#FFFFFFFF"),
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
        val photoBackgroundColor: Int = Color.parseColor("#FFFFFFFF"),
        /**
         * 预览图片背景色
         */
        @ColorInt
        val prevPhotoBackgroundColor: Int = Color.parseColor("#FFFFFFFF"),
        /**
         * RootView背景色
         */
        @ColorInt
        val rootViewBackground: Int = Color.parseColor("#FFFFFFFF")
) : Parcelable

