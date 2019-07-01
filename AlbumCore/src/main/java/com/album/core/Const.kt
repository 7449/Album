package com.album.core

object AlbumPermissionConst {
    //读写code
    const val WRITE_REQUEST_CODE = 0
    //拍照code
    const val CAMERA_REQUEST_CODE = 1
    //图库权限
    const val TYPE_PERMISSIONS_ALBUM = 0
    //拍照权限
    const val TYPE_PERMISSIONS_CAMERA = 1
}

object AlbumCameraConst {
    //自定义拍照返回时使用的 REQUEST_CODE
    const val CUSTOM_CAMERA_REQUEST_CODE = 113
    //自定义拍照返回时使用的路径Key,返回时直接调用[finishCamera]即可
    const val CUSTOM_CAMERA_RESULT_PATH = "custom_camera_path"
    //拍照返回的 REQUEST_CODE
    const val OPEN_CAMERA_REQUEST_CODE = 110
    //打开相机没有权限code
    const val OPEN_CAMERA_PERMISSION_ERROR = 0
    //打开相机成功code
    const val OPEN_CAMERA_SUCCESS = 1
    //打开相机错误code
    const val OPEN_CAMERA_ERROR = 2
}

object AlbumScanConst {
    //扫描类型：图片
    const val IMAGE = 0
    //扫描类型：视频
    const val VIDEO = 1
    //扫描类型：混合
    const val MIX = 2
    //全部 parent id
    const val ALL_PARENT = (-111111111).toLong()
    //预览 parent id
    const val PREV_PARENT = (-222222222).toLong()
}