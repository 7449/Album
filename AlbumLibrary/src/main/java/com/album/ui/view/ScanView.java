package com.album.ui.view;

import android.content.ContentResolver;
import android.database.Cursor;

import com.album.model.AlbumModel;
import com.album.model.FinderModel;
import com.album.ui.widget.ScanCallBack;

import java.util.ArrayList;

/**
 * by y on 17/08/2017.
 * <p>
 * <p>
 * //        final Activity albumActivity = activity;
 * //
 * //        LoaderManager loaderManager = albumActivity.getLoaderManager();
 * //        final Bundle bundle = new Bundle();
 * //        bundle.putStringArray("albumKey", new String[]{bucketId, String.valueOf(page), String.valueOf(count)});
 * //        loaderManager.initLoader(0, bundle, new LoaderManager.LoaderCallbacks<Cursor>() {
 * //        @Override
 * //        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
 * //            String[] albumArgs = bundle.getStringArray("albumKey");
 * //            return new CursorLoader(albumActivity,
 * //                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
 * //                    null,
 * //                    null,
 * //                    null,
 * //                    null);
 * //        }
 * //
 * //        @Override
 * //        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
 * //            AlbumTool.log(data.getCount());
 * //        }
 * //
 * //        @Override
 * //        public void onLoaderReset(Loader<Cursor> loader) {
 * //
 * //        }
 * //    });
 */


public interface ScanView {
    void start(ContentResolver contentResolver, ScanCallBack scanCallBack, String bucketId, int page, int count);

    void resultScan(ContentResolver contentResolver, ScanCallBack scanCallBack, String path);

    void scanCursor(ArrayList<AlbumModel> albumModels, int dataColumnIndex, int idColumnIndex, int sizeColumnIndex, Cursor cursor);

    void cursorFinder(ArrayList<FinderModel> finderModels);

    int cursorCount(String bucketId);

    Cursor getCursor(String bucketId, int page, int count);

    String[] getSelectionArgs(String bucketId);

}
