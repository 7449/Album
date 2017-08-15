package com.album;

import android.widget.ImageView;

/**
 * by y on 15/08/2017.
 */

public interface AlbumImageLoader {

    void displayAlbum(ImageView view, String path);

    void displayAlbumThumbnails(ImageView appCompatImageView, String thumbnailsPath);

    void displayPreview(ImageView img, String path);
}
