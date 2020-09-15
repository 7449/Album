package com.gallery.ui

enum class FinderType {
    POPUP,
    BOTTOM
}

enum class CropType {
    @Deprecated("annoying version support", replaceWith = ReplaceWith("CROPPER"))
    UCROP,
    CROPPER
}