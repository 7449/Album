package com.album.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.album.AlbumConstant;
import com.album.R;
import com.album.ui.adapter.PreviewAdapter;
import com.album.util.VersionUtil;

import java.util.ArrayList;

/**
 * by y on 15/08/2017.
 */

public class PreviewActivity extends BaseActivity {

    private Toolbar toolbar;
    private ViewPager viewPager;

    private PreviewAdapter adapter;

    @Override
    protected void initCreate(@Nullable Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        ArrayList<String> stringArrayList = extras.getStringArrayList(AlbumConstant.PREVIEW_KEY);
        adapter = new PreviewAdapter(stringArrayList);
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
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
        return R.layout.activity_preview;
    }
}
