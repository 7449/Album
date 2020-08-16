package com.gallery.core.ui.base

import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.kotlin.expand.os.getParcelableOrDefault
import com.gallery.core.GalleryBundle
import com.gallery.core.GalleryConfig
import com.gallery.core.callback.*
import com.gallery.core.crop.ICrop
import com.gallery.core.expand.PermissionCode
import com.gallery.core.expand.requestPermissionResultLauncherExpand

/**
 * @author y
 */
abstract class GalleryBaseFragment(layoutId: Int) : Fragment(layoutId) {

    /** 相机请求权限 */
    protected val cameraPermissionLauncher: ActivityResultLauncher<String> =
            requestPermissionResultLauncherExpand {
                if (it) {
                    permissionsGranted(PermissionCode.READ)
                } else {
                    permissionsDenied(PermissionCode.READ)
                }
            }

    /** 读写请求权限 */
    protected val writePermissionLauncher: ActivityResultLauncher<String> =
            requestPermissionResultLauncherExpand {
                if (it) {
                    permissionsGranted(PermissionCode.WRITE)
                } else {
                    permissionsDenied(PermissionCode.WRITE)
                }
            }

    /** 图库拦截器 */
    protected val galleryInterceptor: IGalleryInterceptor by lazy {
        when {
            parentFragment is IGalleryInterceptor -> parentFragment as IGalleryInterceptor
            activity is IGalleryInterceptor -> activity as IGalleryInterceptor
            else -> object : IGalleryInterceptor {}
        }
    }

    /** 图库回调 */
    protected val galleryCallback: IGalleryCallback by lazy {
        when {
            parentFragment is IGalleryCallback -> parentFragment as IGalleryCallback
            activity is IGalleryCallback -> activity as IGalleryCallback
            else -> throw IllegalArgumentException(context.toString() + " must implement IGalleryCallback")
        }
    }

    /** 裁剪回调 */
    protected val galleryCrop: ICrop by lazy {
        when {
            parentFragment is ICrop -> (parentFragment as ICrop).cropImpl ?: throw KotlinNullPointerException("cropImpl == null or crop == null")
            activity is ICrop -> (activity as ICrop).cropImpl ?: throw KotlinNullPointerException("cropImpl == null or crop == null")
            else -> object : ICrop {
                override val cropImpl: ICrop?
                    get() = null
            }.cropImpl ?: throw KotlinNullPointerException("cropImpl == null or crop == null")
        }
    }

    /** 预览页拦截器 */
    protected val galleryPrevInterceptor: IGalleryPrevInterceptor by lazy {
        when {
            parentFragment is IGalleryPrevInterceptor -> parentFragment as IGalleryPrevInterceptor
            activity is IGalleryPrevInterceptor -> activity as IGalleryPrevInterceptor
            else -> object : IGalleryPrevInterceptor {}
        }
    }

    /** 预览页回调 */
    protected val galleryPrevCallback: IGalleryPrevCallback by lazy {
        when {
            parentFragment is IGalleryPrevCallback -> parentFragment as IGalleryPrevCallback
            activity is IGalleryPrevCallback -> activity as IGalleryPrevCallback
            else -> throw IllegalArgumentException(context.toString() + " must implement IGalleryPrevCallback")
        }
    }

    /** 图片加载器 */
    protected val galleryImageLoader: IGalleryImageLoader by lazy {
        when {
            parentFragment is IGalleryImageLoader -> parentFragment as IGalleryImageLoader
            activity is IGalleryImageLoader -> activity as IGalleryImageLoader
            else -> object : IGalleryImageLoader {}
        }
    }

    /** 参数 */
    protected val galleryBundle by lazy { getParcelableOrDefault<GalleryBundle>(GalleryConfig.GALLERY_CONFIG, GalleryBundle()) }

    protected open fun onCameraResultCanceled() {}

    protected open fun onCameraResultOk() {}

    protected open fun permissionsGranted(type: PermissionCode) {}

    protected open fun permissionsDenied(type: PermissionCode) {}
}

