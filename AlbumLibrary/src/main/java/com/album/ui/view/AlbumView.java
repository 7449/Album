package com.album.ui.view;

import com.album.model.AlbumModel;
import com.album.model.FinderModel;

import java.util.ArrayList;

/**
 * by y on 14/08/2017.
 */

public interface AlbumView {

    void showProgress();

    void hideProgress();

    void scanSuccess(ArrayList<AlbumModel> albumModels);

    void finderModel(ArrayList<FinderModel> list);

    ArrayList<AlbumModel> getSelectModel();
}
