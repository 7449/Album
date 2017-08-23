package com.album;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.album.model.AlbumModel;
import com.album.model.FinderModel;
import com.album.ui.annotation.FrescoType;

/**
 * by y on 15/08/2017.
 */

public interface AlbumImageLoader {

    void displayAlbum(@NonNull ImageView view, int width, int height, @NonNull AlbumModel albumModel);

    void displayAlbumThumbnails(@NonNull ImageView view, @NonNull FinderModel finderModel);

    void displayPreview(@NonNull ImageView view, @NonNull AlbumModel albumModel);

    ImageView frescoView(@NonNull Context context, @FrescoType int type);
}
