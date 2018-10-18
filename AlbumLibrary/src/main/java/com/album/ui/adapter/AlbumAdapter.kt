package com.album.ui.adapter

import android.graphics.PorterDuff
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.album.Album
import com.album.AlbumConfig
import com.album.AlbumConstant
import com.album.R
import com.album.entity.AlbumEntity
import com.album.ui.widget.AlbumImageView
import com.album.util.FileUtils

/**
 *   @author y
 */
class AlbumAdapter(private val list: ArrayList<AlbumEntity>, private val display: Int) : RecyclerView.Adapter<AlbumAdapter.ViewHolder>() {

    private var albumConfig: AlbumConfig = Album.instance.config
    private lateinit var onItemClickListener: OnItemClickListener
    private val layoutParams: FrameLayout.LayoutParams = FrameLayout.LayoutParams(display, display)
    private var multiplePreviewList: ArrayList<AlbumEntity> = ArrayList()

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.album_item_album, parent, false)
        val viewHolder = ViewHolder(view)
        view.setOnClickListener { v -> onItemClickListener.onItemClick(v, viewHolder.adapterPosition, list[viewHolder.adapterPosition]) }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val albumEntity = list[position]
        if (TextUtils.equals(albumEntity.path, AlbumConstant.CAMERA)) {
            holder.camera()
        } else {
            holder.cameraRootView.visibility = View.GONE
            holder.imageView.visibility = View.VISIBLE
            var childAt = holder.imageView.getChildAt(0)
            if (childAt == null) {
                childAt = if (albumConfig.isFrescoImageLoader) {
                    Album.instance.albumImageLoader.frescoView(holder.imageView.context, AlbumConstant.TYPE_FRESCO_ALBUM)!!
                } else {
                    AlbumImageView(holder.imageView.context)
                }
                childAt.layoutParams = layoutParams
                holder.imageView.addView(childAt)
            }
            Album.instance.albumImageLoader.displayAlbum(childAt as ImageView, display, display, albumEntity)
            if (!albumConfig.isRadio) {
                holder.checkBox.visibility = View.VISIBLE
                holder.checkBox.isChecked = albumEntity.isCheck
                holder.checkBox.setBackgroundResource(albumConfig.albumContentItemCheckBoxDrawable)
                holder.checkBox.setOnClickListener(View.OnClickListener {
                    if (!FileUtils.isFile(albumEntity.path)) {
                        holder.checkBox.isChecked = false
                        Album.instance.albumListener.onAlbumCheckBoxFileNull()
                        return@OnClickListener
                    }
                    if (!multiplePreviewList.contains(albumEntity) && multiplePreviewList.size >= albumConfig.multipleMaxCount) {
                        holder.checkBox.isChecked = false
                        Album.instance.albumListener.onAlbumMaxCount()
                        return@OnClickListener
                    }
                    if (!albumEntity.isCheck) {
                        albumEntity.isCheck = true
                        multiplePreviewList.add(albumEntity)
                    } else {
                        multiplePreviewList.remove(albumEntity)
                        albumEntity.isCheck = false
                    }
                })
            }
        }
    }

    override fun getItemCount(): Int = list.size

    fun addAll(newList: ArrayList<AlbumEntity>) {
        if (!newList.isEmpty()) {
            list.addAll(newList)
            notifyDataSetChanged()
        }
    }

    fun getAlbumList(): ArrayList<AlbumEntity> {
        return list
    }

    fun getMultiplePreviewList(): ArrayList<AlbumEntity> {
        return multiplePreviewList
    }

    fun setMultiplePreviewList(multiplePreviewList: ArrayList<AlbumEntity>) {
        this.multiplePreviewList = multiplePreviewList
        notifyDataSetChanged()
    }

    fun removeAll() {
        list.clear()
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int, albumEntity: AlbumEntity)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: FrameLayout = itemView.findViewById(R.id.album_image)
        val checkBox: AppCompatCheckBox = itemView.findViewById(R.id.album_check_box)
        private val imageCamera: AppCompatImageView = itemView.findViewById(R.id.album_image_camera)
        private val cameraTips: AppCompatTextView = itemView.findViewById(R.id.album_image_camera_tv)
        val cameraRootView: LinearLayout = itemView.findViewById(R.id.album_camera_root_view)

        fun camera() {
            val drawable = ContextCompat.getDrawable(itemView.context, albumConfig.albumContentViewCameraDrawable)
            drawable?.setColorFilter(ContextCompat.getColor(itemView.context, albumConfig.albumContentViewCameraDrawableColor),
                    PorterDuff.Mode.SRC_ATOP)
            cameraTips.setText(albumConfig.albumContentViewCameraTips)
            cameraTips.textSize = albumConfig.albumContentViewCameraTipsSize.toFloat()
            cameraTips.setTextColor(ContextCompat.getColor(itemView.context, albumConfig.albumContentViewCameraTipsColor))
            cameraRootView.setBackgroundColor(ContextCompat.getColor(itemView.context, albumConfig.albumContentViewCameraBackgroundColor))
            imageCamera.setImageDrawable(drawable)
            cameraRootView.visibility = View.VISIBLE
            imageView.visibility = View.GONE
            checkBox.visibility = View.GONE
        }
    }
}