package com.album.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.album.R;
import com.album.model.FinderModel;
import com.album.ui.adapter.ListPopupWindowAdapter;
import com.album.ui.fragment.AlbumFragment;
import com.album.ui.view.AlbumMethodActivityView;
import com.album.util.AlbumLog;
import com.album.util.VersionUtil;

import java.util.List;

/**
 * by y on 14/08/2017.
 */

public class AlbumActivity extends BaseActivity
        implements View.OnClickListener, AlbumMethodActivityView, AdapterView.OnItemClickListener {

    private Toolbar toolbar;
    private AppCompatTextView preview;
    private AppCompatTextView select;
    private AlbumFragment albumFragment;
    private AppCompatTextView finder;
    private ListPopupWindow listPopupWindow;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (albumFragment != null) {
            albumFragment.disconnectMediaScanner();
            albumFragment = null;
            AlbumLog.log("AlbumFragment = null");
        }
        if (listPopupWindow != null) {
            listPopupWindow = null;
        }
    }

    @Override
    protected void initCreate(@Nullable Bundle savedInstanceState) {
        if (albumFragment != null) {
            albumFragment = null;
        }
        initFragment();
        initListPopupWindow();
    }

    @Override
    protected void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        preview = (AppCompatTextView) findViewById(R.id.tv_preview);
        select = (AppCompatTextView) findViewById(R.id.tv_select);
        finder = (AppCompatTextView) findViewById(R.id.tv_finder_all);
        listPopupWindow = new ListPopupWindow(this);
        preview.setOnClickListener(this);
        select.setOnClickListener(this);
        finder.setOnClickListener(this);
    }

    @Override
    public void initFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.album_frame, albumFragment = AlbumFragment.newInstance(), AlbumFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public void initListPopupWindow() {
        listPopupWindow.setAnchorView(finder);
        listPopupWindow.setWidth(600);
        listPopupWindow.setHorizontalOffset(20);
        listPopupWindow.setVerticalOffset(80);
        listPopupWindow.setModal(true);
        listPopupWindow.setOnItemClickListener(this);
    }


    @Override
    protected void initTitle() {
        toolbar.setTitle(R.string.app_name);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setBackgroundResource(R.color.colorAlbumPrimary);
        if (VersionUtil.hasL()) {
            toolbar.setElevation(6f);
            findViewById(R.id.album_bottom_view).setElevation(6f);
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
        } else if (i == R.id.tv_finder_all) {
            List<FinderModel> finderModel = albumFragment.getFinderModel();
            if (finderModel == null || finderModel.isEmpty()) {
                Toast.makeText(this, "...", Toast.LENGTH_SHORT).show();
            } else {
                listPopupWindow.setAdapter(new ListPopupWindowAdapter(finderModel));
                listPopupWindow.show();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
