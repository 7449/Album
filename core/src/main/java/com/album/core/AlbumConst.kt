package com.album.core

object AlbumPermissionConst {
    //读写code
    const val WRITE_REQUEST_CODE = 360
    //拍照code
    const val CAMERA_REQUEST_CODE = 361
    //图库权限
    const val ALBUM = 362
    //拍照权限
    const val CAMERA = 363
}

object AlbumCameraConst {
    //自定义拍照返回时使用的 REQUEST_CODE
    const val CUSTOM_CAMERA_REQUEST_CODE = 364
    //自定义拍照返回时使用的路径Key,返回时直接调用[finishCamera]即可
    const val RESULT_PATH = "custom_camera_path"
    //拍照返回的 REQUEST_CODE
    const val CAMERA_REQUEST_CODE = 365
    //打开相机没有权限code
    const val CAMERA_PERMISSION_ERROR = 366
    //打开相机成功code
    const val CAMERA_SUCCESS = 367
    //打开相机错误code
    const val CAMERA_ERROR = 368
}

object AlbumConst {
    //[AlbumBundle]主要数据的Key
    const val EXTRA_ALBUM_OPTIONS = BuildConfig.LIBRARY_PACKAGE_NAME + ".Album.Options"
    //UI数据Key
    const val EXTRA_ALBUM_UI_OPTIONS = BuildConfig.LIBRARY_PACKAGE_NAME + ".Album.Ui.Options"
    //预览页扫描图片成功之后需要定位的位置 Key
    const val TYPE_PRE_POSITION = "pre_position"
    //跳转预览页面时携带选中数据 key
    const val TYPE_PRE_SELECT = "pre_multiple_select"
    //预览页横竖屏切换保存已选择数据 / 跳转预览界面携带的所有数据 Key
    const val TYPE_PRE_ALL = "pre_multiple_all"
    //拍照成功之后刷新的Type
    const val TYPE_RESULT_CAMERA = 460
    //裁剪成功之后刷新的Type
    const val TYPE_RESULT_CROP = 461
    //fragment进入到预览页的 request_code
    const val TYPE_PRE_REQUEST_CODE = 462
}

internal object AlbumInternalConst {
    //相机type
    internal const val CAMERA = "Album:Camera"
    //横竖屏切换保存已选择数据 Key
    internal const val TYPE_STATE_SELECT = "album:state_select"
    //横竖屏切换保存当前文件夹的parent Key
    internal const val TYPE_STATE_PARENT = "album:state_parent"
    //横竖屏切换保存当前文件夹名称 Key
    internal const val TYPE_STATE_FINDER_NAME = "album:state_finder_name"
    //横竖屏切换保存拍照图片Uri Key
    internal const val TYPE_STATE_IMAGE_URI = "album:state_image_uri"
    //横竖屏切换保存拍照图片路径 针对FileProvider Key
    internal const val TYPE_STATE_IMAGE_PATH = "album:state_image_path"
    //是否刷新前一个页面的数据
    internal const val TYPE_PRE_REFRESH_UI = "pre_refresh_ui"
    //预览页确定数据之后是否销毁前一个页面
    internal const val TYPE_PRE_DONE_FINISH = "pre_select_ok_finish"
}

