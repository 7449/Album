package com.gallery.core.constant

import com.gallery.core.BuildConfig

object GalleryConst {
    //[GalleryBundle]数据Key
    const val EXTRA_GALLERY_OPTIONS = BuildConfig.LIBRARY_PACKAGE_NAME + ".Gallery.Options"
    //UI数据Key
    const val EXTRA_GALLERY_UI_OPTIONS = BuildConfig.LIBRARY_PACKAGE_NAME + ".Gallery.Ui.Options"
    //预览页扫描图片成功之后需要定位的位置 Key
    const val TYPE_PRE_POSITION = "pre_position"
    //跳转预览页面时携带选中数据 key
    const val TYPE_PRE_SELECT = "pre_multiple_select"
    //预览页横竖屏切换保存已选择数据 / 跳转预览界面携带的所有数据 Key
    const val TYPE_PRE_ALL = "pre_multiple_all"
    //拍照成功之后刷新的Type
    const val TYPE_RESULT_CAMERA = 460
    //裁剪成功之后刷新的Type
    const val TYPE_RESULT_CROP = 461
    //fragment进入到预览页的 request_code
    const val TYPE_PRE_REQUEST_CODE = 462
}