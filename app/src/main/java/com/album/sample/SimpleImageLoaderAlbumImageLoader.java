package com.album.sample;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.album.AlbumImageLoader;
import com.album.model.AlbumModel;
import com.album.model.FinderModel;
import com.album.ui.annotation.FrescoType;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

/**
 * by y on 20/08/2017.
 */

public class SimpleImageLoaderAlbumImageLoader implements AlbumImageLoader {

    private DisplayImageOptions displayImageOptions;

    public SimpleImageLoaderAlbumImageLoader() {
        if (displayImageOptions == null) {
            displayImageOptions = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.ic_launcher)
                    .showImageForEmptyUri(R.drawable.ic_launcher)
                    .showImageOnFail(R.drawable.ic_launcher)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
        }
    }

    @Override
    public void displayAlbum(@NonNull ImageView view, int width, int height, @NonNull AlbumModel albumModel) {
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImageLoader.getInstance().displayImage("file:///" + albumModel.getPath(), view, displayImageOptions, null);
    }


    @Override
    public void displayAlbumThumbnails(@NonNull ImageView view, @NonNull FinderModel finderModel) {
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImageLoader.getInstance().displayImage("file:///" + finderModel.getThumbnailsPath(), new ImageViewAware(view), displayImageOptions, null, null, null);
    }

    @Override
    public void displayPreview(@NonNull ImageView view, @NonNull AlbumModel albumModel) {
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImageLoader.getInstance().displayImage("file:///" + albumModel.getPath(), new ImageViewAware(view), displayImageOptions, null, null, null);
    }

    @Override
    public ImageView frescoView(@NonNull Context context, @FrescoType int type) {
        return null;
    }
}
