@file:Suppress("ArrayInDataClass")

package com.gallery.core.args

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import androidx.core.os.bundleOf
import com.gallery.core.entity.ScanEntity
import com.gallery.core.extensions.parcelableVersion
import com.gallery.core.extensions.square
import com.gallery.scan.Types
import com.gallery.scan.impl.file.FileScanArgs
import kotlinx.parcelize.Parcelize

@Parcelize
data class GalleryConfigs(
    /*** 默认选中的数据*/
    val selects: ArrayList<ScanEntity> = arrayListOf(),
    /*** 扫描类型*/
    val type: Array<String> = arrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString()),
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
    /*** 根目录名称 and 全部图片*/
    val sdNameAndAllName: Pair<String, String> = "根目录" to "全部",
    /*** 拍照裁剪文件配置 [takePictureName]  [takeCropName]*/
    val fileConfig: FileConfig = FileConfig(),
    /*** LayoutManager* 滑动方向* 分割线宽度*/
    val gridConfig: GridConfig = GridConfig(),
    /*** 相机提示文字 相机提示文字大小 相机提示文字颜色 相机图片 相机颜色 背景色 空白占位图 选择box*/
    val cameraConfig: CameraConfig = CameraConfig(),
) : Parcelable {

    /** 返回拍照文件名称 */
    val takePictureName: String get() = "${fileConfig.pictureName}_${System.currentTimeMillis()}.${fileConfig.pictureNameSuffix}"

    /** 返回裁剪文件名称 */
    val takeCropName: String get() = "${fileConfig.cropName}_${System.currentTimeMillis()}.${fileConfig.cropNameSuffix}"

    /** 是否是视频 */
    val isScanVideoMedia: Boolean
        get() = type.size == 1 && type.contains(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString())

    /** 是否是图片 */
    val isScanImageMedia: Boolean
        get() = type.size == 1 && type.contains(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString())

    /** 获取file扫描参数类 */
    val fileScanArgs: FileScanArgs
        get() = FileScanArgs(type, sort.second, sort.first)

    /** item宽高 */
    fun display(context: Context): Int = context.square(gridConfig.spanCount)

    companion object {
        private const val Key = "GalleryConfigs"

        fun GalleryConfigs.toBundle(): Bundle {
            return bundleOf(Key to this)
        }

        val Bundle.configs
            get() = parcelableVersion(Key) ?: GalleryConfigs()
    }

}