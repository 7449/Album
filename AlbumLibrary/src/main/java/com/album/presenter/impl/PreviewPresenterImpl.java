package com.album.presenter.impl;

import android.content.ContentResolver;
import android.text.TextUtils;

import com.album.model.AlbumModel;
import com.album.model.FinderModel;
import com.album.presenter.PreviewPresenter;
import com.album.ui.view.PreviewView;
import com.album.ui.view.ScanView;
import com.album.ui.widget.ScanCallBack;
import com.album.util.scan.AlbumScanUtils;
import com.album.util.scan.VideoScanUtils;
import com.album.util.task.AlbumTask;
import com.album.util.task.AlbumTaskCallBack;

import java.util.ArrayList;
import java.util.List;

/**
 * by y on 17/08/2017.
 */

public class PreviewPresenterImpl implements PreviewPresenter, ScanCallBack {
    private final PreviewView previewView;
    private final ScanView scanView;

    public PreviewPresenterImpl(PreviewView previewView, boolean isVideo) {
        this.previewView = previewView;
        ContentResolver contentResolver = previewView.getPreviewActivity().getContentResolver();
        scanView = isVideo ? VideoScanUtils.get(contentResolver) : AlbumScanUtils.get(contentResolver);
    }

    @Override
    public void scan(final String bucketId, final int page, final int count) {
        previewView.getPreviewActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                previewView.showProgress();
            }
        });
        AlbumTask.get().start(new AlbumTaskCallBack.Call() {
            @Override
            public void start() {
                scanView.start(PreviewPresenterImpl.this, bucketId, page, count);
            }
        });
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
    public void scanSuccess(final ArrayList<AlbumModel> albumModels, ArrayList<FinderModel> list) {
        previewView.getPreviewActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                previewView.hideProgress();
                previewView.scanSuccess(albumModels);
            }
        });
    }

    @Override
    public void resultSuccess(AlbumModel albumModel, ArrayList<FinderModel> finderModels) {

    }

}
