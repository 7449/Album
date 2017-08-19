package com.album;

import android.widget.ImageView;

import com.album.model.AlbumModel;
import com.album.model.FinderModel;

/**
 * by y on 15/08/2017.
 */

public interface AlbumImageLoader {

    void displayAlbum(ImageView view, AlbumModel albumModel);

    void displayAlbumThumbnails(ImageView view, FinderModel finderModel);

    void displayPreview(ImageView view, AlbumModel albumModel);
}
