package com.gallery.compat

import androidx.activity.result.ActivityResultCallback
import com.gallery.compat.internal.call.GalleryListener
import com.gallery.compat.internal.call.GalleryResultCallback

object GalleryConfig {

    /**
     * 图库页toolbar返回 result_code
     */
    const val RESULT_CODE_TOOLBAR_BACK = -11

    /**
     * 单选下返回
     * 与之对应的是[GALLERY_SINGLE_DATA]
     */
    const val RESULT_CODE_SINGLE_DATA = -12

    /**
     * 多选下返回
     * 与之对应的是[GALLERY_MULTIPLE_DATA]
     */
    const val RESULT_CODE_MULTIPLE_DATA = -13

    /**
     * 获取单选数据
     */
    const val GALLERY_SINGLE_DATA = "gallerySingleData"

    /**
     * 获取多选数据
     */
    const val GALLERY_MULTIPLE_DATA = "galleryMultipleData"

    /**
     * 内置的裁剪所需要的 RESULT_CODE 以及参数
     * 可用可不用
     * [GalleryListener]和[GalleryResultCallback]
     */
    object Crop {
        /**
         * 裁剪返回
         * 与之对应的是[GALLERY_RESULT_CROP]
         * 裁剪成功之后的 RESULT_CODE
         * 可在[ActivityResultCallback]中用于判断
         */
        const val RESULT_CODE_CROP = -14

        /**
         * 获取裁剪数据
         * 如果使用这个key
         * 则需要在启动裁剪的时候作为 output uri 的参数传递
         */
        const val GALLERY_RESULT_CROP = "galleryResultCrop"
    }

}