package com.gallery.core.extensions

import android.net.Uri

enum class LayoutManager {
    LINEAR,
    GRID
}

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
class CameraUri(val type: IntArray, val uri: Uri)