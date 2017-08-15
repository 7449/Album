package com.album.ui.adapter;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.album.R;
import com.album.model.FinderModel;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * by y on 15/08/2017.
 */

public class ListPopupWindowAdapter extends BaseAdapter {
    private List<FinderModel> list = null;

    public ListPopupWindowAdapter(List<FinderModel> finderModel) {
        this.list = finderModel;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_finder, parent, false);
        }

        AppCompatImageView appCompatImageView = (AppCompatImageView) convertView.findViewById(R.id.iv_album_finder_icon);
        AppCompatTextView appCompatTextView = (AppCompatTextView) convertView.findViewById(R.id.tv_album_finder_name);

        if (list != null && list.get(position) != null) {
            FinderModel finderModel = list.get(position);
            appCompatTextView.setText(finderModel.getDirName());
            Glide
                    .with(parent.getContext())
                    .load(finderModel.getThumbnailsPath())
                    .placeholder(R.drawable.ic_launcher)
                    .error(R.drawable.ic_launcher)
                    .centerCrop()
                    .into(appCompatImageView);
        }

        return convertView;
    }
}
