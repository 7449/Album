package com.album.util;

import android.os.Build;

/**
 * by y on 14/08/2017.
 */

public class VersionUtil {
    private VersionUtil() {
    }


    public static boolean hasL() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
}
