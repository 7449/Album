package com.album.ui.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * by y on 14/08/2017.
 */

public abstract class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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
