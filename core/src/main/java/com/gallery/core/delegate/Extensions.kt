package com.gallery.core.delegate

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.kotlin.expand.os.bundleOrEmptyExpand
import com.gallery.core.GalleryBundle
import com.gallery.core.GalleryBundle.Companion.galleryBundleOrDefault
import com.gallery.core.PrevArgs.Companion.prevArgsOrDefault
import com.gallery.core.callback.*
import com.gallery.core.crop.ICrop
import com.gallery.core.ui.fragment.PrevFragment
import com.gallery.core.ui.fragment.ScanFragment

/** 图库拦截器 */
val Fragment.galleryInterceptor: IGalleryInterceptor
    get() = when {
        parentFragment is IGalleryInterceptor -> parentFragment as IGalleryInterceptor
        activity is IGalleryInterceptor -> activity as IGalleryInterceptor
        else -> object : IGalleryInterceptor {}
    }

/** 图库回调 */
val Fragment.galleryCallback: IGalleryCallback
    get() = when {
        parentFragment is IGalleryCallback -> parentFragment as IGalleryCallback
        activity is IGalleryCallback -> activity as IGalleryCallback
        else -> throw IllegalArgumentException(context.toString() + " must implement IGalleryCallback")
    }

/** 裁剪回调 */
val Fragment.galleryCrop: ICrop
    get() = when {
        parentFragment is ICrop -> (parentFragment as ICrop).cropImpl ?: throw KotlinNullPointerException("cropImpl == null or crop == null")
        activity is ICrop -> (activity as ICrop).cropImpl ?: throw KotlinNullPointerException("cropImpl == null or crop == null")
        else -> throw KotlinNullPointerException("cropImpl == null or crop == null")
    }

/** 预览页拦截器 */
val Fragment.galleryPrevInterceptor: IGalleryPrevInterceptor
    get() = when {
        parentFragment is IGalleryPrevInterceptor -> parentFragment as IGalleryPrevInterceptor
        activity is IGalleryPrevInterceptor -> activity as IGalleryPrevInterceptor
        else -> object : IGalleryPrevInterceptor {}
    }

/** 预览页回调 */
val Fragment.galleryPrevCallback: IGalleryPrevCallback
    get() = when {
        parentFragment is IGalleryPrevCallback -> parentFragment as IGalleryPrevCallback
        activity is IGalleryPrevCallback -> activity as IGalleryPrevCallback
        else -> throw IllegalArgumentException(context.toString() + " must implement IGalleryPrevCallback")
    }

/** 图片加载器 */
val Fragment.galleryImageLoader: IGalleryImageLoader
    get() = when {
        parentFragment is IGalleryImageLoader -> parentFragment as IGalleryImageLoader
        activity is IGalleryImageLoader -> activity as IGalleryImageLoader
        else -> throw IllegalArgumentException(context.toString() + " must implement IGalleryImageLoader")
    }

/** Fragment获取[GalleryBundle],建议初始化的时候赋值给对应的数据，这个每次调用都会重新获取数据 */
val Fragment.galleryArgs get() = bundleOrEmptyExpand().galleryBundleOrDefault

/** 预览页参数，建议初始化的时候赋值给对应的数据，这个每次调用都会重新获取数据 */
val Fragment.prevArgs get() = bundleOrEmptyExpand().prevArgsOrDefault

/** [ScanFragment] */
val AppCompatActivity.galleryFragment: ScanFragment get() = supportFragmentManager.findFragmentByTag(ScanFragment::class.java.simpleName) as ScanFragment

/** [PrevFragment] */
val AppCompatActivity.prevFragment: PrevFragment get() = supportFragmentManager.findFragmentByTag(PrevFragment::class.java.simpleName) as PrevFragment

/** [IScanDelegate] */
val AppCompatActivity.scanDelegate: IScanDelegate get() = galleryFragment.delegate

/** [IPrevDelegate] */
val AppCompatActivity.prevDelegate: IPrevDelegate get() = prevFragment.delegate