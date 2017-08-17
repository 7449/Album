package com.album.presenter;

import android.content.ContentResolver;

import com.album.model.AlbumModel;

import java.util.ArrayList;
import java.util.List;

/**
 * by y on 17/08/2017.
 */

public interface PreviewPresenter {
    void scan(ContentResolver contentResolver, String bucketId);

    void mergeModel(List<AlbumModel> albumModels, ArrayList<AlbumModel> selectAlbumModels);
}
