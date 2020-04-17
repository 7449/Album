package com.gallery.core

import android.os.Parcelable
import androidx.annotation.ColorRes
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
         */
        val uCropPath: String? = null,
        /**
         * 根目录名称,有的会出现0所以设置下
         */
        val sdName: Int = R.string.gallery_sd_name,
        /**
         * 全部图片
         */
        val allName: Int = R.string.gallery_all_name,
        /**
         * 每行几张图片
         */
        val spanCount: Int = 3,
        /**
         * 分割线宽度
         */
        val dividerWidth: Int = 8,
        /**
         * 相机提示文字
         */
        val cameraText: Int = R.string.gallery_camera_text,
        /**
         * 相机提示文字大小
         */
        val cameraTextSize: Float = 18F,
        /**
         * 相机提示文字颜色
         */
        @ColorRes
        val cameraTextColor: Int = R.color.colorGalleryContentViewTipsColor,
        /**
         * 相机图片
         */
        @DrawableRes
        val cameraDrawable: Int = R.drawable.ic_camera_drawable,
        /**
         * 相机图片背景色
         */
        @ColorRes
        val cameraDrawableColor: Int = R.color.colorGalleryContentViewCameraDrawableColor,
        /**
         * 相机背景色
         */
        @ColorRes
        val cameraBackgroundColor: Int = R.color.colorGalleryCameraBackgroundColor,
        /**
         * 图片背景色
         */
        @ColorRes
        val photoBackgroundColor: Int = R.color.colorGalleryPhotoBackgroundColor,
        /**
         * 预览图片背景色
         */
        @ColorRes
        val prevPhotoBackgroundColor: Int = R.color.colorGalleryPhotoBackgroundColor,
        /**
         * RootView背景色
         */
        @ColorRes
        val rootViewBackground: Int = R.color.colorGalleryContentViewBackground,
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
         * 没有图片显示的占位图片背景色
         */
        @ColorRes
        val photoEmptyDrawableColor: Int = R.color.colorGalleryContentEmptyDrawableColor
) : Parcelable

