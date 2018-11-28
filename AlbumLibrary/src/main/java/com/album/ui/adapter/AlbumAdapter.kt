package com.album.ui.adapter

import android.graphics.PorterDuff
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.album.*
import com.album.util.FileUtils

/**
 *   @author y
 */
class AlbumAdapter(private val list: ArrayList<AlbumEntity>, private val display: Int, private val albumBundle: AlbumBundle) : RecyclerView.Adapter<AlbumAdapter.ViewHolder>() {

    private lateinit var onItemClickListener: OnItemClickListener
    private var multiplePreviewList: ArrayList<AlbumEntity> = ArrayList()
    private val layoutParams: FrameLayout.LayoutParams = FrameLayout.LayoutParams(display, display)

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
            return
        }
        holder.cameraRootView.visibility = View.GONE
        holder.imageView.visibility = View.VISIBLE
        holder.imageView.addView(Album.instance.albumImageLoader.displayAlbum(display, display, albumEntity, holder.imageView), layoutParams)
        if (!albumBundle.radio) {
            holder.checkBox.visibility = View.VISIBLE
            holder.checkBox.isChecked = albumEntity.isCheck
            holder.checkBox.setBackgroundResource(albumBundle.checkBoxDrawable)
            holder.checkBox.setOnClickListener(View.OnClickListener {
                if (!FileUtils.isFile(albumEntity.path)) {
                    holder.checkBox.isChecked = false
                    Album.instance.albumListener.onAlbumCheckFileNotExist()
                    return@OnClickListener
                }
                if (!multiplePreviewList.contains(albumEntity) && multiplePreviewList.size >= albumBundle.multipleMaxCount) {
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

    override fun getItemCount(): Int = list.size

    fun addAll(newList: ArrayList<AlbumEntity>) {
        if (!newList.isEmpty()) {
            list.addAll(newList)
            notifyDataSetChanged()
        }
    }

    fun getAlbumList(): ArrayList<AlbumEntity> = list

    fun getMultiplePreviewList(): ArrayList<AlbumEntity> = multiplePreviewList

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
            val drawable = ContextCompat.getDrawable(itemView.context, albumBundle.cameraDrawable)
            drawable?.setColorFilter(ContextCompat.getColor(itemView.context, albumBundle.cameraDrawableColor), PorterDuff.Mode.SRC_ATOP)
            cameraTips.setText(albumBundle.cameraText)
            cameraTips.textSize = albumBundle.cameraTextSize
            cameraTips.setTextColor(ContextCompat.getColor(itemView.context, albumBundle.cameraTextColor))
            cameraRootView.setBackgroundColor(ContextCompat.getColor(itemView.context, albumBundle.cameraBackgroundColor))
            imageCamera.setImageDrawable(drawable)
            cameraRootView.visibility = View.VISIBLE
            imageView.visibility = View.GONE
            checkBox.visibility = View.GONE
        }
    }
}