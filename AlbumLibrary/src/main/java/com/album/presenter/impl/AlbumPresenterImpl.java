package com.album.presenter.impl;

import android.content.ContentResolver;
import android.support.v4.util.ArrayMap;

import com.album.AlbumConstant;
import com.album.model.AlbumModel;
import com.album.model.FinderModel;
import com.album.presenter.AlbumPresenter;
import com.album.ui.view.AlbumView;
import com.album.util.ScanUtils;

import java.util.List;

/**
 * by y on 14/08/2017.
 */

public class AlbumPresenterImpl implements AlbumPresenter, ScanUtils.ScanCallBack {
    private final AlbumView albumView;

    public AlbumPresenterImpl(AlbumView albumView) {
        this.albumView = albumView;
    }

    @Override
    public void scan(ContentResolver contentResolver) {
        albumView.showProgress();
        ScanUtils.start(contentResolver, this);
    }

    @Override
    public void scanSuccess(ArrayMap<String, List<AlbumModel>> galleryModels) {
        albumView.scanSuccess(galleryModels.get(AlbumConstant.ALL_ALBUM));
        albumView.hideProgress();
    }

    @Override
    public void finderModelSuccess(List<FinderModel> list) {
        albumView.finderModel(list);
    }
}
