package com.album.ui.activity

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.album.Album
import com.album.AlbumConfig
import com.album.util.AlbumTool

/**
 * by y on 14/08/2017.
 */

abstract class AlbumBaseActivity : AppCompatActivity() {

    protected var albumConfig: AlbumConfig = Album.instance.config

    override fun onCreate(savedInstanceState: Bundle?) {
        AlbumTool.setStatusBarColor(ContextCompat.getColor(this, albumConfig.albumStatusBarColor), window)
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initView()
        initTitle()
        initCreate(savedInstanceState)
    }

    protected abstract fun initView()

    protected abstract fun initTitle()

    protected abstract fun initCreate(savedInstanceState: Bundle?)

    protected abstract fun getLayoutId(): Int
}
