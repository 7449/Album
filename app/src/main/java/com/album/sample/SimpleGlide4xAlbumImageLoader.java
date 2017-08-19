package com.album.sample;

import android.widget.ImageView;

import com.album.AlbumImageLoader;
import com.album.model.AlbumModel;
import com.album.model.FinderModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

/**
 * by y on 20/08/2017.
 */

public class SimpleGlide4xAlbumImageLoader implements AlbumImageLoader {

    private RequestOptions requestOptions;

    public SimpleGlide4xAlbumImageLoader() {
        requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_launcher)
                .error(R.drawable.ic_launcher)
                .centerCrop();
    }

    @Override
    public void displayAlbum(ImageView view, AlbumModel albumModel) {
        Glide
                .with(view.getContext())
                .load(albumModel.getPath()).apply(requestOptions)
                .into(view);
    }

    @Override
    public void displayAlbumThumbnails(ImageView view, FinderModel finderModel) {
        Glide
                .with(view.getContext())
                .load(finderModel.getThumbnailsPath()).apply(requestOptions)
                .into(view);
    }

    @Override
    public void displayPreview(ImageView view, AlbumModel albumModel) {
        Glide
                .with(view.getContext())
                .load(albumModel.getPath()).apply(requestOptions)
                .into(view);
    }
}
