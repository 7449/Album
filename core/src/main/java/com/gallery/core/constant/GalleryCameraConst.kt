package com.gallery.core.constant

object GalleryCameraConst {
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