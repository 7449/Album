package com.gallery.core.crop

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResult
import com.gallery.core.GalleryBundle
import com.gallery.core.delegate.IScanDelegate
import com.gallery.core.extensions.cropUriExpand
import com.gallery.core.extensions.cropUriExpand2
import com.gallery.core.extensions.orEmptyExpand

/**
 * core library 剥离出裁剪相关功能，仅提供对外的裁剪接口，具体实现可由
 * ui library 给出
 */
interface ICrop {

    val cropImpl: ICrop?
        get() = null

    /**
     * content://media/external/images/media/id
     */
    fun cropOutPutUri(context: Context, bundle: GalleryBundle): Uri {
        return context.cropUriExpand(bundle).orEmptyExpand()
    }

    /**
     * 格式一直都是 file:///path/xxxxx.jpg
     * Android10返回 file:///storage/emulated/0/Android/data/packageName/cache/xxxxx.jpg
     * android-image-cropper支持 content 的 outputUri
     * 这里推荐使用 android-image-cropper 或者其他支持outputUri为content的裁剪库
     * 除非 UCrop什么时候支持 content 的Uri，否则高版本适配比较麻烦
     */
    @Deprecated(
        "annoying version support",
        replaceWith = ReplaceWith("cropOutPutUri(context, galleryBundle)")
    )
    fun cropOutPutUri2(context: Context, bundle: GalleryBundle): Uri {
        return context.cropUriExpand2(bundle).orEmptyExpand()
    }

    /**
     * 打开裁剪
     */
    fun openCrop(context: Context, bundle: GalleryBundle, inputUri: Uri): Intent {
        TODO("cropping has not been initialized")
    }

    /**
     * 裁剪回调
     * [ActivityResult.mResultCode]
     * 以及
     * [ActivityResult.mData]
     * 裁剪完成之后有可能需要[IScanDelegate.onScanResult]来进行数据刷新
     */
    fun onCropResult(
        delegate: IScanDelegate,
        galleryBundle: GalleryBundle,
        intent: ActivityResult
    ) {
        TODO("need to handle crop callback")
    }

}