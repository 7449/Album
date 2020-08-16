package com.gallery.ui

object UIResult {

    /**
     * UI 参数
     * [GalleryUiBundle]
     */
    const val UI_CONFIG = "uiConfig"

    /**
     * UI 参数
     * [Any]
     */
    const val UI_GALLERY_CONFIG = "uiGalleryConfig"

    /**
     * UI 预览 参数
     * [Any]
     */
    const val UI_RESULT_CONFIG = "uiResultConfig"

    /**
     * 横竖屏切换保存文件夹数据
     */
    const val UI_FINDER_LIST = "uiFinderList"

    /**
     * 横竖屏切换保存当前文件夹名称
     */
    const val UI_FINDER_NAME = "uiFinderName"

    /**
     * 图库页toolbar返回 result_code
     */
    const val UI_TOOLBAR_BACK_RESULT_CODE = -11

    /**
     * 裁剪返回
     * 与之对应的是[GALLERY_RESULT_CROP]
     */
    const val GALLERY_CROP_RESULT_CODE = -12

    /**
     * 单选下返回
     * 与之对应的是[GALLERY_SINGLE_DATA]
     */
    const val GALLERY_SINGLE_RESULT_CODE = -13

    /**
     * 多选下返回
     * 与之对应的是[GALLERY_MULTIPLE_DATA]
     */
    const val GALLERY_MULTIPLE_RESULT_CODE = -14

    /**
     * 预览页toolbar返回 result_code
     */
    const val PREV_TOOLBAR_BACK_RESULT_CODE = -15

    /**
     * 预览页back返回 result_code
     */
    const val PREV_BACK_RESULT_CODE = -16

    /**
     * 预览页选中数据返回 result_code
     */
    const val PREV_OK_RESULT_CODE = -17

    /**
     * 获取裁剪数据
     */
    const val GALLERY_RESULT_CROP = "galleryResultCrop"

    /**
     * 获取单选数据
     */
    const val GALLERY_SINGLE_DATA = "gallerySingleData"

    /**
     * 获取多选数据
     */
    const val GALLERY_MULTIPLE_DATA = "galleryMultipleData"
}