package com.album.sample.ui;

import com.album.Album;
import com.album.AlbumConfig;

/**
 * @author y
 */
public class SimpleJava {


    public void start() {
        AlbumConfig albumConfig = new AlbumConfig();
        albumConfig.setFrescoImageLoader(true);
        albumConfig.setRadio(true);
        Album instance = Album.Companion.getInstance();
        instance.setAlbumImageLoader(null);
        instance.setConfig(albumConfig);
        instance.setAlbumListener(null);
        instance.setAlbumClass(null);
    }
}
