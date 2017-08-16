package com.album.ui.activity;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.album.AlbumConstant;
import com.album.R;
import com.album.model.FinderModel;
import com.album.ui.adapter.ListPopupWindowAdapter;
import com.album.ui.fragment.AlbumFragment;
import com.album.ui.view.AlbumMethodActivityView;
import com.album.util.DrawableUtil;
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
    private AppCompatTextView finderTv;
    private ListPopupWindow listPopupWindow;
    private RelativeLayout albumBottomView;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (albumFragment != null) {
            albumFragment.disconnectMediaScanner();
            albumFragment = null;
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
        initBottomView();
        initListPopupWindow();
    }

    @Override
    protected void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        preview = (AppCompatTextView) findViewById(R.id.tv_preview);
        select = (AppCompatTextView) findViewById(R.id.tv_select);
        finderTv = (AppCompatTextView) findViewById(R.id.tv_finder_all);
        albumBottomView = (RelativeLayout) findViewById(R.id.album_bottom_view);
        listPopupWindow = new ListPopupWindow(this);
        preview.setOnClickListener(this);
        select.setOnClickListener(this);
        finderTv.setOnClickListener(this);
        preview.setVisibility(albumConfig.isRadio() ? View.GONE : View.VISIBLE);
        select.setVisibility(albumConfig.isRadio() ? View.GONE : View.VISIBLE);
    }

    @Override
    public void initFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.album_frame, albumFragment = AlbumFragment.newInstance(), AlbumFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public void initBottomView() {
        albumBottomView.setBackgroundColor(ContextCompat.getColor(this, albumConfig.getAlbumBottomViewBackground()));
        finderTv.setTextSize(albumConfig.getAlbumBottomFinderTextSize());
        finderTv.setTextColor(ContextCompat.getColor(this, albumConfig.getAlbumBottomFinderTextColor()));
        finderTv.setCompoundDrawables(null, null, DrawableUtil.getDrawable(this, albumConfig.getAlbumBottomFinderTextDrawable(), albumConfig.getAlbumBottomFinderTextDrawableColor()), null);
        preview.setText(albumConfig.getAlbumBottomPreViewText());
        preview.setTextSize(albumConfig.getAlbumBottomPreViewTextSize());
        preview.setTextColor(ContextCompat.getColor(this, albumConfig.getAlbumBottomPreViewTextColor()));
        select.setText(albumConfig.getAlbumBottomSelectText());
        select.setTextSize(albumConfig.getAlbumBottomSelectTextSize());
        select.setTextColor(ContextCompat.getColor(this, albumConfig.getAlbumBottomSelectTextColor()));
    }

    @Override
    public void initListPopupWindow() {
        listPopupWindow.setAnchorView(finderTv);
        listPopupWindow.setWidth(albumConfig.getAlbumListPopupWidth());
        listPopupWindow.setHorizontalOffset(albumConfig.getAlbumListPopupHorizontalOffset());
        listPopupWindow.setVerticalOffset(albumConfig.getAlbumListPopupVerticalOffset());
        listPopupWindow.setModal(true);
        listPopupWindow.setOnItemClickListener(this);
    }


    @Override
    protected void initTitle() {
        toolbar.setTitle(albumConfig.getAlbumToolbarText());
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
    protected int getLayoutId() {
        return R.layout.activity_album;
    }

    @Override
    protected void permissionsDenied(int type) {
        Toast.makeText(this, "permissionsDenied:" + type, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void permissionsGranted(int type) {
        switch (type) {
            case AlbumConstant.TYPE_ALBUM:
                if (albumFragment != null) {
                    albumFragment.onScanAlbum();
                }
                break;
            case AlbumConstant.TYPE_CAMERA:
                if (albumFragment != null) {
                    albumFragment.openCamera();
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_preview) {
        } else if (i == R.id.tv_select) {
        } else if (i == R.id.tv_finder_all) {
            if (albumFragment == null) {
                return;
            }
            List<FinderModel> finderModel = albumFragment.getFinderModel();
            if (finderModel != null && !finderModel.isEmpty()) {
                listPopupWindow.setAdapter(new ListPopupWindowAdapter(finderModel));
                listPopupWindow.show();
                ListView listView = listPopupWindow.getListView();
                if (listView != null) {
                    listView.setBackgroundColor(ContextCompat.getColor(this, albumConfig.getAlbumListPopupItemBackground()));
                }
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ListView listView = listPopupWindow.getListView();
        if (listView == null) {
            return;
        }
        ListPopupWindowAdapter adapter = (ListPopupWindowAdapter) listView.getAdapter();
        FinderModel finder = adapter.getFinder(position);
        if (finder == null) {
            return;
        }
        finderTv.setText(finder.getDirName());
        if (albumFragment != null) {
            albumFragment.updateUI(finder.getDirName());
        }
        listPopupWindow.dismiss();
    }
}
