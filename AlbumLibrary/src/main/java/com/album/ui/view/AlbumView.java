package com.album.ui.view;

import com.album.model.AlbumModel;
import com.album.model.FinderModel;

import java.util.List;

/**
 * by y on 14/08/2017.
 */

public interface AlbumView {

    void showProgress();

    void hideProgress();

    void scanSuccess(List<AlbumModel> list);

    void finderModel(List<FinderModel> list);
}
