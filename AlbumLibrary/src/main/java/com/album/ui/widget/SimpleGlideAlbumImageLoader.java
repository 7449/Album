package com.album.ui.widget;

import android.widget.ImageView;

import com.album.AlbumImageLoader;
import com.album.R;
import com.album.model.AlbumModel;
import com.album.model.FinderModel;
import com.bumptech.glide.Glide;

/**
 * by y on 15/08/2017.
 */

public class SimpleGlideAlbumImageLoader implements AlbumImageLoader {

    @Override
    public void displayAlbum(ImageView view, AlbumModel albumModel) {
        Glide
                .with(view.getContext())
                .load(albumModel.getPath())
                .placeholder(R.drawable.ic_launcher)
                .error(R.drawable.ic_launcher)
                .centerCrop()
                .into(view);
    }

    @Override
    public void displayAlbumThumbnails(ImageView view, FinderModel finderModel) {
        Glide
                .with(view.getContext())
                .load(finderModel.getThumbnailsPath())
                .placeholder(R.drawable.ic_launcher)
                .error(R.drawable.ic_launcher)
                .centerCrop()
                .into(view);
    }

    @Override
    public void displayPreview(ImageView view, AlbumModel albumModel) {
        Glide
                .with(view.getContext())
                .load(albumModel.getPath())
                .placeholder(R.drawable.ic_launcher)
                .error(R.drawable.ic_launcher)
                .centerCrop()
                .into(view);
    }
}
