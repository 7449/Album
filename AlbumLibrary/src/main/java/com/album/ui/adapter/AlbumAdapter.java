package com.album.ui.adapter;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.album.Album;
import com.album.AlbumConfig;
import com.album.AlbumConstant;
import com.album.R;
import com.album.model.AlbumModel;

import java.util.ArrayList;
import java.util.List;

/**
 * by y on 14/08/2017.
 */

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private List<AlbumModel> albumList = null;
    private OnItemClickListener onItemClickListener = null;
    private AlbumConfig albumConfig = null;

    public AlbumAdapter(List<AlbumModel> list) {
        this.albumList = list;
        albumConfig = Album.getInstance().getConfig();
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
        String path = albumModel.getPath();
        if (TextUtils.equals(String.valueOf(path), AlbumConstant.CAMERA)) {
            Drawable drawable = ContextCompat.getDrawable(holder.imageView.getContext(), albumConfig.getAlbumContentViewCameraDrawable());
            drawable.setColorFilter(ContextCompat.getColor(holder.imageView.getContext(), albumConfig.getAlbumContentViewCameraDrawableColor()), PorterDuff.Mode.SRC_ATOP);
            holder.imageView.setBackgroundDrawable(drawable);
        } else {
            Album.getInstance().getAlbumImageLoader().displayAlbum(holder.imageView, path);
        }
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
