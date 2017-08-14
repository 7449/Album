package com.album.util;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import com.album.AlbumConstant;


/**
 * by y on 14/08/2017.
 */

public class CameraUtil {
    private CameraUtil() {
    }

    public static void openCamera(Fragment activity, Uri cameraUri) {
        if (PermissionUtils.camera(activity.getActivity())) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
            } else {
                ContentValues contentValues = new ContentValues(1);
                contentValues.put(MediaStore.Images.Media.DATA, cameraUri.getPath());
                contentValues.put(MediaStore.Images.Media.MIME_TYPE, AlbumConstant.ITEM_CAMERA);
                Uri uri = activity.getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            }
            activity.startActivityForResult(intent, AlbumConstant.ITEM_CAMERA);
        }
    }
}
