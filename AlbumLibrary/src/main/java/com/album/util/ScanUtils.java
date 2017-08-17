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

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

/**
 * by y on 11/08/2017.
 */
public class ScanUtils implements ScanView {
    private static final String ALL_ALBUM_SELECTION = MediaStore.Images.Media.MIME_TYPE + "= ? or " + MediaStore.Images.Media.MIME_TYPE + "= ? ";
    private static final String FINDER_ALBUM_SELECTION = MediaStore.Images.Media.BUCKET_ID + "= ? and " + ALL_ALBUM_SELECTION;
    private ContentResolver contentResolver = null;

    private ScanUtils() {
    }

    public static ScanUtils get() {
        return new ScanUtils();
    }

    @Override
    public void start(ContentResolver contentResolver, ScanCallBack scanCallBack, String bucketId, boolean finder, boolean hideCamera) {
        this.contentResolver = contentResolver;
        if (contentResolver == null) {
            throw new NullPointerException("ContentResolver == null");
        }
        ArrayMap<String, FinderModel> finderModelMap = new ArrayMap<>();
        ArrayList<AlbumModel> albumModels = new ArrayList<>();
        ArrayList<FinderModel> finderModels = new ArrayList<>();
        Cursor cursor = getAlbumCursor(bucketId);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                cursorAlbum(albumModels, finderModelMap, cursor);
            }
            cursor.close();
            if (finder) {
                cursorFinder(finderModelMap, finderModels);
                scanCallBack.finderModelSuccess(finderModels);
            }
            if (finder && !hideCamera) {
                initCamera(albumModels);
            }
            scanCallBack.scanSuccess(albumModels);
        }
    }

    @Override
    public void cursorAlbum(ArrayList<AlbumModel> albumModels, ArrayMap<String, FinderModel> finderModelMap, Cursor cursor) {
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        File pathFile = FileUtils.getPathFile(path);
        if (pathFile != null) {
            String bucketId = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID));
            String finderName = pathFile.getName();
            FinderModel finderModel = finderModelMap.get(finderName);
            if (finderModel == null) {
                finderModelMap.put(finderName, new FinderModel(finderName, path, bucketId, cursorAlbumCount(bucketId)));
            }
            albumModels.add(new AlbumModel(null, null, path, false));
        }
    }

    @Override
    public void cursorFinder(ArrayMap<String, FinderModel> finderModelMap, ArrayList<FinderModel> finderModels) {
        FinderModel finderModel = new FinderModel(AlbumConstant.ALL_ALBUM_NAME, null, null, 0);
        int count = 0;
        for (Map.Entry<String, FinderModel> entry : finderModelMap.entrySet()) {
            finderModels.add(entry.getValue());
            count += entry.getValue().getCount();
        }
        finderModel.setCount(count);
        if (finderModels.size() > 0 && finderModels.get(0) != null) {
            finderModel.setThumbnailsPath(finderModels.get(0).getThumbnailsPath());
        }
        finderModels.add(0, finderModel);
    }

    @Override
    public int cursorAlbumCount(String bucketId) {
        String[] args = TextUtils.isEmpty(bucketId) ? new String[]{"image/jpeg", "image/png"} : new String[]{bucketId, "image/jpeg", "image/png"};
        Cursor query = contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null,
                FINDER_ALBUM_SELECTION,
                args,
                MediaStore.Images.Media.DATE_MODIFIED + " desc");
        if (query == null) {
            return 0;
        }
        int count = query.getCount();
        query.close();
        return count;
    }


    @Override
    public Cursor getAlbumCursor(String bucketId) {
        String selection = TextUtils.isEmpty(bucketId) ? ALL_ALBUM_SELECTION : FINDER_ALBUM_SELECTION;
        String[] args = TextUtils.isEmpty(bucketId) ? new String[]{"image/jpeg", "image/png"} : new String[]{bucketId, "image/jpeg", "image/png"};
        return contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null,
                selection,
                args,
                MediaStore.Images.Media.DATE_MODIFIED + " desc");
    }

    @Override
    public void initCamera(ArrayList<AlbumModel> albumModelArrayList) {
        albumModelArrayList.add(0, new AlbumModel(null, null, AlbumConstant.CAMERA, false));
    }


    public interface ScanCallBack {
        void scanSuccess(ArrayList<AlbumModel> albumModels);

        void finderModelSuccess(ArrayList<FinderModel> list);
    }
}



