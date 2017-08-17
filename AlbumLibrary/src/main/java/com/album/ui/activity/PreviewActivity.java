package com.album.ui.activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.album.AlbumConstant;
import com.album.R;
import com.album.model.AlbumModel;
import com.album.presenter.PreviewPresenter;
import com.album.presenter.impl.PreviewPresenterImpl;
import com.album.ui.adapter.PreviewAdapter;
import com.album.ui.view.PreviewMethodActivityView;
import com.album.ui.view.PreviewView;
import com.album.util.FileUtils;
import com.album.util.VersionUtil;

import java.util.ArrayList;

/**
 * by y on 15/08/2017.
 */

public class PreviewActivity extends BaseActivity implements View.OnClickListener, PreviewView, PreviewMethodActivityView {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private PreviewAdapter adapter;
    private AppCompatCheckBox appCompatCheckBox;

    private PreviewPresenter previewPresenter;
    private ArrayList<AlbumModel> albumModels;
    private ArrayList<AlbumModel> selectAlbumModels;
    private int selectPosition;
    private String bucketId = null;

    @Override
    protected void initCreate(@Nullable Bundle savedInstanceState) {
        previewPresenter = new PreviewPresenterImpl(this);
        albumModels = new ArrayList<>();
        initBundle();
        init();
    }

    @Override
    public void initBundle() {
        Bundle extras = getIntent().getExtras();
        selectAlbumModels = extras.getParcelableArrayList(AlbumConstant.PREVIEW_KEY);
        selectPosition = extras.getInt(AlbumConstant.PREVIEW_POSITION_KEY, 0);
        bucketId = extras.getString(AlbumConstant.PREVIEW_BUCKET_ID);
    }

    private void init() {
        boolean isPreview = TextUtils.equals(bucketId, AlbumConstant.PREVIEW_BUTTON_KEY);
        if (isPreview) {
            albumModels.addAll(selectAlbumModels);
            initViewPager(albumModels);
        } else {
            previewPresenter.scan(getContentResolver(), bucketId);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void initViewPager(final ArrayList<AlbumModel> albumModels) {
        adapter = new PreviewAdapter(albumModels);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(TextUtils.isEmpty(bucketId) ? selectPosition - 1 : selectPosition);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (!FileUtils.isFile(adapter.getAlbumPath(position))) {
                    Toast.makeText(viewPager.getContext(), "file null", Toast.LENGTH_SHORT).show();
                }
                appCompatCheckBox.setChecked(albumModels.get(position).isCheck());
                setTitles(position + 1, albumModels.size());
            }
        });
        setTitles(viewPager.getCurrentItem() + 1, albumModels.size());
        appCompatCheckBox.setChecked(albumModels.get(viewPager.getCurrentItem()).isCheck());
    }


    @Override
    protected void initTitle() {
        toolbar.setTitleTextColor(ContextCompat.getColor(this, albumConfig.getAlbumToolbarTextColor()));
        Drawable drawable = ContextCompat.getDrawable(this, albumConfig.getAlbumToolbarIcon());
        drawable.setColorFilter(ContextCompat.getColor(this, albumConfig.getAlbumToolbarIconColor()), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationIcon(drawable);
        toolbar.setBackgroundColor(ContextCompat.getColor(this, albumConfig.getAlbumToolbarBackground()));
        if (VersionUtil.hasL()) {
            toolbar.setElevation(albumConfig.getAlbumToolbarElevation());
        }
    }

    @Override
    protected void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        appCompatCheckBox = (AppCompatCheckBox) findViewById(R.id.preview_check_box);
        LinearLayout rootView = (LinearLayout) findViewById(R.id.preview_root_view);
        RelativeLayout previewBottomView = (RelativeLayout) findViewById(R.id.preview_bottom_view);
        AppCompatTextView previewOk = (AppCompatTextView) findViewById(R.id.preview_bottom_view_tv_select);
        appCompatCheckBox.setBackgroundResource(albumConfig.getAlbumContentItemCheckBoxDrawable());
        previewBottomView.setBackgroundColor(ContextCompat.getColor(this, albumConfig.getAlbumPreviewBottomViewBackground()));
        previewOk.setText(albumConfig.getAlbumPreviewBottomOkText());
        previewOk.setTextSize(albumConfig.getAlbumPreviewBottomOkTextSize());
        previewOk.setTextColor(ContextCompat.getColor(this, albumConfig.getAlbumPreviewBottomOkTextColor()));
        rootView.setBackgroundColor(ContextCompat.getColor(this, albumConfig.getAlbumPreviewBackground()));
        appCompatCheckBox.setOnClickListener(this);
        previewOk.setOnClickListener(this);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_preview;
    }

    @Override
    protected void permissionsDenied(int type) {

    }

    @Override
    protected void permissionsGranted(int type) {
        switch (type) {
            case AlbumConstant.TYPE_ALBUM:
                init();
                break;
        }
    }

    @Override
    public void setTitles(int page, int imageSize) {
        toolbar.setTitle(getString(albumConfig.getAlbumPreviewTitle()) + "(" + page + "/" + imageSize + ")");
    }

    @Override
    public void checkBoxClick() {
        AlbumModel albumModel = adapter.getAlbumModel(viewPager.getCurrentItem());
        if (albumModel == null) {
            Toast.makeText(viewPager.getContext(), "album == null", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!selectAlbumModels.contains(albumModel) && selectAlbumModels.size() >= albumConfig.getMultipleMaxCount()) {
            appCompatCheckBox.setChecked(false);
            Toast.makeText(this, "maxCount", Toast.LENGTH_SHORT).show();
            return;
        }
        if (albumModel.isCheck()) {
            selectAlbumModels.remove(albumModel);
            albumModel.setCheck(false);
        } else {
            albumModel.setCheck(true);
            selectAlbumModels.add(albumModel);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.preview_check_box) {
            checkBoxClick();
        } else if (i == R.id.preview_bottom_view_tv_select) {
            finish();
        }
    }

    @Override
    public void scanSuccess(ArrayList<AlbumModel> albumModels) {
        previewPresenter.mergeModel(albumModels, selectAlbumModels);
        initViewPager(albumModels);
    }

    @Override
    public void finish() {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(AlbumConstant.PREVIEW_KEY, selectAlbumModels);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        super.finish();
    }
}
