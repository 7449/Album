package com.album.ui.view;

import android.content.ContentResolver;
import android.database.Cursor;

import com.album.model.AlbumModel;
import com.album.model.FinderModel;
import com.album.util.AlbumScanUtils;

import java.util.ArrayList;

/**
 * by y on 17/08/2017.
 */

public interface AlbumScanView {

    void start(ContentResolver contentResolver, AlbumScanUtils.ScanCallBack scanCallBack, String bucketId, int page, int count);

    void resultScan(ContentResolver contentResolver, AlbumScanUtils.ScanCallBack scanCallBack, String path);

    void cursorAlbum(ArrayList<AlbumModel> albumModels, int dataColumnIndex, int idColumnIndex, Cursor cursor);

    void cursorFinder(ArrayList<FinderModel> finderModels);

    int cursorAlbumCount(String bucketId);

    Cursor getAlbumCursor(String bucketId, int page, int count);

    String[] getSelectionArgs(String bucketId);

}
