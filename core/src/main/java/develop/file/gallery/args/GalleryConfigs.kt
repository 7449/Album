package develop.file.gallery.args

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import develop.file.gallery.entity.ScanEntity
import develop.file.gallery.extensions.ContextCompat.square
import develop.file.gallery.extensions.ResultCompat.parcelableVersion
import develop.file.media.Types
import develop.file.media.impl.file.FileMediaCursorLoaderArgs
import kotlinx.parcelize.Parcelize

@Parcelize
data class GalleryConfigs(
    /*** 默认选中的数据*/
    val selects: ArrayList<ScanEntity> = arrayListOf(),
    /*** 扫描类型*/
    val type: List<Int> = listOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE),
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

    val takePictureName: String get() = "${fileConfig.pictureName}_${System.currentTimeMillis()}.${fileConfig.pictureNameSuffix}"

    val takeCropName: String get() = "${fileConfig.cropName}_${System.currentTimeMillis()}.${fileConfig.cropNameSuffix}"

    val isScanVideoMedia: Boolean
        get() = type.size == 1 && type.contains(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)

    val fileMediaArgs: FileMediaCursorLoaderArgs
        get() = FileMediaCursorLoaderArgs(type, sort.second, sort.first)

    fun display(context: Context): Int = context.square(gridConfig.spanCount)

    fun layoutManager(context: Context): LayoutManager {
        return GridLayoutManager(
            context,
            if (gridConfig.orientation == RecyclerView.HORIZONTAL) 1 else gridConfig.spanCount,
            gridConfig.orientation,
            false
        )
    }

    companion object {
        private const val Key = "GalleryConfigs"

        fun GalleryConfigs.toBundle(): Bundle {
            return bundleOf(Key to this)
        }

        internal val Bundle.configs
            get() = parcelableVersion(Key) ?: GalleryConfigs()
    }

}