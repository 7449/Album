package com.album.ui.wechat.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * by y on 14/08/2017.
 */

abstract class AlbumWeChatBaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initView()
        initCreate(savedInstanceState)
    }

    protected abstract fun initView()

    protected abstract fun initCreate(savedInstanceState: Bundle?)

    protected abstract fun getLayoutId(): Int
}
