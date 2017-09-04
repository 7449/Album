package com.album.ui.widget;

import com.album.model.AlbumModel;
import com.album.model.FinderModel;

import java.util.ArrayList;

/**
 * by y on 04/09/2017.
 */

public interface ScanCallBack {
    void scanSuccess(ArrayList<AlbumModel> albumModels, ArrayList<FinderModel> list);

    void resultSuccess(AlbumModel albumModel, ArrayList<FinderModel> finderModels);
}
