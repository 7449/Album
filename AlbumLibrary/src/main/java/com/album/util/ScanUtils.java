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

import java.util.ArrayList;
import java.util.Map;

/**
 * by y on 11/08/2017.
 */
public class ScanUtils implements ScanView {
    private static final String ALL_ALBUM_SELECTION = MediaStore.Images.Media.MIME_TYPE + "= ? or " + MediaStore.Images.Media.MIME_TYPE + "= ? or " + MediaStore.Images.Media.MIME_TYPE + "= ? or " + MediaStore.Images.Media.MIME_TYPE + "= ? ";
    private static final String FINDER_ALBUM_SELECTION = MediaStore.Images.Media.BUCKET_ID + "= ? and  (" + ALL_ALBUM_SELECTION + " )";
    private static final String[] ALBUM_COUNT_PROJECTION = new String[]{MediaStore.Images.Media.BUCKET_ID};
    private static final String[] ALBUM_NO_BUCKET_ID_SELECTION_ARGS = new String[]{"image/jpeg", "image/png", "image/jpg", "image/gif"};
    private static final String[] ALBUM_PROJECTION = new String[]{
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media._ID,
    };
    private static final String[] ALBUM_FINDER_PROJECTION = new String[]{
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATA
    };

    private ContentResolver contentResolver = null;

    private ScanUtils() {
    }

    public static ScanUtils get() {
        return new ScanUtils();
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
        Cursor cursor = getAlbumCursor(bucketId, page, count);
        if (cursor != null) {
            int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            int idColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);

            while (cursor.moveToNext()) {
                cursorAlbum(albumModels, dataColumnIndex, idColumnIndex, cursor);
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
    public void cursorAlbum(ArrayList<AlbumModel> albumModels,
                            int dataColumnIndex,
                            int idColumnIndex,
                            Cursor cursor) {
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
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                ALBUM_FINDER_PROJECTION, ALL_ALBUM_SELECTION, ALBUM_NO_BUCKET_ID_SELECTION_ARGS,
                MediaStore.Images.Media.DATE_MODIFIED + " desc");
        if (finderCursor != null) {
            int bucketIdColumnIndex = finderCursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);
            int finderNameColumnIndex = finderCursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            int finderPathColumnIndex = finderCursor.getColumnIndex(MediaStore.Images.Media.DATA);
            int idColumnIndex = finderCursor.getColumnIndex(MediaStore.Images.Media._ID);
            while (finderCursor.moveToNext()) {
                String bucketId = finderCursor.getString(bucketIdColumnIndex);
                String finderName = finderCursor.getString(finderNameColumnIndex);
                String finderPath = finderCursor.getString(finderPathColumnIndex);
                long id = finderCursor.getLong(idColumnIndex);
                FinderModel finderModel = finderModelMap.get(finderName);
                if (finderModel == null && FileUtils.getPathFile(finderPath) != null) {
                    finderModelMap.put(finderName, new FinderModel(finderName, finderPath, id, bucketId, cursorAlbumCount(bucketId)));
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
    public int cursorAlbumCount(String bucketId) {
        Cursor query = contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                ALBUM_COUNT_PROJECTION,
                FINDER_ALBUM_SELECTION,
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
    public Cursor getAlbumCursor(String bucketId, int page, int count) {
        String sortOrder = count == -1 ? MediaStore.Images.Media.DATE_MODIFIED + " desc" : MediaStore.Images.Media.DATE_MODIFIED + " desc limit " + page * count + "," + count;
        String selection = TextUtils.isEmpty(bucketId) ? ALL_ALBUM_SELECTION : FINDER_ALBUM_SELECTION;
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


    public interface ScanCallBack {
        void scanSuccess(ArrayList<AlbumModel> albumModels, ArrayList<FinderModel> list);

        void resultSuccess(AlbumModel albumModel, ArrayList<FinderModel> finderModels);
    }
}



