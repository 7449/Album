package com.album.ui.view;

import android.content.ContentResolver;
import android.database.Cursor;

import com.album.model.AlbumModel;
import com.album.model.FinderModel;
import com.album.ui.widget.ScanCallBack;

import java.util.ArrayList;

/**
 * by y on 17/08/2017.
 */

public interface ScanView {

    void start(ContentResolver contentResolver, ScanCallBack scanCallBack, String bucketId, int page, int count);

    void resultScan(ContentResolver contentResolver, ScanCallBack scanCallBack, String path);

    void scanCursor(ArrayList<AlbumModel> albumModels, int dataColumnIndex, int idColumnIndex, Cursor cursor);

    void cursorFinder(ArrayList<FinderModel> finderModels);

    int cursorCount(String bucketId);

    Cursor getCursor(String bucketId, int page, int count);

    String[] getSelectionArgs(String bucketId);

}
