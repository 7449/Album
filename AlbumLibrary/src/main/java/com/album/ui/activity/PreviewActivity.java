package com.album.ui.activity;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
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
        viewPager.setBackgroundColor(ContextCompat.getColor(this, albumConfig.getAlbumPreviewBackground()));
    }

    @Override
    protected void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
    }

    @Override
    protected void initTitle() {
        toolbar.setTitle(albumConfig.getAlbumPreviewTitle());
        toolbar.setTitleTextColor(ContextCompat.getColor(this, albumConfig.getAlbumToolbarTextColor()));
        Drawable drawable = ContextCompat.getDrawable(this, albumConfig.getAlbumToolbarIcon());
        drawable.setColorFilter(ContextCompat.getColor(this, albumConfig.getAlbumToolbarIconColor()), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationIcon(drawable);
        toolbar.setBackgroundColor(ContextCompat.getColor(this, albumConfig.getAlbumToolbarBackground()));
        if (VersionUtil.hasL()) {
            toolbar.setElevation(albumConfig.getAlbumToolbarElevation());
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
        MenuItem item = menu.findItem(R.id.action_preview_check);
        Drawable drawable = ContextCompat.getDrawable(this, albumConfig.getAlbumPreviewTitleCheckDrawable());
        drawable.setColorFilter(ContextCompat.getColor(this, albumConfig.getAlbumPreviewTitleCheckDrawableColor()), PorterDuff.Mode.SRC_ATOP);
        item.setIcon(drawable);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_preview_check) {
            String albumPath = adapter.getAlbumPath(viewPager.getCurrentItem());
            Toast.makeText(this, albumPath, Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_preview;
    }
}
