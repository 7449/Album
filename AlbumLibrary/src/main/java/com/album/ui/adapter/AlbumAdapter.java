package com.album.ui.adapter;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.album.Album;
import com.album.AlbumConfig;
import com.album.AlbumConstant;
import com.album.R;
import com.album.model.AlbumModel;
import com.album.ui.widget.AlbumImageView;
import com.album.util.FileUtils;

import java.util.ArrayList;

/**
 * by y on 14/08/2017.
 */

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {


    private ArrayList<AlbumModel> albumList = null;
    private ArrayList<AlbumModel> multiplePreviewList = null;
    private OnItemClickListener onItemClickListener = null;
    private AlbumConfig albumConfig = null;
    private int display;
    private final FrameLayout.LayoutParams layoutParams;

    public AlbumAdapter(ArrayList<AlbumModel> list, int display) {
        this.albumList = list;
        this.display = display;
        albumConfig = Album.getInstance().getConfig();
        layoutParams = new FrameLayout.LayoutParams(display, display);
        if (!albumConfig.isRadio()) {
            multiplePreviewList = new ArrayList<>();
        }
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
    public void onBindViewHolder(final AlbumAdapter.ViewHolder holder, int position) {

        if (albumList == null) {
            return;
        }
        final AlbumModel albumModel = albumList.get(position);
        if (albumModel == null) {
            return;
        }
        String path = albumModel.getPath();
        if (TextUtils.equals(String.valueOf(path), AlbumConstant.CAMERA)) {
            holder.camera();
        } else {
            holder.cameraRootView.setVisibility(View.GONE);
            holder.imageView.setVisibility(View.VISIBLE);
            ImageView imageView;
            if (albumConfig.isFrescoImageLoader()) {
                imageView = Album.getInstance().getAlbumImageLoader().frescoView(holder.imageView.getContext(), AlbumConstant.TYPE_FRESCO_ALBUM);
            } else {
                imageView = new AlbumImageView(holder.imageView.getContext());
            }
            imageView.setLayoutParams(layoutParams);
            holder.imageView.addView(imageView);
            Album.getInstance().getAlbumImageLoader().displayAlbum(imageView, display, display, albumModel);
            if (!albumConfig.isRadio()) {
                holder.checkBox.setVisibility(View.VISIBLE);
                holder.checkBox.setChecked(albumModel.isCheck());
                holder.checkBox.setBackgroundResource(albumConfig.getAlbumContentItemCheckBoxDrawable());
                holder.checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!FileUtils.isFile(albumModel.getPath())) {
                            holder.checkBox.setChecked(false);
                            Album.getInstance().getAlbumListener().onAlbumCheckBoxFileNull();
                            return;
                        }
                        if (!multiplePreviewList.contains(albumModel) && multiplePreviewList.size() >= albumConfig.getMultipleMaxCount()) {
                            holder.checkBox.setChecked(false);
                            Album.getInstance().getAlbumListener().onAlbumMaxCount();
                            return;
                        }
                        if (!albumModel.isCheck()) {
                            albumModel.setCheck(true);
                            multiplePreviewList.add(albumModel);
                        } else {
                            multiplePreviewList.remove(albumModel);
                            albumModel.setCheck(false);
                        }
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return albumList == null ? 0 : albumList.size();
    }

    public void addAll(ArrayList<AlbumModel> list) {
        if (albumList == null) {
            albumList = new ArrayList<>();
        }
        if (list != null && !list.isEmpty()) {
            albumList.addAll(list);
            notifyDataSetChanged();
        }
    }

    public ArrayList<AlbumModel> getAlbumList() {
        return albumList;
    }

    public ArrayList<AlbumModel> getMultiplePreviewList() {
        return multiplePreviewList;
    }

    public void setMultiplePreviewList(ArrayList<AlbumModel> multiplePreviewList) {
        this.multiplePreviewList = multiplePreviewList;
        notifyDataSetChanged();
    }

    public void removeAll() {
        if (albumList != null) {
            albumList.clear();
        }
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, AlbumModel albumModel);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private FrameLayout imageView;
        private AppCompatCheckBox checkBox;
        private AppCompatImageView imageCamera;
        private AppCompatTextView cameraTips;
        private LinearLayout cameraRootView;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = (FrameLayout) itemView.findViewById(R.id.album_image);
            checkBox = (AppCompatCheckBox) itemView.findViewById(R.id.album_check_box);
            imageCamera = (AppCompatImageView) itemView.findViewById(R.id.album_image_camera);
            cameraTips = (AppCompatTextView) itemView.findViewById(R.id.album_image_camera_tv);
            cameraRootView = (LinearLayout) itemView.findViewById(R.id.album_camera_root_view);
        }

        void camera() {
            Drawable drawable = ContextCompat.getDrawable(itemView.getContext(), albumConfig.getAlbumContentViewCameraDrawable());
            drawable.setColorFilter(ContextCompat.getColor(itemView.getContext(), albumConfig.getAlbumContentViewCameraDrawableColor()), PorterDuff.Mode.SRC_ATOP);
            cameraTips.setText(albumConfig.getAlbumContentViewCameraTips());
            cameraTips.setTextSize(albumConfig.getAlbumContentViewCameraTipsSize());
            cameraTips.setTextColor(ContextCompat.getColor(itemView.getContext(), albumConfig.getAlbumContentViewCameraTipsColor()));
            cameraRootView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), albumConfig.getAlbumContentViewCameraBackgroundColor()));
            imageCamera.setImageDrawable(drawable);
            cameraRootView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
            checkBox.setVisibility(View.GONE);
        }
    }
}
