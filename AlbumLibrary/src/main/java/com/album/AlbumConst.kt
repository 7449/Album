package com.album

object AlbumConst {
    //[AlbumBundle]主要数据的Key
    const val EXTRA_ALBUM_OPTIONS = BuildConfig.APPLICATION_ID + ".Album.Options"
    //UI数据Key
    const val EXTRA_ALBUM_UI_OPTIONS = BuildConfig.APPLICATION_ID + ".Album.Ui.Options"
    //拍照成功之后刷新的Type
    const val TYPE_RESULT_CAMERA = 0
    //裁剪成功之后刷新的Type
    const val TYPE_RESULT_CROP = 1
}

internal object AlbumInternalConst {
    //相机type
    internal const val CAMERA = "Album:Camera"
    //横竖屏切换保存已选择数据 Key
    internal const val TYPE_ALBUM_STATE_SELECT = "album:state_select"
    //横竖屏切换保存当前文件夹的parent Key
    internal const val TYPE_ALBUM_STATE_PARENT = "album:state_parent"
    //横竖屏切换保存当前文件夹名称 Key
    internal const val TYPE_ALBUM_STATE_FINDER_NAME = "album:state_finder_name"
    //横竖屏切换保存拍照图片路径 Key
    internal const val TYPE_ALBUM_STATE_IMAGE_PATH = "album:state_image_path"
}