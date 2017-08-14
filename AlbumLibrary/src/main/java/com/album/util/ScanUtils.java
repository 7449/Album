package com.album.util;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v4.util.ArrayMap;

import com.album.AlbumConstant;
import com.album.model.AlbumModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * by y on 11/08/2017.
 */

public class ScanUtils {
    private ScanUtils() {
    }

    public static void start(ContentResolver contentResolver, ScanCallBack scanCallBack) {
        ArrayMap<String, List<AlbumModel>> arrayMap = new ArrayMap<>();
        Cursor cursor = contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null,
                MediaStore.Images.Media.MIME_TYPE + "= ? or " + MediaStore.Images.Media.MIME_TYPE + "= ?",
                new String[]{"image/jpeg", "image/png"},
                MediaStore.Images.Media.DATE_MODIFIED);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                initImage(arrayMap, cursor);
            }
            List<AlbumModel> galleryModels = new ArrayList<>();
            for (Map.Entry<String, List<AlbumModel>> entry : arrayMap.entrySet()) {
                galleryModels.addAll(entry.getValue());
            }
            arrayMap.put(AlbumConstant.ALL_ALBUM, galleryModels);
            scanCallBack.scanSuccess(arrayMap);
            cursor.close();
        }
    }

    private static void initImage(ArrayMap<String, List<AlbumModel>> arrayMap, Cursor cursor) {
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        File pathFile = FileUtils.getPathFile(path);
        if (pathFile != null) {
            String absolutePath = pathFile.getAbsolutePath();
            String finderName = pathFile.getName();
            List<AlbumModel> galleryModels = arrayMap.get(finderName);
            if (galleryModels == null) {
                galleryModels = new ArrayList<>();
                galleryModels.add(new AlbumModel(absolutePath, finderName, path));
                arrayMap.put(finderName, galleryModels);
            } else {
                galleryModels.add(new AlbumModel(absolutePath, finderName, path));
            }
        }
    }

    public interface ScanCallBack {
        void scanSuccess(ArrayMap<String, List<AlbumModel>> galleryModels);
    }
}



