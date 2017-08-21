package com.album.presenter.impl;

import android.text.TextUtils;

import com.album.model.AlbumModel;
import com.album.model.FinderModel;
import com.album.presenter.AlbumPresenter;
import com.album.ui.view.AlbumView;
import com.album.util.ScanUtils;
import com.album.util.task.AlbumTask;
import com.album.util.task.AlbumTaskCallBack;

import java.util.ArrayList;

/**
 * by y on 14/08/2017.
 */

public class AlbumPresenterImpl implements AlbumPresenter, ScanUtils.ScanCallBack {
    private final AlbumView albumView;

    public AlbumPresenterImpl(AlbumView albumView) {
        this.albumView = albumView;
    }

    @Override
    public void scan(final boolean hideCamera, final String bucketId) {
        albumView.getAlbumActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                albumView.showProgress();
            }
        });
        AlbumTask.get().start(new AlbumTaskCallBack.Call() {
            @Override
            public void start() {
                ScanUtils.get().start(albumView.getAlbumActivity().getContentResolver(), AlbumPresenterImpl.this, bucketId, TextUtils.isEmpty(bucketId), hideCamera);
            }
        });
    }

    @Override
    public void mergeModel(ArrayList<AlbumModel> albumList, ArrayList<AlbumModel> multiplePreviewList) {
        if (albumList == null || multiplePreviewList == null) {
            return;
        }
        for (AlbumModel albumModel : albumList) {
            albumModel.setCheck(false);
        }
        for (AlbumModel albumModel : multiplePreviewList) {
            String path = albumModel.getPath();
            for (AlbumModel allAlbumModel : albumList) {
                String allModelPath = allAlbumModel.getPath();
                if (TextUtils.equals(path, allModelPath)) {
                    allAlbumModel.setCheck(albumModel.isCheck());
                }
            }
        }
    }

    @Override
    public void firstMergeModel(ArrayList<AlbumModel> albumModels, ArrayList<AlbumModel> selectModel) {
        for (AlbumModel albumModel : albumModels) {
            albumModel.setCheck(false);
        }
        for (AlbumModel albumModel : selectModel) {
            albumModel.setCheck(true);
        }
        for (AlbumModel albumModel : selectModel) {
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
    public void scanSuccess(final ArrayList<AlbumModel> albumModels, final ArrayList<FinderModel> list) {
        albumView.getAlbumActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mergeModel(albumModels, albumView.getSelectModel());
                albumView.scanSuccess(albumModels);
                if (list != null && !list.isEmpty()) {
                    albumView.finderModel(list);
                }
                albumView.hideProgress();
            }
        });
    }
}
