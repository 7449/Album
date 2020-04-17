package com.gallery.core.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gallery.core.ui.fragment.PrevFragment
import com.gallery.core.ui.fragment.ScanFragment

/**
 * @author y
 * @create 2019/2/27
 */
abstract class GalleryBaseActivity(layoutId: Int) : AppCompatActivity(layoutId) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initCreate(savedInstanceState)
    }

    protected abstract fun initView()

    protected abstract fun initCreate(savedInstanceState: Bundle?)

    fun galleryFragment(): ScanFragment {
        return supportFragmentManager.findFragmentByTag(ScanFragment::class.java.simpleName) as ScanFragment
    }

    fun prevFragment(): PrevFragment {
        return supportFragmentManager.findFragmentByTag(PrevFragment::class.java.simpleName) as PrevFragment
    }
}
