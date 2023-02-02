package com.gallery.core.crop

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResult
import com.gallery.core.args.GalleryConfigs
import com.gallery.core.delegate.IScanDelegate
import com.gallery.core.extensions.takeCropUri

interface ICrop {

    val cropImpl: ICrop?
        get() = null

    /*** content://media/external/images/media/id*/
    fun cropOutPutUri(context: Context, configs: GalleryConfigs): Uri? {
        return context.takeCropUri(configs)
    }

    /*** 打开裁剪*/
    fun openCrop(context: Context, configs: GalleryConfigs, inputUri: Uri): Intent {
        TODO("cropping has not been initialized")
    }

    /**
     * 裁剪回调
     * [ActivityResult.mResultCode]
     * 以及
     * [ActivityResult.mData]
     * 裁剪完成之后需要[IScanDelegate.onScanResult]来进行数据刷新
     */
    fun onCropResult(delegate: IScanDelegate, intent: ActivityResult) {
        TODO("need to handle crop callback")
    }

}