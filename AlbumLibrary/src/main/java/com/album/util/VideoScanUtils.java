package com.album.util;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.album.AlbumConstant;
import com.album.model.AlbumModel;
import com.album.model.FinderModel;
import com.album.ui.view.ScanView;
import com.album.ui.widget.ScanCallBack;

import java.util.ArrayList;
import java.util.Map;

/**
 * by y on 01/09/2017.
 */

public class VideoScanUtils implements ScanView {

    private static final String FINDER_VIDEO_SELECTION = MediaStore.Video.Media.BUCKET_ID + "= ? ";
    private static final String[] VIDEO_COUNT_PROJECTION = new String[]{MediaStore.Video.Media.BUCKET_ID};
    private static final String[] VIDEO_PROJECTION = new String[]{
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media._ID,
    };
    private static final String[] VIDEO_FINDER_PROJECTION = new String[]{
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.BUCKET_ID,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Video.Media.DATA
    };

    private ContentResolver contentResolver = null;

    private VideoScanUtils() {
    }

    public static VideoScanUtils get() {
        return new VideoScanUtils();
    }

    @Override
    public void start(ContentResolver contentResolver, ScanCallBack scanCallBack, String bucketId, int page, int count) {
        this.contentResolver = contentResolver;
        if (contentResolver == null) {
            throw new NullPointerException("ContentResolver == null");
        }
        ArrayList<AlbumModel> albumModels = new ArrayList<>();
        ArrayList<FinderModel> finderModels = new ArrayList<>();
        Cursor cursor = getCursor(bucketId, page, count);
        if (cursor != null) {
            int dataColumnIndex = cursor.getColumnIndex(MediaStore.Video.Media.DATA);
            int idColumnIndex = cursor.getColumnIndex(MediaStore.Video.Media._ID);
            int sizeColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.SIZE);
            while (cursor.moveToNext()) {
                scanCursor(albumModels, dataColumnIndex, idColumnIndex, sizeColumnIndex, cursor);
            }
            cursor.close();
            cursorFinder(finderModels);
            scanCallBack.scanSuccess(albumModels, finderModels);
        }
    }

    @Override
    public void resultScan(ContentResolver contentResolver, ScanCallBack scanCallBack, String path) {
        this.contentResolver = contentResolver;
        AlbumModel albumModel = null;
        ArrayList<FinderModel> finderModels = new ArrayList<>();
        Cursor query = contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                VIDEO_PROJECTION,
                MediaStore.Video.Media.DATA + "= ? ",
                new String[]{path},
                MediaStore.Video.Media.DATE_MODIFIED);
        if (query != null) {
            int dataColumnIndex = query.getColumnIndex(MediaStore.Video.Media.DATA);
            int idColumnIndex = query.getColumnIndex(MediaStore.Video.Media._ID);
            while (query.moveToNext()) {
                String resultPath = query.getString(dataColumnIndex);
                long id = query.getLong(idColumnIndex);
                if (FileUtils.getPathFile(resultPath) != null) {
                    albumModel = new AlbumModel(null, null, resultPath, id, false);
                }
            }
            cursorFinder(finderModels);
            query.close();
        }
        scanCallBack.resultSuccess(albumModel, finderModels);
    }

    @Override
    public void scanCursor(ArrayList<AlbumModel> albumModels, int dataColumnIndex, int idColumnIndex, int sizeColumnIndex, Cursor cursor) {
        String path = cursor.getString(dataColumnIndex);
        long id = cursor.getLong(idColumnIndex);
        if (FileUtils.getPathFile(path) != null) {
            albumModels.add(new AlbumModel(null, null, path, id, false));
        }
    }

    @Override
    public void cursorFinder(ArrayList<FinderModel> finderModels) {
        ArrayMap<String, FinderModel> finderModelMap = new ArrayMap<>();
        Cursor finderCursor = contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                VIDEO_FINDER_PROJECTION, null, null,
                MediaStore.Video.Media.DATE_MODIFIED + " desc");
        if (finderCursor != null) {
            int bucketIdColumnIndex = finderCursor.getColumnIndex(MediaStore.Video.Media.BUCKET_ID);
            int finderNameColumnIndex = finderCursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);
            int finderPathColumnIndex = finderCursor.getColumnIndex(MediaStore.Video.Media.DATA);
            int idColumnIndex = finderCursor.getColumnIndex(MediaStore.Video.Media._ID);
            while (finderCursor.moveToNext()) {
                String bucketId = finderCursor.getString(bucketIdColumnIndex);
                String finderName = finderCursor.getString(finderNameColumnIndex);
                String finderPath = finderCursor.getString(finderPathColumnIndex);
                long id = finderCursor.getLong(idColumnIndex);
                FinderModel finderModel = finderModelMap.get(finderName);
                if (finderModel == null && FileUtils.getPathFile(finderPath) != null) {
                    finderModelMap.put(finderName, new FinderModel(finderName, finderPath, id, bucketId, cursorCount(bucketId)));
                }
            }
            finderCursor.close();
        }
        if (finderModelMap.isEmpty()) {
            return;
        }
        FinderModel finderModel = new FinderModel(AlbumConstant.ALL_ALBUM_NAME, null, 0, null, 0);
        int count = 0;
        for (Map.Entry<String, FinderModel> entry : finderModelMap.entrySet()) {
            finderModels.add(entry.getValue());
            count += entry.getValue().getCount();
        }
        finderModel.setCount(count);
        if (finderModels.size() > 0 && finderModels.get(0) != null) {
            finderModel.setThumbnailsPath(finderModels.get(0).getThumbnailsPath());
            finderModel.setThumbnailsId(finderModels.get(0).getThumbnailsId());
        }
        finderModels.add(0, finderModel);
        finderModelMap.clear();
    }

    @Override
    public int cursorCount(String bucketId) {
        Cursor query = contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                VIDEO_COUNT_PROJECTION,
                FINDER_VIDEO_SELECTION,
                new String[]{bucketId},
                MediaStore.Video.Media.DATE_MODIFIED + " desc");
        if (query == null) {
            return 0;
        }
        int count = query.getCount();
        query.close();
        return count;
    }

    @Override
    public Cursor getCursor(String bucketId, int page, int count) {
        String sortOrder = count == -1 ? MediaStore.Video.Media.DATE_MODIFIED + " desc" : MediaStore.Video.Media.DATE_MODIFIED + " desc limit " + page * count + "," + count;
        String selection = TextUtils.isEmpty(bucketId) ? null : FINDER_VIDEO_SELECTION;
        String[] args = TextUtils.isEmpty(bucketId) ? null : new String[]{bucketId};
        return contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                VIDEO_PROJECTION,
                selection,
                args,
                sortOrder);
    }

    @Override
    public String[] getSelectionArgs(String bucketId) {
        return null;
    }
}
