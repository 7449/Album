package com.gallery.core.ui.base

import androidx.appcompat.app.AppCompatActivity
import androidx.kotlin.expand.os.bundleParcelableOrDefault
import com.gallery.core.GalleryBundle
import com.gallery.core.GalleryConfig
import com.gallery.core.ui.fragment.PrevFragment
import com.gallery.core.ui.fragment.ScanFragment

/**
 * @author y
 * @create 2019/2/27
 */
abstract class GalleryBaseActivity(layoutId: Int) : AppCompatActivity(layoutId) {

    /** 初始配置 */
    protected val galleryBundle by lazy {
        bundleParcelableOrDefault<GalleryBundle>(GalleryConfig.GALLERY_CONFIG, GalleryBundle())
    }

    val galleryFragment: ScanFragment
        get() = supportFragmentManager.findFragmentByTag(ScanFragment::class.java.simpleName) as ScanFragment

    val prevFragment: PrevFragment
        get() = supportFragmentManager.findFragmentByTag(PrevFragment::class.java.simpleName) as PrevFragment
}
