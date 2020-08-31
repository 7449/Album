package com.gallery.core.expand

import android.net.Uri

enum class CameraStatus {
    SUCCESS,
    ERROR,
    PERMISSION
}

enum class PermissionCode {
    READ,
    WRITE
}

/** 打开相机的自定义数据携带体 */
class CameraUri(val type: Int, val uri: Uri)