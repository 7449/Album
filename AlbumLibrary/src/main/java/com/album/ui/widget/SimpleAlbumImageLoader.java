package com.album.ui.widget;

import android.widget.ImageView;

import com.album.AlbumImageLoader;
import com.album.R;
import com.bumptech.glide.Glide;

/**
 * by y on 15/08/2017.
 */

public class SimpleAlbumImageLoader implements AlbumImageLoader {


    @Override
    public void displayAlbum(ImageView view, String path) {
        Glide
                .with(view.getContext())
                .load(path)
                .placeholder(R.drawable.ic_launcher)
                .error(R.drawable.ic_launcher)
                .centerCrop()
                .into(view);
    }

    @Override
    public void displayAlbumThumbnails(ImageView appCompatImageView, String thumbnailsPath) {
        Glide
                .with(appCompatImageView.getContext())
                .load(thumbnailsPath)
                .placeholder(R.drawable.ic_launcher)
                .error(R.drawable.ic_launcher)
                .centerCrop()
                .into(appCompatImageView);
    }

    @Override
    public void displayPreview(ImageView img, String path) {
        Glide
                .with(img.getContext())
                .load(path)
                .placeholder(R.drawable.ic_launcher)
                .error(R.drawable.ic_launcher)
                .centerCrop()
                .into(img);
    }
}
