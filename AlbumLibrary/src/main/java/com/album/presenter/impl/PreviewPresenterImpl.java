package com.album.presenter.impl;

import android.content.ContentResolver;
import android.text.TextUtils;

import com.album.model.AlbumModel;
import com.album.model.FinderModel;
import com.album.presenter.PreviewPresenter;
import com.album.ui.view.PreviewView;
import com.album.util.ScanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * by y on 17/08/2017.
 */

public class PreviewPresenterImpl implements PreviewPresenter, ScanUtils.ScanCallBack {
    private final PreviewView previewView;

    public PreviewPresenterImpl(PreviewView previewView) {
        this.previewView = previewView;
    }

    @Override
    public void scan(ContentResolver contentResolver, String bucketId) {
        ScanUtils.get().start(contentResolver, this, bucketId, false, true);
    }

    @Override
    public void mergeModel(List<AlbumModel> albumModels, ArrayList<AlbumModel> selectAlbumModels) {
        if (albumModels == null || selectAlbumModels == null) {
            return;
        }
        for (AlbumModel albumModel : selectAlbumModels) {
            String path = albumModel.getPath();
            for (AlbumModel allAlbumModel : albumModels) {
                String allModelPath = allAlbumModel.getPath();
                if (TextUtils.equals(path, allModelPath)) {
                    allAlbumModel.setCheck(albumModel.isCheck());
                }
            }
        }
    }

    @Override
    public void scanSuccess(ArrayList<AlbumModel> albumModels) {
        previewView.scanSuccess(albumModels);
    }

    @Override
    public void finderModelSuccess(ArrayList<FinderModel> list) {

    }
}
