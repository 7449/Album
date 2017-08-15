package com.album.ui.view;

import com.album.model.FinderModel;

import java.util.List;

/**
 * by y on 15/08/2017.
 */

public interface AlbumMethodFragmentView {

    void initRecyclerView();

    void disconnectMediaScanner();

    void onScanAlbum();

    void openCamera();

    List<FinderModel> getFinderModel();
}
