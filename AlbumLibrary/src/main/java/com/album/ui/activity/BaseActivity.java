package com.album.ui.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.album.Album;
import com.album.AlbumConfig;

/**
 * by y on 14/08/2017.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected AlbumConfig albumConfig = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        albumConfig = Album.getInstance().getConfig();
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initView();
        initTitle();
        initCreate(savedInstanceState);
    }

    protected abstract void initCreate(@Nullable Bundle savedInstanceState);

    protected abstract void initView();


    protected abstract void initTitle();

    @LayoutRes
    protected abstract int getLayoutId();
}
