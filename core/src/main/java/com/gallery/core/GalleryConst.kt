package com.gallery.core

enum class ResultType {
    CAMERA,
    CROP,
}

enum class CameraStatus {
    PERMISSION,
    SUCCESS,
    ERROR,
}

enum class PermissionCode(val code: Int) {
    WRITE(360),
    READ(361),
}