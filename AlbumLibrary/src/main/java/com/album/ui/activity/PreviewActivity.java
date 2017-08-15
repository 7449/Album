package com.album.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.album.AlbumConstant;
import com.album.R;
import com.album.model.AlbumModel;
import com.album.ui.adapter.PreviewAdapter;
import com.album.util.VersionUtil;

import java.util.List;

/**
 * by y on 15/08/2017.
 */

public class PreviewActivity extends BaseActivity {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private PreviewAdapter adapter;

    @SuppressWarnings("unchecked")
    @Override
    protected void initCreate(@Nullable Bundle savedInstanceState) {
        List<AlbumModel> albumModels = (List<AlbumModel>) getIntent().getSerializableExtra(AlbumConstant.PREVIEW_KEY);
        int position = getIntent().getExtras().getInt(AlbumConstant.PREVIEW_POSITION_KEY, 0);
        adapter = new PreviewAdapter(albumModels);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);
        setSupportActionBar(toolbar);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.preview_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_preview_check) {
            String albumPath = adapter.getAlbumPath(viewPager.getCurrentItem());
            Toast.makeText(this, albumPath, Toast.LENGTH_SHORT).show();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_preview;
    }
}
