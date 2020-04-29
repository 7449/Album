package com.gallery.core.callback

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.kotlin.expand.toastExpand
import com.gallery.core.R
import com.gallery.core.ui.fragment.ScanFragment
import com.yalantis.ucrop.UCrop

/**
 * [ScanFragment] 拦截器
 */
interface IGalleryInterceptor {

    /**
     * 是否拦截[ScanFragment.onActivityResult]
     * true 拦截
     * 默认不拦截
     */
    fun onGalleryFragmentResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean = false

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
     * 在[onGalleryFragmentResult]和[onCustomPhotoCrop]为false的情况下会触发
     * 取消裁剪
     */
    fun onUCropCanceled(context: Context) {
        context.getString(R.string.gallery_crop_canceled).toastExpand(context)
    }

    /**
     * 在[onGalleryFragmentResult]和[onCustomPhotoCrop]为false的情况下会触发
     * 裁剪异常
     */
    fun onUCropError(context: Context, throwable: Throwable?) {
        context.getString(R.string.gallery_crop_error).toastExpand(context)
    }

    /**
     * 在[onGalleryFragmentResult]和[onCustomPhotoCrop]为false的情况下会触发
     * 裁剪成功
     */
    fun onUCropResources(uri: Uri) {
        //crop rewrite this method
    }

    /**
     * 无图片或视频时触发,true会自动打开相机
     */
    fun onEmptyPhotoClick(view: View): Boolean = true
}