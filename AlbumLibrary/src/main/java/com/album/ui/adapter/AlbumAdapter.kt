package com.album.ui.adapter

import android.graphics.PorterDuff
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
import com.album.Album
import com.album.AlbumBundle
import com.album.CAMERA
import com.album.R
import com.album.core.fileExists
import com.album.core.scan.AlbumEntity
import com.album.core.show
import com.album.listener.AlbumParentListener

/**
 *   @author y
 */
class AlbumAdapter(
        private val display: Int,
        private val albumBundle: AlbumBundle,
        private val albumParentListener: AlbumParentListener?,
        private val onAlbumItemClickListener: OnAlbumItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        internal const val TYPE_CAMERA = 0
        internal const val TYPE_PHOTO = 1
    }

    /**
     * 图片数据
     */
    var albumList: ArrayList<AlbumEntity> = ArrayList()

    /**
     * 多选时的临时数据
     */
    var multipleList: ArrayList<AlbumEntity> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    /**
     * item 的 layoutParams,为正方形
     */
    private val layoutParams: FrameLayout.LayoutParams = FrameLayout.LayoutParams(display, display)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            AlbumAdapter.TYPE_CAMERA -> {
                val cameraView: View = LayoutInflater.from(parent.context).inflate(R.layout.album_item_album_camera, parent, false)
                val cameraViewHolder = CameraViewHolder(cameraView, albumBundle)
                cameraView.setOnClickListener { v -> onAlbumItemClickListener.onCameraItemClick(v, cameraViewHolder.adapterPosition, albumList[cameraViewHolder.adapterPosition]) }
                cameraViewHolder
            }
            else -> {
                val photoView: View = LayoutInflater.from(parent.context).inflate(R.layout.album_item_album, parent, false)
                val photoViewHolder = PhotoViewHolder(photoView, albumBundle, display, layoutParams, albumParentListener, onAlbumItemClickListener)
                photoView.setOnClickListener { v -> onAlbumItemClickListener.onPhotoItemClick(v, photoViewHolder.adapterPosition, albumList[photoViewHolder.adapterPosition]) }
                photoViewHolder
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val albumEntity = albumList[position]

        if (holder is CameraViewHolder) {

            holder.camera()

        } else if (holder is PhotoViewHolder) {

            holder.photo(position, albumEntity, multipleList)
        }
    }

    override fun getItemCount(): Int = albumList.size

    override fun getItemViewType(position: Int): Int {
        return when {
            albumList.isEmpty() -> AlbumAdapter.TYPE_PHOTO
            albumList[position].path == CAMERA -> AlbumAdapter.TYPE_CAMERA
            else -> AlbumAdapter.TYPE_PHOTO
        }
    }

    /**
     * 添加新数据
     */
    fun addAll(newList: ArrayList<AlbumEntity>) {
        albumList.addAll(newList)
        notifyDataSetChanged()
    }

    /**
     * 清除所有数据
     */
    fun removeAll() {
        albumList.clear()
        notifyDataSetChanged()
    }

    interface OnAlbumItemClickListener {
        fun onItemCheckBoxClick(view: View, currentMaxCount: Int, albumEntity: AlbumEntity)
        fun onCameraItemClick(view: View, position: Int, albumEntity: AlbumEntity)
        fun onPhotoItemClick(view: View, position: Int, albumEntity: AlbumEntity)
    }


    class CameraViewHolder(itemView: View, private val albumBundle: AlbumBundle) : RecyclerView.ViewHolder(itemView) {

        private val container: LinearLayout = itemView.findViewById(R.id.album_camera_root_view)
        private val cameraIv: AppCompatImageView = itemView.findViewById(R.id.album_image_camera)
        private val cameraTv: AppCompatTextView = itemView.findViewById(R.id.album_image_camera_tv)

        fun camera() {
            val drawable = ContextCompat.getDrawable(itemView.context, albumBundle.cameraDrawable)
            drawable?.setColorFilter(ContextCompat.getColor(itemView.context, albumBundle.cameraDrawableColor), PorterDuff.Mode.SRC_ATOP)
            cameraTv.setText(albumBundle.cameraText)
            cameraTv.textSize = albumBundle.cameraTextSize
            cameraTv.setTextColor(ContextCompat.getColor(itemView.context, albumBundle.cameraTextColor))
            container.setBackgroundColor(ContextCompat.getColor(itemView.context, albumBundle.cameraBackgroundColor))
            cameraIv.setImageDrawable(drawable)
        }
    }

    class PhotoViewHolder(itemView: View,
                          private val albumBundle: AlbumBundle,
                          private val display: Int,
                          private val layoutParams: ViewGroup.LayoutParams,
                          private val albumParentListener: AlbumParentListener?,
                          private val onAlbumItemClickListener: OnAlbumItemClickListener) : RecyclerView.ViewHolder(itemView) {

        private val container: FrameLayout = itemView.findViewById(R.id.album_container)
        private val checkBox: AppCompatCheckBox = itemView.findViewById(R.id.album_check_box)

        fun photo(position: Int, albumEntity: AlbumEntity, multipleList: ArrayList<AlbumEntity>) {
            val imageView = Album.instance.albumImageLoader?.displayAlbum(display, display, albumEntity, container)
            imageView?.let { container.addView(it, layoutParams) }
            container.setBackgroundColor(ContextCompat.getColor(itemView.context, albumBundle.photoBackgroundColor))

            if (albumBundle.radio) {
                return
            }

            checkBox.show()
            checkBox.isChecked = albumEntity.isCheck
            checkBox.setBackgroundResource(albumBundle.checkBoxDrawable)
            checkBox.setOnClickListener(View.OnClickListener {

                if (albumParentListener?.onAlbumCheckBoxFilter(itemView, position, albumEntity) == true) {
                    return@OnClickListener
                }

                if (!albumEntity.path.fileExists()) {
                    checkBox.isChecked = false
                    Album.instance.albumListener?.onAlbumCheckFileNotExist()
                    return@OnClickListener
                }
                if (!multipleList.contains(albumEntity) && multipleList.size >= albumBundle.multipleMaxCount) {
                    checkBox.isChecked = false
                    Album.instance.albumListener?.onAlbumMaxCount()
                    return@OnClickListener
                }
                if (!albumEntity.isCheck) {
                    albumEntity.isCheck = true
                    multipleList.add(albumEntity)
                } else {
                    multipleList.remove(albumEntity)
                    albumEntity.isCheck = false
                }
                onAlbumItemClickListener.onItemCheckBoxClick(itemView, multipleList.size, albumEntity)
                Album.instance.albumListener?.onAlbumCheckBox(multipleList.size, albumBundle.multipleMaxCount)
            })
        }
    }
}
