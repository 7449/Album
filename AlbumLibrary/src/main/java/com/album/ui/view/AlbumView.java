package com.album.ui.view;

import android.support.v4.util.ArrayMap;

import com.album.model.AlbumModel;
import com.album.model.FinderModel;

import java.util.List;

/**
 * by y on 14/08/2017.
 */

public interface AlbumView {

    void showProgress();

    void hideProgress();

    void scanSuccess(ArrayMap<String, List<AlbumModel>> maps);

    void finderModel(List<FinderModel> list);
}
