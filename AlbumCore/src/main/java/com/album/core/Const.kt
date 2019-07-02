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

object AlbumScanConst {
    //扫描类型：图片
    const val IMAGE = 369
    //扫描类型：视频
    const val VIDEO = 370
    //扫描类型：混合
    const val MIX = 371
    //全部 parent id
    const val ALL = (-111111111).toLong()
}