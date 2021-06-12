package com.gallery.core

import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.provider.MediaStore
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.Size
import androidx.recyclerview.widget.RecyclerView
import com.gallery.core.entity.ScanEntity
import com.gallery.scan.Types
import kotlinx.parcelize.Parcelize

@Parcelize
data class GalleryBundle(
    /**
     * 默认选中的数据
     */
    val selectEntities: ArrayList<ScanEntity> = arrayListOf(),
    /**
     * 扫描类型
     * 根据[MediaStore.Files.FileColumns.MEDIA_TYPE]搜索
     * 默认为图片扫描
     */
    @Suppress("ArrayInDataClass")
    @Size(min = 1)
    val scanType: IntArray = intArrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE),
    /**
     * 排序方式以及排序所需的数据库字段
     * [Types.Sort.DESC]
     * [Types.Sort.ASC]
     * [MediaStore.Files.FileColumns.DATE_MODIFIED]
     */
    val scanSort: String = Types.Sort.DESC,
    val scanSortField: String = MediaStore.Files.FileColumns.DATE_MODIFIED,
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
     * 这个不区分单选还是多选，拍照裁剪或者直接获取数据或者继续进行选择
     */
    val cameraCrop: Boolean = false,
    /**
     * 多选最多数
     */
    val multipleMaxCount: Int = 9,
    /**
     * 用于配置加载图片的 Width Height
     */
    val spanCount: Int = 4,
    /**
     * 滑动方向
     */
    val orientation: Int = RecyclerView.VERTICAL,
    /**
     * 分割线宽度
     */
    val dividerWidth: Int = 8,
    /**
     * 文件输出路径，仅在Q及以上生效
     * 传入[Environment.DIRECTORY_DCIM]或者[Environment.DIRECTORY_PICTURES]为前缀的字符串
     * 例如: Environment.DIRECTORY_PICTURES + "/your app name"
     */
    val relativePath: String = Environment.DIRECTORY_PICTURES,
    /**
     * 文件输出路径
     * 支持在Q版本以下
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
    val cameraNameSuffix: String = "jpg",
    /**
     * 裁剪路径
     * 只在未拦截自定义裁剪生效,或使用者自定义裁剪使用
     */
    val cropPath: String? = null,
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
     * 相机提示文字颜色
     */
    @ColorInt
    val cameraTextColor: Int = Color.WHITE,
    /**
     * 相机图片
     */
    @DrawableRes
    val cameraDrawable: Int = R.drawable.ic_default_camera_drawable,
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
     * 选择框
     */
    @DrawableRes
    val checkBoxDrawable: Int = R.drawable.selector_default_gallery_item_check,
    /**
     * 空数据占位图片
     */
    @DrawableRes
    val photoEmptyDrawable: Int = 0,
) : Parcelable {
    companion object {
        private const val Key = "galleryBundle"

        fun GalleryBundle.putGalleryArgs(bundle: Bundle = Bundle()): Bundle {
            bundle.putParcelable(Key, this)
            return bundle
        }

        val Bundle.galleryBundle
            get() = getParcelable<GalleryBundle>(Key)

        val Bundle.galleryBundleOrDefault
            get() = galleryBundle ?: GalleryBundle()
    }
}