package com.album.core.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.album.core.AlbumBundle
import com.album.core.AlbumInternalConst
import com.album.core.R
import com.album.core.action.AlbumAction
import com.album.core.ui.adapter.vh.CameraViewHolder
import com.album.core.ui.adapter.vh.PhotoViewHolder
import com.album.scan.scan.AlbumEntity

class AlbumAdapter(
        private val display: Int,
        private val albumBundle: AlbumBundle,
        private val albumAction: AlbumAction?,
        private val onAlbumItemClickListener: OnAlbumItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnAlbumItemClickListener {
        fun onCameraItemClick(view: View, position: Int, albumEntity: AlbumEntity)
        fun onPhotoItemClick(view: View, position: Int, albumEntity: AlbumEntity)
    }

    companion object {
        private const val TYPE_CAMERA = 0
        private const val TYPE_PHOTO = 1
    }

    var albumList: ArrayList<AlbumEntity> = ArrayList()

    var multipleList: ArrayList<AlbumEntity> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private val layoutParams: FrameLayout.LayoutParams = FrameLayout.LayoutParams(display, display)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_CAMERA -> {
                val cameraView: View = LayoutInflater.from(parent.context).inflate(R.layout.album_item_album_camera, parent, false)
                val cameraViewHolder = CameraViewHolder(cameraView, albumBundle)
                cameraView.setOnClickListener { v -> onAlbumItemClickListener.onCameraItemClick(v, cameraViewHolder.adapterPosition, albumList[cameraViewHolder.adapterPosition]) }
                cameraViewHolder
            }
            else -> {
                val photoView: View = LayoutInflater.from(parent.context).inflate(R.layout.album_item_album, parent, false)
                val photoViewHolder = PhotoViewHolder(photoView, albumBundle, display, layoutParams, albumAction)
                photoView.setOnClickListener { v -> onAlbumItemClickListener.onPhotoItemClick(v, photoViewHolder.adapterPosition, albumList[photoViewHolder.adapterPosition]) }
                photoViewHolder
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CameraViewHolder) {
            holder.camera()
        } else if (holder is PhotoViewHolder) {
            holder.photo(position, albumList[position], multipleList)
        }
    }

    override fun getItemCount(): Int = albumList.size

    override fun getItemViewType(position: Int): Int = when {
        albumList.isEmpty() -> TYPE_PHOTO
        albumList[position].path == AlbumInternalConst.CAMERA -> TYPE_CAMERA
        else -> TYPE_PHOTO
    }

    fun addAll(newList: ArrayList<AlbumEntity>) {
        albumList.addAll(newList)
        notifyDataSetChanged()
    }

    fun removeAll() {
        albumList.clear()
        notifyDataSetChanged()
    }
}
