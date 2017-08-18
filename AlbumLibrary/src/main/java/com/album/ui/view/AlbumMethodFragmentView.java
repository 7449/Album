package com.album.ui.view;

import android.net.Uri;
import android.os.Bundle;

import com.album.model.FinderModel;

import java.util.List;

/**
 * by y on 15/08/2017.
 */

public interface AlbumMethodFragmentView {

    void initRecyclerView();

    void disconnectMediaScanner();

    void onScanAlbum(String bucketId);

    void openCamera();

    void openUCrop(String path, Uri uri);

    void refreshMedia();

    List<FinderModel> getFinderModel();

    void multiplePreview();

    void multipleSelect();

    void onResultPreview(Bundle bundle);

}
