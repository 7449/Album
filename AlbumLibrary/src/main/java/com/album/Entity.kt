package com.album

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class AlbumBundle(
        /**
         * 扫描类型
         */
        var scanType: Int = IMAGE,
        /**
         * 隐藏相机
         */
        var hideCamera: Boolean = false,
        /**
         * 是否单选
         */
        var radio: Boolean = false,
        /**
         * 是否裁剪
         */
        var crop: Boolean = true,
        /**
         * 拍照之后是否立即裁剪
         */
        var cameraCrop: Boolean = false,
        /**
         * 权限被拒之后是否销毁
         */
        var permissionsDeniedFinish: Boolean = false,
        /**
         * 多选最多数
         */
        var multipleMaxCount: Int = 9,
        /**
         * 拍照路径
         */
        var cameraPath: String? = null,
        /**
         * 裁剪路径
         */
        var uCropPath: String? = null,
        /**
         * 扫描数据库时一次最多扫描多少张
         */
        var scanCount: Int = 500,
        /**
         * 根目录名称,有的会出现0所以设置下
         */
        var sdName: String = "根目录",
        /**
         * 全部图片
         */
        var allName: String = FINDER_ALL_DIR_NAME,
        /**
         * 是否过滤损坏图片
         */
        var filterImg: Boolean = false,
        /**
         * 裁剪异常是否退出
         */
        var cropErrorFinish: Boolean = true,
        /**
         * 选择图片之后是否退出
         */
        var selectImageFinish: Boolean = true,
        /**
         * 裁剪之后是否退出
         */
        var cropFinish: Boolean = true,
        /**
         * 是否预览
         */
        var noPreview: Boolean = false,
        /**
         * 每行几张图片
         */
        var spanCount: Int = 3,
        /**
         * 分割线宽度
         */
        var dividerWidth: Int = 10,
        /**
         * 相机提示文字
         */
        var cameraText: Int = R.string.album_camera_text,
        /**
         * 相机提示文字大小
         */
        var cameraTextSize: Float = 18F,
        /**
         * 相机提示文字颜色
         */
        var cameraTextColor: Int = R.color.colorAlbumContentViewTipsColor,
        /**
         * 相机图片
         */
        var cameraDrawable: Int = R.drawable.ic_camera_drawable,
        /**
         * 相机图片背景色
         */
        var cameraDrawableColor: Int = R.color.colorAlbumContentViewCameraDrawableColor,
        /**
         * 相机背景色
         */
        var cameraBackgroundColor: Int = R.color.colorAlbumCameraBackgroundColor,
        /**
         * RootView背景色
         */
        var rootViewBackground: Int = R.color.colorAlbumContentViewBackground,
        /**
         * 选择框
         */
        var checkBoxDrawable: Int = R.drawable.selector_album_item_check,
        /**
         * 没有图片显示的图片
         */
        var photoEmptyDrawable: Int = R.drawable.ic_camera_drawable,
        /**
         * 没有图片显示的图片背景色
         */
        var photoEmptyDrawableColor: Int = R.color.colorAlbumContentEmptyDrawableColor
) : Parcelable

@Parcelize
class AlbumEntity(private var dirPath: String = "",
                  private var dirName: String = "",
                  var bucketId: String = "",
                  var path: String = "",
                  var id: Long = 0,
                  var isCheck: Boolean = false) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AlbumEntity

        if (dirPath != other.dirPath) return false
        if (dirName != other.dirName) return false
        if (bucketId != other.bucketId) return false
        if (path != other.path) return false
        if (id != other.id) return false
        if (isCheck != other.isCheck) return false

        return true
    }

    override fun hashCode(): Int {
        var result = dirPath.hashCode()
        result = 31 * result + dirName.hashCode()
        result = 31 * result + bucketId.hashCode()
        result = 31 * result + path.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + isCheck.hashCode()
        return result
    }

    override fun toString(): String {
        return "AlbumEntity(dirPath='$dirPath', dirName='$dirName', bucketId='$bucketId', path='$path', id=$id, isCheck=$isCheck)"
    }
}

class FinderEntity(var dirName: String = "", var thumbnailsPath: String = "", var thumbnailsId: Long = 0, var bucketId: String = "", var count: Int = 0)
