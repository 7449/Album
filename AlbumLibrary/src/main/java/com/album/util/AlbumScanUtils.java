package com.album.util;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.album.Album;
import com.album.AlbumConstant;
import com.album.model.AlbumModel;
import com.album.model.FinderModel;
import com.album.ui.view.ScanView;
import com.album.ui.widget.ScanCallBack;

import java.util.ArrayList;
import java.util.Map;

/**
 * by y on 11/08/2017.
 */
public class AlbumScanUtils implements ScanView {
    private static final String ALL_ALBUM_SELECTION = MediaStore.Images.Media.MIME_TYPE + "= ? or " + MediaStore.Images.Media.MIME_TYPE + "= ? or " + MediaStore.Images.Media.MIME_TYPE + "= ? or " + MediaStore.Images.Media.MIME_TYPE + "= ? ";
    private static final String FINDER_ALBUM_SELECTION = MediaStore.Images.Media.BUCKET_ID + "= ? %s and  (" + ALL_ALBUM_SELECTION + " ) ";
    private static final String[] ALBUM_NO_BUCKET_ID_SELECTION_ARGS = new String[]{"image/jpeg", "image/png", "image/jpg", "image/gif"};
    private static final String[] ALBUM_COUNT_PROJECTION = new String[]{
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.SIZE,
    };
    private static final String[] ALBUM_PROJECTION = new String[]{
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.SIZE,
    };
    private static final String[] ALBUM_FINDER_PROJECTION = new String[]{
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.SIZE,
    };

    private ContentResolver contentResolver = null;

    private AlbumScanUtils() {
    }

    public static AlbumScanUtils get() {
        return new AlbumScanUtils();
    }

    @Override
    public void start(ContentResolver contentResolver,
                      ScanCallBack scanCallBack,
                      String bucketId, int page, int count) {
        this.contentResolver = contentResolver;
        if (contentResolver == null) {
            throw new NullPointerException("ContentResolver == null");
        }
        ArrayList<AlbumModel> albumModels = new ArrayList<>();
        ArrayList<FinderModel> finderModels = new ArrayList<>();
        Cursor cursor = getCursor(bucketId, page, count);
        if (cursor != null) {
            int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            int idColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
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
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                ALBUM_PROJECTION,
                MediaStore.Images.Media.DATA + "= ? ",
                new String[]{path},
                MediaStore.Images.Media.DATE_MODIFIED);
        if (query != null) {
            int dataColumnIndex = query.getColumnIndex(MediaStore.Images.Media.DATA);
            int idColumnIndex = query.getColumnIndex(MediaStore.Images.Media._ID);
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
        long size = cursor.getLong(sizeColumnIndex);
        if (FileUtils.getPathFile(path) != null) {
            if (Album.getInstance().getConfig().isFilterImg() && size <= 0) {
                return;
            }
            albumModels.add(new AlbumModel(null, null, path, id, false));
        }
    }

    @Override
    public void cursorFinder(ArrayList<FinderModel> finderModels) {
        ArrayMap<String, FinderModel> finderModelMap = new ArrayMap<>();
        Cursor finderCursor = contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                ALBUM_FINDER_PROJECTION, ALL_ALBUM_SELECTION, ALBUM_NO_BUCKET_ID_SELECTION_ARGS,
                MediaStore.Images.Media.DATE_MODIFIED + " desc");
        if (finderCursor != null) {
            int bucketIdColumnIndex = finderCursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);
            int finderNameColumnIndex = finderCursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            int finderPathColumnIndex = finderCursor.getColumnIndex(MediaStore.Images.Media.DATA);
            int idColumnIndex = finderCursor.getColumnIndex(MediaStore.Images.Media._ID);
            int sizeColumnIndex = finderCursor.getColumnIndex(MediaStore.Images.Media.SIZE);
            while (finderCursor.moveToNext()) {
                String bucketId = finderCursor.getString(bucketIdColumnIndex);
                String finderName = TextUtils.equals(finderCursor.getString(finderNameColumnIndex), "0") ? Album.getInstance().getConfig().getSdName() : finderCursor.getString(finderNameColumnIndex);
                String finderPath = finderCursor.getString(finderPathColumnIndex);
                long size = finderCursor.getLong(sizeColumnIndex);
                long id = finderCursor.getLong(idColumnIndex);
                FinderModel finderModel = finderModelMap.get(finderName);
                if (finderModel == null && FileUtils.getPathFile(finderPath) != null) {
                    if (Album.getInstance().getConfig().isFilterImg() && size <= 0) {
                        continue;
                    }
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
        String selection = Album.getInstance().getConfig().isFilterImg() ? String.format(FINDER_ALBUM_SELECTION, " and _size > 0") : String.format(FINDER_ALBUM_SELECTION, "");
        Cursor query = contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                ALBUM_COUNT_PROJECTION,
                selection,
                getSelectionArgs(bucketId),
                MediaStore.Images.Media.DATE_MODIFIED + " desc");
        if (query == null) {
            return 0;
        }
        int count = query.getCount();
        query.close();
        return count;
    }

    @Override
    public Cursor getCursor(String bucketId, int page, int count) {
        String sortOrder = count == -1 ? MediaStore.Images.Media.DATE_MODIFIED + " desc" : MediaStore.Images.Media.DATE_MODIFIED + " desc limit " + page * count + "," + count;
        String selection = TextUtils.isEmpty(bucketId) ? ALL_ALBUM_SELECTION : Album.getInstance().getConfig().isFilterImg() ? String.format(FINDER_ALBUM_SELECTION, " and _size > 0") : String.format(FINDER_ALBUM_SELECTION, "");
        String[] args = TextUtils.isEmpty(bucketId) ? ALBUM_NO_BUCKET_ID_SELECTION_ARGS : getSelectionArgs(bucketId);
        return contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                ALBUM_PROJECTION,
                selection,
                args,
                sortOrder);
    }

    @Override
    public String[] getSelectionArgs(String bucketId) {
        return new String[]{bucketId, "image/jpeg", "image/png", "image/jpg", "image/gif"};
    }


}



