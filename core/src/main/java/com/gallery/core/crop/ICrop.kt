package com.gallery.core.crop

import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResult

/**
 * core library 剥离出裁剪相关功能，仅提供对外的裁剪接口，具体实现可由
 * ui library 给出
 */
interface ICrop {

    val cropImpl: ICrop?
        get() = null

    /**
     * 打开裁剪
     *
     * [outPutUri2]的格式一直都是
     * file:///path/xxxxx.jpg
     * Android10直接返回 file:///storage/emulated/0/Android/data/packageName/cache/xxxxx.jpg
     *
     * [inputUri]
     * [outPutUri]
     * content://media/external/images/media/id
     *
     * 需要注意的是[outPutUri2]在高版本上返回的是缓存目录下的文件
     * 因为UCrop不支持 content outputUri,所以需要特殊处理下
     *
     * android-image-cropper则支持 content 的 outputUri
     * 这里推荐使用 android-image-cropper 或者其他支持outputUri为content的裁剪库
     * 除非 UCrop什么时候支持 content 的Uri，否则高版本适配比较麻烦
     */
    fun openCrop(inputUri: Uri, outPutUri: Uri, outPutUri2: Uri): Intent {
        TODO("cropping has not been initialized")
    }

    /**
     * 裁剪回调
     * [ActivityResult.mResultCode]
     * [ActivityResult.mData]
     */
    fun onCropResult(intent: ActivityResult) {
        TODO("need to handle crop callback")
    }
}