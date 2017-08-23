package com.album.sample;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.album.AlbumImageLoader;
import com.album.R;
import com.album.model.AlbumModel;
import com.album.model.FinderModel;
import com.album.ui.annotation.FrescoType;
import com.squareup.picasso.Picasso;

/**
 * by y on 19/08/2017.
 */

public class SimplePicassoAlbumImageLoader implements AlbumImageLoader {

    @Override
    public void displayAlbum(@NonNull ImageView view, int width, int height, @NonNull AlbumModel albumModel) {
        Picasso.with(view.getContext())
                .load(ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, albumModel.getId()))
                .config(Bitmap.Config.RGB_565)
                .placeholder(R.drawable.ic_launcher)
                .fit().centerCrop()
                .tag(view.getContext())
                .into(view);
    }

    @Override
    public void displayAlbumThumbnails(@NonNull ImageView view, @NonNull FinderModel finderModel) {
        Picasso.with(view.getContext())
                .load(ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, finderModel.getThumbnailsId()))
                .config(Bitmap.Config.RGB_565)
                .placeholder(R.drawable.ic_launcher)
                .error(R.drawable.ic_launcher)
                .fit().centerCrop()
                .tag(view.getContext())
                .into(view);
    }

    @Override
    public void displayPreview(@NonNull ImageView view, @NonNull AlbumModel albumModel) {
        Picasso.with(view.getContext())
                .load(ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, albumModel.getId()))
                .config(Bitmap.Config.RGB_565)
                .placeholder(R.drawable.ic_launcher)
                .error(R.drawable.ic_launcher)
                .resize(400, 300)
                .centerInside()
                .tag(view.getContext())
                .into(view);
    }

    @Override
    public ImageView frescoView(@NonNull Context context, @FrescoType int type) {
        return null;
    }
}
