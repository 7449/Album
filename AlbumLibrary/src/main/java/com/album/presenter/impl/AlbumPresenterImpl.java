package com.album.presenter.impl;

import android.content.ContentResolver;
import android.text.TextUtils;

import com.album.model.AlbumModel;
import com.album.model.FinderModel;
import com.album.presenter.AlbumPresenter;
import com.album.ui.view.AlbumView;
import com.album.ui.view.ScanView;
import com.album.ui.widget.ScanCallBack;
import com.album.util.scan.AlbumScanUtils;
import com.album.util.scan.VideoScanUtils;
import com.album.util.task.AlbumTask;
import com.album.util.task.AlbumTaskCallBack;

import java.util.ArrayList;

/**
 * by y on 14/08/2017.
 */

public class AlbumPresenterImpl implements AlbumPresenter, ScanCallBack {
    private final AlbumView albumView;
    private boolean isScan = false;
    private final ScanView scanView;

    public AlbumPresenterImpl(AlbumView albumView, boolean isVideo) {
        this.albumView = albumView;
        ContentResolver contentResolver = albumView.getAlbumActivity().getContentResolver();
        scanView = isVideo ? VideoScanUtils.get(contentResolver) : AlbumScanUtils.get(contentResolver);
    }

    @Override
    public void scan(final String bucketId, final int page, final int count) {
        albumView.getAlbumActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                isScan = true;
                if (page == 0) {
                    albumView.showProgress();
                }
            }
        });
        AlbumTask.get().start(new AlbumTaskCallBack.Call() {
            @Override
            public void start() {
                scanView.start(AlbumPresenterImpl.this, bucketId, page, count);
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
    public boolean isScan() {
        return isScan;
    }

    @Override
    public void resultScan(final String path) {
        AlbumTask.get().start(new AlbumTaskCallBack.Call() {
            @Override
            public void start() {
                scanView.resultScan(AlbumPresenterImpl.this, path);
            }
        });
    }

    @Override
    public void scanSuccess(final ArrayList<AlbumModel> albumModels, final ArrayList<FinderModel> list) {
        albumView.getAlbumActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                isScan = false;
                albumView.hideProgress();
                if (albumModels != null && albumModels.isEmpty()) {
                    albumView.onAlbumNoMore();
                } else {
                    mergeModel(albumModels, albumView.getSelectModel());
                    albumView.scanSuccess(albumModels);
                    if (list != null && !list.isEmpty()) {
                        albumView.scanFinder(list);
                    }
                }
            }
        });
    }

    @Override
    public void resultSuccess(final AlbumModel albumModel, final ArrayList<FinderModel> finderModels) {
        albumView.getAlbumActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                albumView.resultSuccess(albumModel);
                if (finderModels != null && !finderModels.isEmpty()) {
                    albumView.scanFinder(finderModels);
                }
            }
        });
    }
}
