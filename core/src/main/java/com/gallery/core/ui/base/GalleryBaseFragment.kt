package com.gallery.core.ui.base

import androidx.fragment.app.Fragment
import androidx.kotlin.expand.os.bundleExpand
import androidx.kotlin.expand.os.getParcelableOrDefault
import androidx.kotlin.expand.os.orEmptyExpand
import com.gallery.core.GalleryBundle
import com.gallery.core.GalleryConfig
import com.gallery.core.PrevArgs.Companion.prevArgs
import com.gallery.core.callback.*
import com.gallery.core.crop.ICrop

/**
 * @author y
 */
abstract class GalleryBaseFragment(layoutId: Int) : Fragment(layoutId) {

    /** 图库拦截器 */
    val galleryInterceptor: IGalleryInterceptor by lazy {
        when {
            parentFragment is IGalleryInterceptor -> parentFragment as IGalleryInterceptor
            activity is IGalleryInterceptor -> activity as IGalleryInterceptor
            else -> object : IGalleryInterceptor {}
        }
    }

    /** 图库回调 */
    val galleryCallback: IGalleryCallback by lazy {
        when {
            parentFragment is IGalleryCallback -> parentFragment as IGalleryCallback
            activity is IGalleryCallback -> activity as IGalleryCallback
            else -> throw IllegalArgumentException(context.toString() + " must implement IGalleryCallback")
        }
    }

    /** 裁剪回调 */
    val galleryCrop: ICrop by lazy {
        when {
            parentFragment is ICrop -> (parentFragment as ICrop).cropImpl
                    ?: throw KotlinNullPointerException("cropImpl == null or crop == null")
            activity is ICrop -> (activity as ICrop).cropImpl
                    ?: throw KotlinNullPointerException("cropImpl == null or crop == null")
            else -> object : ICrop {
                override val cropImpl: ICrop?
                    get() = null
            }.cropImpl ?: throw KotlinNullPointerException("cropImpl == null or crop == null")
        }
    }

    /** 预览页拦截器 */
    val galleryPrevInterceptor: IGalleryPrevInterceptor by lazy {
        when {
            parentFragment is IGalleryPrevInterceptor -> parentFragment as IGalleryPrevInterceptor
            activity is IGalleryPrevInterceptor -> activity as IGalleryPrevInterceptor
            else -> object : IGalleryPrevInterceptor {}
        }
    }

    /** 预览页回调 */
    val galleryPrevCallback: IGalleryPrevCallback by lazy {
        when {
            parentFragment is IGalleryPrevCallback -> parentFragment as IGalleryPrevCallback
            activity is IGalleryPrevCallback -> activity as IGalleryPrevCallback
            else -> throw IllegalArgumentException(context.toString() + " must implement IGalleryPrevCallback")
        }
    }

    /** 图片加载器 */
    val galleryImageLoader: IGalleryImageLoader by lazy {
        when {
            parentFragment is IGalleryImageLoader -> parentFragment as IGalleryImageLoader
            activity is IGalleryImageLoader -> activity as IGalleryImageLoader
            else -> object : IGalleryImageLoader {}
        }
    }

    /** 参数 */
    val galleryBundle by lazy { getParcelableOrDefault<GalleryBundle>(GalleryConfig.GALLERY_CONFIG, GalleryBundle()) }

    /** 预览页参数 */
    val prevArgs by lazy { bundleExpand.orEmptyExpand().prevArgs ?: throw KotlinNullPointerException("prevArgs == null") }
}

