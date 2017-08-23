package com.album.util;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;

import com.album.ui.annotation.AlbumResultType;

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
    private int type;

    public SingleMediaScanner(Context context, File file, SingleScannerListener singleScannerListener, @AlbumResultType int type) {
        this.file = file;
        this.type = type;
        this.mediaScannerConnection = new MediaScannerConnection(context.getApplicationContext(), this);
        this.mediaScannerConnection.connect();
        this.singleScannerListener = singleScannerListener;
        this.singleScannerListener.onScanStart();
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
            singleScannerListener.onScanCompleted(type);
        }
    }

    public interface SingleScannerListener {
        void onScanStart();

        void onScanCompleted(@AlbumResultType int type);
    }
}
