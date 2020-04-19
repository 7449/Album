package com.gallery.core.ui.base

import androidx.appcompat.app.AppCompatActivity
import com.gallery.core.ui.fragment.PrevFragment
import com.gallery.core.ui.fragment.ScanFragment

/**
 * @author y
 * @create 2019/2/27
 */
abstract class GalleryBaseActivity(layoutId: Int) : AppCompatActivity(layoutId) {

    val galleryFragment: ScanFragment
        get() = supportFragmentManager.findFragmentByTag(ScanFragment::class.java.simpleName) as ScanFragment

    val prevFragment: PrevFragment
        get() = supportFragmentManager.findFragmentByTag(PrevFragment::class.java.simpleName) as PrevFragment
}
