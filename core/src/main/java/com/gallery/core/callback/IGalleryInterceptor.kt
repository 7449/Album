package com.gallery.core.callback

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.kotlin.expand.text.toastExpand
import com.gallery.core.R
import com.gallery.core.ui.fragment.ScanFragment
import com.yalantis.ucrop.UCrop

/**
 * [ScanFragment] 拦截器
 */
interface IGalleryInterceptor {

    /**
     * 自定义相机
     */
    @Deprecated("this feature will be implemented in the next version", ReplaceWith("false"))
    fun onCustomCamera(): Boolean = false

    /**
     * 自定义图片裁剪
     *
     * true 自定义
     */
    fun onCustomPhotoCrop(uri: Uri): Boolean = false

    /**
     * 在[onCustomPhotoCrop]为false的情况下会触发
     * [UCrop]的配置
     */
    fun onUCropOptions() = UCrop.Options()

    /**
     * 在[onCustomPhotoCrop]为false的情况下会触发
     * 取消裁剪
     */
    fun onUCropCanceled(context: Context) {
        context.getString(R.string.gallery_crop_canceled).toastExpand(context)
    }

    /**
     * 在[onCustomPhotoCrop]为false的情况下会触发
     * 裁剪异常
     */
    fun onUCropError(context: Context, throwable: Throwable?) {
        Log.i("onUCropError", throwable?.message.toString())
        context.getString(R.string.gallery_crop_error).toastExpand(context)
    }

    /**
     * 在[onCustomPhotoCrop]为false的情况下会触发
     * 裁剪成功
     *
     * 需要注意的是Uri分为两种情况
     * Android 10
     *
     *   content://media/external/images/media/id
     *
     * Android 9 及以下
     *
     *   file:///storage/emulated/0/Android/data/packageName/cache/xxxxx.jpg
     *
     * 在Android10如果图片转移成功的话,文件名称,路径则依旧和Android10拍照命名规则一样
     *
     * val file: File = when {
     *    hasQExpand() -> File(externalCacheDir, fileName)
     *    uCropPath.isNullOrEmpty() -> lowerVersionFile(fileName, relativePath)
     *    else -> galleryPathFile(uCropPath, fileName)
     * }
     *
     */
    fun onUCropResources(uri: Uri) {
        //crop rewrite this method
    }

    /**
     * 无图片或视频时触发,true会自动打开相机
     */
    fun onEmptyPhotoClick(view: View): Boolean = true
}