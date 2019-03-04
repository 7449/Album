package com.album

import android.os.Parcelable
import com.album.core.scan.AlbumScan
import kotlinx.android.parcel.Parcelize

@Parcelize
class AlbumBundle(
        /**
         * 扫描类型
         */
        var scanType: Int = AlbumScan.IMAGE,
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
         * 如果依赖的Dialog默认即可
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
         * 裁剪异常是否退出
         * 如果依赖的Dialog则设置为false
         */
        var cropErrorFinish: Boolean = true,
        /**
         * 选择图片之后是否退出
         * 如果依赖的Dialog则设置为false
         */
        var selectImageFinish: Boolean = true,
        /**
         * 裁剪之后是否退出
         * 如果依赖的Dialog则设置为false
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

