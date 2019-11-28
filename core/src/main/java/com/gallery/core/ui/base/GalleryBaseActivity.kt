package com.gallery.core.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * @author y
 * @create 2019/2/27
 */
abstract class GalleryBaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        initView()
        initCreate(savedInstanceState)
    }

    protected abstract fun initView()

    protected abstract fun initCreate(savedInstanceState: Bundle?)

    protected abstract val layoutId: Int
}
