package com.album.ui.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.album.AlbumImageLoader;
import com.album.R;
import com.album.model.AlbumModel;
import com.album.model.FinderModel;
import com.album.ui.annotation.FrescoType;
import com.bumptech.glide.Glide;

/**
 * by y on 15/08/2017.
 */

public class SimpleGlideAlbumImageLoader implements AlbumImageLoader {

    @Override
    public void displayAlbum(@NonNull ImageView view, int width, int height, @NonNull AlbumModel albumModel) {
        Glide
                .with(view.getContext())
                .load(albumModel.getPath())
                .placeholder(R.drawable.ic_launcher)
                .error(R.drawable.ic_launcher)
                .override(width, height)
                .centerCrop()
                .into(view);
    }

    @Override
    public void displayAlbumThumbnails(@NonNull ImageView view, @NonNull FinderModel finderModel) {
        Glide
                .with(view.getContext())
                .load(finderModel.getThumbnailsPath())
                .placeholder(R.drawable.ic_launcher)
                .error(R.drawable.ic_launcher)
                .centerCrop()
                .into(view);
    }

    @Override
    public void displayPreview(@NonNull ImageView view, @NonNull AlbumModel albumModel) {
        Glide
                .with(view.getContext())
                .load(albumModel.getPath())
                .placeholder(R.drawable.ic_launcher)
                .error(R.drawable.ic_launcher)
                .centerCrop()
                .into(view);
    }

    @Override
    public ImageView frescoView(@NonNull Context context, @FrescoType int type) {
        return null;
    }

}
