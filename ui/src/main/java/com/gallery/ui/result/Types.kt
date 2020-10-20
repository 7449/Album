package com.gallery.ui.result

enum class FinderType {
    POPUP,
    BOTTOM
}

enum class CropType {
    @Deprecated("annoying version support", replaceWith = ReplaceWith("CROPPER"))
    UCROP,
    CROPPER
}