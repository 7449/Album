package com.gallery.ui.result

import com.gallery.ui.GalleryUiBundle

object UiConfig {
    /**
     * 裁剪携带的参数
     * 从[GalleryUiBundle.args]里面获取
     */
    const val CROP_ARGS = "cropArgs"

    /**
     * 图库页toolbar返回 result_code
     */
    const val RESULT_CODE_TOOLBAR_BACK = -11

    /**
     * 裁剪返回
     * 与之对应的是[GALLERY_RESULT_CROP]
     */
    const val RESULT_CODE_CROP = -12

    /**
     * 单选下返回
     * 与之对应的是[GALLERY_SINGLE_DATA]
     */
    const val RESULT_CODE_SINGLE_DATA = -13

    /**
     * 多选下返回
     * 与之对应的是[GALLERY_MULTIPLE_DATA]
     */
    const val RESULT_CODE_MULTIPLE_DATA = -14

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