package com.album;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.album.ui.activity.AlbumActivity;
import com.album.ui.widget.SimpleAlbumImageLoader;
import com.yalantis.ucrop.UCrop;

/**
 * by y on 14/08/2017.
 */

public class Album {

    private AlbumConfig config = new AlbumConfig();
    private AlbumImageLoader albumImageLoader = new SimpleAlbumImageLoader();
    private UCrop.Options options = new UCrop.Options();

    public static Album getInstance() {
        return AlbumHolder.ALBUM;
    }

    public AlbumImageLoader getAlbumImageLoader() {
        return albumImageLoader;
    }

    public UCrop.Options getOptions() {
        return options;
    }

    public Album setOptions(UCrop.Options options) {
        this.options = options;
        return this;
    }

    public Album setAlbumImageLoader(AlbumImageLoader albumImageLoader) {
        this.albumImageLoader = albumImageLoader;
        return this;
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
