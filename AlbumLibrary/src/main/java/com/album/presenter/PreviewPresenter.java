package com.album.presenter;


import com.album.model.AlbumModel;

import java.util.ArrayList;
import java.util.List;

/**
 * by y on 17/08/2017.
 */

public interface PreviewPresenter {
    void scan(String bucketId);

    void mergeModel(List<AlbumModel> albumModels, ArrayList<AlbumModel> selectAlbumModels);
}
