package com.album.presenter;

import com.album.model.AlbumModel;
import com.album.ui.widget.ScanCallBack;

import java.util.ArrayList;

/**
 * by y on 14/08/2017.
 */

public interface AlbumPresenter extends ScanCallBack {
    void scan(String bucketId, int page, int count);

    void mergeModel(ArrayList<AlbumModel> albumList, ArrayList<AlbumModel> multiplePreviewList);

    void firstMergeModel(ArrayList<AlbumModel> albumModels, ArrayList<AlbumModel> selectModel);

    boolean isScan();

    void resultScan(String path);
}
