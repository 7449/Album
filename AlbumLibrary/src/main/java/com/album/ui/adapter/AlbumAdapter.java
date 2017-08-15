package com.album.ui.adapter;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.album.AlbumConstant;
import com.album.R;
import com.album.model.AlbumModel;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * by y on 14/08/2017.
 */

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private List<AlbumModel> albumList = null;
    private OnItemClickListener onItemClickListener = null;

    public AlbumAdapter(List<AlbumModel> list) {
        this.albumList = list;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public AlbumAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null && albumList != null) {
                    onItemClickListener.onItemClick(v, viewHolder.getAdapterPosition(), albumList.get(viewHolder.getAdapterPosition()));
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AlbumAdapter.ViewHolder holder, int position) {
        if (albumList == null) {
            return;
        }
        AlbumModel albumModel = albumList.get(position);
        if (albumModel == null) {
            return;
        }
        Object path = albumModel.getPath();
        if (TextUtils.equals(String.valueOf(path), AlbumConstant.CAMERA)) {
            path = R.drawable.ic_action_camera;
        }
        Glide
                .with(holder.imageView.getContext())
                .load(path)
                .placeholder(R.drawable.ic_launcher)
                .error(R.drawable.ic_launcher)
                .centerCrop()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return albumList == null ? 0 : albumList.size();
    }

    public void addAll(List<AlbumModel> list) {
        if (albumList == null) {
            albumList = new ArrayList<>();
        } else {
            albumList.clear();
        }
        if (list != null) {
            albumList.addAll(list);
            notifyDataSetChanged();
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, AlbumModel albumModel);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private AppCompatImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = (AppCompatImageView) itemView.findViewById(R.id.album_image);
        }
    }
}
