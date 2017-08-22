package com.album.ui.view;

import android.content.ContentResolver;
import android.database.Cursor;

import com.album.model.AlbumModel;
import com.album.model.FinderModel;
import com.album.util.ScanUtils;

import java.util.ArrayList;

/**
 * by y on 17/08/2017.
 */

public interface ScanView {

    void start(ContentResolver contentResolver, ScanUtils.ScanCallBack scanCallBack, String bucketId, boolean finder, boolean hideCamera, int page, int count);

    void cursorAlbum(ArrayList<AlbumModel> albumModels, int dataColumnIndex, int idColumnIndex, Cursor cursor);

    void cursorFinder(ArrayList<FinderModel> finderModels);

    int cursorAlbumCount(String bucketId);

    Cursor getAlbumCursor(String bucketId, int page, int count);

    String[] getSelectionArgs(String bucketId);

}
