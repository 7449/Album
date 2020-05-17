package com.gallery.core.callback

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.kotlin.expand.text.toastExpand
import com.gallery.core.GalleryBundle
import com.gallery.core.R
import com.gallery.core.ext.cropPathToUri
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
     * 图片裁剪
     */
    fun onCustomPhotoCrop(activity: FragmentActivity, uri: Uri, galleryBundle: GalleryBundle): Intent {
        return UCrop.of(uri, activity.cropPathToUri(galleryBundle.cropPath, galleryBundle.cropName, galleryBundle.cropNameSuffix, galleryBundle.relativePath))
                .withOptions(UCrop.Options().apply {
                    optionBundle.putAll(onUCropOptions())
                })
                .getIntent(activity)
    }

    /**
     * 裁剪成功返回正确的Uri
     */
    fun onCropSuccessUriRule(intent: Intent?): Uri? {
        return if (intent == null) null else UCrop.getOutput(intent)
    }

    /**
     * 裁剪错误的ResultCode
     */
    fun onCropErrorResultCode(): Int = UCrop.RESULT_ERROR

    /**
     * 裁剪错误的Throwable
     */
    fun onCropErrorThrowable(intent: Intent?): Throwable? {
        return if (intent == null) null else UCrop.getError(intent)
    }

    /**
     * [onCustomPhotoCrop]默认uCrop会触发
     */
    fun onUCropOptions() = Bundle()

    /**
     * 在[onCustomPhotoCrop]为false的情况下会触发
     * 取消裁剪
     */
    fun onCropCanceled(context: Context) {
        context.getString(R.string.gallery_crop_canceled).toastExpand(context)
    }

    /**
     * 在[onCustomPhotoCrop]为false的情况下会触发
     * 裁剪异常
     */
    fun onCropError(context: Context, throwable: Throwable?) {
        context.getString(R.string.gallery_crop_error).toastExpand(context)
    }

    /**
     * 裁剪成功
     *
     * 需要注意的是Uri分为两种情况
     * Android 10
     *
     *   content://media/external/images/media/id
     *
     *   如果获取的 content uri 为null,则返回
     *   file:///storage/emulated/0/Android/data/packageName/cache/xxxxx.jpg
     *
     * Android 9 及以下
     *
     *   file:///path/xxxxx.jpg
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
    fun onCropResources(uri: Uri) {
        //crop rewrite this method
    }

    /**
     * 无图片或视频时触发,true会自动打开相机
     */
    fun onEmptyPhotoClick(view: View): Boolean = true
}