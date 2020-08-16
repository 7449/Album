package com.gallery.core.crop

import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResult

interface ICrop {

    val cropImpl: ICrop?

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
     */
    fun openCrop(inputUri: Uri, outPutUri: Uri, outPutUri2: Uri): Intent {
        TODO("cropping has not been initialized")
    }

    /**
     * 裁剪回调
     */
    fun cropCallback(intent: ActivityResult) {
        TODO("need to handle crop callback")
    }
}