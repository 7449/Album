package com.album.ui.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.album.Album;
import com.album.model.AlbumModel;
import com.album.ui.widget.TouchImageView;

import java.util.List;

/**
 * by y on 15/08/2017.
 */

public class PreviewAdapter extends PagerAdapter {

    private List<AlbumModel> list = null;

    public PreviewAdapter(List<AlbumModel> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        TouchImageView img = new TouchImageView(container.getContext());
        Album.getInstance().getAlbumImageLoader().displayPreview(img, list.get(position).getPath());
        container.addView(img);
        return img;
    }

    public String getAlbumPath(int position) {
        return list.get(position).getPath();
    }
}
