@file:Suppress("ArrayInDataClass")

package com.gallery.core

import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.provider.MediaStore
import androidx.core.os.bundleOf
import com.gallery.core.entity.ScanEntity
import com.gallery.core.extensions.parcelable
import com.gallery.scan.Types
import com.gallery.scan.impl.file.FileScanArgs
import kotlinx.parcelize.Parcelize

@Parcelize
data class GalleryConfigs(
    /*** 默认选中的数据*/
    val selects: ArrayList<ScanEntity> = arrayListOf(),
    /*** 扫描类型*/
    val type: IntArray = intArrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE),
    /*** 排序方式以及排序所需的数据库字段*/
    val sort: Pair<String, String> = Types.Sort.DESC to MediaStore.Files.FileColumns.DATE_MODIFIED,
    /*** 隐藏相机*/
    val hideCamera: Boolean = false,
    /*** 是否单选*/
    val radio: Boolean = false,
    /*** 是否裁剪 只在[radio]true的情况下生效*/
    val crop: Boolean = true,
    /*** 拍照之后是否立即裁剪*/
    val takePictureCrop: Boolean = false,
    /*** 多选最多数*/
    val maxCount: Int = 9,
    /*** 文件输出路径，仅在Q及以上生效*/
    val relativePath: String = Environment.DIRECTORY_PICTURES,
    /*** 文件输出路径,仅在Q版本以下生效 AND 裁剪路径*/
    val picturePathAndCropPath: Pair<String, String> = "" to "",
    /*** simple: photo.jpg [takePictureName]*/
    val cameraName: Pair<String, String> = System.currentTimeMillis().toString() to "jpg",
    /*** 裁剪文件名称 [takeCropName]*/
    val cropName: Pair<String, String> = System.currentTimeMillis().toString() to "jpg",
    /*** 根目录名称 and 全部图片*/
    val sdNameAndAllName: Pair<String, String> = "根目录" to "全部",
    /*** LayoutManager* 滑动方向* 分割线宽度*/
    val gridConfig: GridConfig = GridConfig(),
    /*** 相机提示文字 相机提示文字大小 相机提示文字颜色 相机图片 相机颜色 背景色 空白占位图 选择box*/
    val cameraConfig: CameraConfig = CameraConfig(),
) : Parcelable {

    /** 返回拍照文件名称 */
    val takePictureName: String get() = "${cameraName.first}_${System.currentTimeMillis()}.${cameraName.second}"

    /** 返回裁剪文件名称 */
    val takeCropName: String get() = "${cropName.first}_${System.currentTimeMillis()}.${cropName.second}"

    /** 是否是视频 */
    val isScanVideoMedia: Boolean
        get() = type.size == 1 && type.contains(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)

    /** 是否是图片 */
    val isScanImageMedia: Boolean
        get() = type.size == 1 && type.contains(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)

    /** 获取file扫描参数类 */
    val fileScanArgs: FileScanArgs
        get() = FileScanArgs(
            type.map { it.toString() }.toTypedArray(),
            sort.second,
            sort.first
        )

    companion object {
        private const val Key = "GalleryConfigs"

        fun GalleryConfigs.toBundle(): Bundle {
            return bundleOf(Key to this)
        }

        val Bundle.configs
            get() = parcelable(Key) ?: GalleryConfigs()
    }

}