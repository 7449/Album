package com.album.ui.view;

import android.content.ContentResolver;
import android.database.Cursor;
import android.support.v4.util.ArrayMap;

import com.album.model.AlbumModel;
import com.album.model.FinderModel;
import com.album.util.ScanUtils;

import java.util.ArrayList;

/**
 * by y on 17/08/2017.
 */

public interface ScanView {

    void start(ContentResolver contentResolver, ScanUtils.ScanCallBack scanCallBack, String bucketId, boolean finder, boolean hideCamera);

    void cursorAlbum(ArrayList<AlbumModel> albumModels, ArrayMap<String, FinderModel> finderModelMap, Cursor cursor);

    void cursorFinder(ArrayMap<String, FinderModel> finderModelMap, ArrayList<FinderModel> finderModels);

    int cursorAlbumCount(String bucketId);

    Cursor getAlbumCursor(String bucketId);


    void initCamera(ArrayList<AlbumModel> albumModelArrayList);

}
