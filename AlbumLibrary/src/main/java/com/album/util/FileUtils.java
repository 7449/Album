package com.album.util;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;

/**
 * by y on 11/08/2017.
 */

public class FileUtils {
    private FileUtils() {
    }

    public static boolean isFile(String path) {
        return getPathFile(path) != null;
    }

    static File getPathFile(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        File parentFile = file.getParentFile();
        if (parentFile == null) {
            return null;
        }
        return parentFile;
    }

    public static File getScannerFile(String path) {
        return new File(path);
    }


    public static File getCameraFile(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            cachePath = Environment.getExternalStorageDirectory().getPath() + "/" + "DCIM";
        } else {
            File externalCacheDir = context.getExternalCacheDir();
            if (externalCacheDir != null) {
                cachePath = externalCacheDir.getPath();
            }
        }
        return new File(cachePath, System.currentTimeMillis() + ".jpg");
    }
}
