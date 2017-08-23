package com.album;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.album.model.AlbumModel;
import com.album.ui.activity.AlbumActivity;
import com.album.ui.widget.OnEmptyClickListener;
import com.album.ui.widget.SimpleAlbumListener;
import com.album.ui.widget.SimpleGlideAlbumImageLoader;
import com.yalantis.ucrop.UCrop;

import java.util.ArrayList;

/**
 * by y on 14/08/2017.
 */

public class Album {

    private AlbumConfig config = new AlbumConfig();
    private AlbumImageLoader albumImageLoader = new SimpleGlideAlbumImageLoader();
    private UCrop.Options options = new UCrop.Options();
    private AlbumListener albumListener = new SimpleAlbumListener();
    private OnEmptyClickListener emptyClickListener = null;
    private ArrayList<AlbumModel> albumModels = null;
    private Class<?> albumClass = null;

    public static Album getInstance() {
        return AlbumHolder.ALBUM;
    }

    public AlbumImageLoader getAlbumImageLoader() {
        return albumImageLoader;
    }

    public Album setAlbumImageLoader(AlbumImageLoader albumImageLoader) {
        this.albumImageLoader = albumImageLoader;
        return this;
    }

    public UCrop.Options getOptions() {
        return options;
    }

    public Album setOptions(@NonNull UCrop.Options options) {
        this.options = options;
        return this;
    }

    public AlbumConfig getConfig() {
        return config;
    }

    public Album setConfig(@NonNull AlbumConfig config) {
        this.config = config;
        return this;
    }

    public AlbumListener getAlbumListener() {
        return albumListener;
    }

    public Album setAlbumListener(@NonNull AlbumListener albumListener) {
        this.albumListener = albumListener;
        return this;
    }

    public ArrayList<AlbumModel> getAlbumModels() {
        return albumModels;
    }

    public Album setAlbumModels(@Nullable ArrayList<AlbumModel> albumModels) {
        this.albumModels = albumModels;
        return this;
    }

    public Class<?> getAlbumClass() {
        return albumClass;
    }

    public Album setAlbumClass(@Nullable Class<?> albumClass) {
        this.albumClass = albumClass;
        return this;
    }

    public OnEmptyClickListener getEmptyClickListener() {
        return emptyClickListener;
    }

    public Album setEmptyClickListener(OnEmptyClickListener emptyClickListener) {
        this.emptyClickListener = emptyClickListener;
        return this;
    }

    private static final class AlbumHolder {
        private static final Album ALBUM = new Album();
    }

    public void start(@NonNull Context context) {
        if (albumClass == null) {
            albumClass = AlbumActivity.class;
        }
        Intent intent = new Intent(context, albumClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
