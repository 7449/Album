package com.album.presenter;

import android.content.ContentResolver;

import com.album.model.AlbumModel;

import java.util.ArrayList;

/**
 * by y on 14/08/2017.
 */

public interface AlbumPresenter {
    void scan(ContentResolver contentResolver, boolean hideCamera, String bucketId);

    void mergeModel(ArrayList<AlbumModel> albumList, ArrayList<AlbumModel> multiplePreviewList);

    void firstMergeModel(ArrayList<AlbumModel> albumModels, ArrayList<AlbumModel> selectModel);

}
