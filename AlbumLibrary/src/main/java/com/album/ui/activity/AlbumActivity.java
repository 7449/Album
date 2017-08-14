package com.album.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.album.R;
import com.album.ui.fragment.AlbumFragment;
import com.album.util.VersionUtil;

/**
 * by y on 14/08/2017.
 */

public class AlbumActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private AppCompatTextView preview;
    private AppCompatTextView select;
    private AlbumFragment albumFragment;

    @Override
    protected void initCreate(@Nullable Bundle savedInstanceState) {
        if (albumFragment != null) {
            albumFragment = null;
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.album_frame, albumFragment = AlbumFragment.newInstance(), AlbumFragment.class.getSimpleName())
                .commit();
    }

    @Override
    protected void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        preview = (AppCompatTextView) findViewById(R.id.tv_preview);
        select = (AppCompatTextView) findViewById(R.id.tv_select);
        preview.setOnClickListener(this);
        select.setOnClickListener(this);
    }

    @Override
    protected void initTitle() {
        toolbar.setTitle(R.string.app_name);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setBackgroundResource(R.color.colorAlbumPrimary);
        if (VersionUtil.hasL()) {
            toolbar.setElevation(6f);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_album;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_preview) {
        } else if (i == R.id.tv_select) {

        }
    }
}
