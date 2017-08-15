package com.album.util;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v4.util.ArrayMap;

import com.album.AlbumConstant;
import com.album.model.AlbumModel;
import com.album.model.FinderModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * by y on 11/08/2017.
 */

public class ScanUtils {
    private ScanUtils() {
    }

    public static void start(ContentResolver contentResolver, ScanCallBack scanCallBack, boolean hideCamera) {
        ArrayMap<String, List<AlbumModel>> arrayMap = new ArrayMap<>();
        List<AlbumModel> galleryModels = new ArrayList<>();
        List<FinderModel> finderModels = new ArrayList<>();

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
            for (Map.Entry<String, List<AlbumModel>> entry : arrayMap.entrySet()) {
                String dirName = entry.getKey();
                List<AlbumModel> value = entry.getValue();
                AlbumModel albumModel = value.get(0);
                galleryModels.addAll(value);
                finderModels.add(new FinderModel(dirName, albumModel == null ? "" : albumModel.getPath(), value.size()));
            }
            // finder all album first path
            AlbumModel albumModel = null;
            if (!galleryModels.isEmpty()) {
                albumModel = galleryModels.get(0);
            }

            // reverse
            Collections.reverse(galleryModels);

            // put all album
            arrayMap.put(AlbumConstant.ALL_ALBUM_NAME, galleryModels);

            // add all album camera item
            if (!hideCamera) {
                galleryModels.add(0, new AlbumModel(null, null, AlbumConstant.CAMERA));
            }

            // add all album thumbnailsPath
            if (!finderModels.isEmpty()) {
                finderModels.add(new FinderModel(AlbumConstant.ALL_ALBUM_NAME, albumModel == null ? "" : albumModel.getPath(), hideCamera ? galleryModels.size() : galleryModels.size() - 1));
            }

            scanCallBack.scanSuccess(arrayMap);
            scanCallBack.finderModelSuccess(finderModels);

            //close cursor
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

        void finderModelSuccess(List<FinderModel> list);
    }
}



