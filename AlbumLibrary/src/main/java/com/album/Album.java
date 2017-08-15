package com.album;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.album.ui.activity.AlbumActivity;

/**
 * by y on 14/08/2017.
 */

public class Album {

    private AlbumConfig config = new AlbumConfig();

    public static Album getInstance() {
        return AlbumHolder.ALBUM;
    }

    public AlbumConfig getConfig() {
        return config;
    }

    public Album setConfig(@NonNull AlbumConfig config) {
        this.config = config;
        return this;
    }

    public void start(@NonNull Context context) {
        Intent intent = new Intent(context, AlbumActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private static final class AlbumHolder {
        private static final Album ALBUM = new Album();
    }
}
