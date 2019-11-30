package com.gallery.core.constant

internal object GalleryInternalConst {
    //相机type
    internal const val CAMERA_ID = -11111L
    //横竖屏切换保存已选择数据 Key
    internal const val TYPE_STATE_SELECT = "gallery:state_select"
    //横竖屏切换保存当前文件夹的parent Key
    internal const val TYPE_STATE_PARENT = "gallery:state_parent"
    //横竖屏切换保存当前文件夹名称 Key
    internal const val TYPE_STATE_FINDER_NAME = "gallery:state_finder_name"
    //横竖屏切换保存拍照图片Uri Key
    internal const val TYPE_STATE_IMAGE_URI = "gallery:state_image_uri"
    //横竖屏切换保存拍照图片路径 针对FileProvider Key
    internal const val TYPE_STATE_IMAGE_PATH = "gallery:state_image_path"
    //是否刷新前一个页面的数据
    internal const val TYPE_PRE_REFRESH_UI = "pre_refresh_ui"
    //预览页确定数据之后是否销毁前一个页面
    internal const val TYPE_PRE_DONE_FINISH = "pre_select_ok_finish"
}