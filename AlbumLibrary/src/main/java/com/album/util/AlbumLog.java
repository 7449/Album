package com.album.util;

import android.util.Log;

import com.album.AlbumConstant;

/**
 * by y on 14/08/2017.
 */

public class AlbumLog {

    public static void log(Object o) {
        if (o != null) {
            Log.d(AlbumConstant.TAG, o.toString());
        }
    }
}
