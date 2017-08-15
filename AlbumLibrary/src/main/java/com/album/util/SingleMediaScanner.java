package com.album.util;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;

import java.io.File;

/**
 * by y on 14/08/2017.
 * <p>
 * <p>
 * https://issuetracker.google.com/issues/37046656
 * <p>
 * https://github.com/square/leakcanary/issues/26
 */

public class SingleMediaScanner implements MediaScannerConnection.MediaScannerConnectionClient {

    private MediaScannerConnection mediaScannerConnection;
    private File file;
    private SingleScannerListener singleScannerListener = null;

    public SingleMediaScanner(Context context, File file, SingleScannerListener singleScannerListener) {
        this.file = file;
        this.mediaScannerConnection = new MediaScannerConnection(context.getApplicationContext(), this);
        this.mediaScannerConnection.connect();
        this.singleScannerListener = singleScannerListener;
    }

    @Override
    public void onMediaScannerConnected() {
        mediaScannerConnection.scanFile(file.getAbsolutePath(), null);
    }

    public void disconnect() {
        if (mediaScannerConnection != null) {
            mediaScannerConnection.disconnect();
            mediaScannerConnection = null;
        }
    }

    @Override
    public void onScanCompleted(String path, Uri uri) {
        disconnect();
        if (singleScannerListener != null) {
            singleScannerListener.onScanCompleted();
        }
    }

    public interface SingleScannerListener {
        void onScanCompleted();
    }
}
