package com.album.ui.adapter

import android.graphics.PorterDuff
import android.text.TextUtils
import android.view.Gravity
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
import com.album.core.AlbumFile.fileExists
import com.album.core.AlbumView.hide
import com.album.core.AlbumView.show
import com.album.core.scan.AlbumEntity

/**
 *   @author y
 */
class AlbumAdapter(
        private val display: Int,
        private val albumBundle: AlbumBundle,
        private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<AlbumAdapter.ViewHolder>() {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.album_item_album, parent, false)
        val viewHolder = ViewHolder(view, albumBundle)
        view.setOnClickListener { v -> onItemClickListener.onItemClick(v, viewHolder.adapterPosition, albumList[viewHolder.adapterPosition]) }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val albumEntity = albumList[position]

        // init camera
        if (TextUtils.equals(albumEntity.path, CAMERA)) {
            holder.camera()
            return
        }

        // imageLoader
        holder.container.addView(Album.instance.albumImageLoader?.displayAlbum(display, display, albumEntity, holder.container), layoutParams)

        if (albumBundle.radio) {
            return
        }

        holder.checkBox.show()
        holder.checkBox.isChecked = albumEntity.isCheck
        holder.checkBox.setBackgroundResource(albumBundle.checkBoxDrawable)
        holder.checkBox.setOnClickListener(View.OnClickListener {
            if (!albumEntity.path.fileExists()) {
                holder.checkBox.isChecked = false
                Album.instance.albumListener?.onAlbumCheckFileNotExist()
                return@OnClickListener
            }
            if (!multipleList.contains(albumEntity) && multipleList.size >= albumBundle.multipleMaxCount) {
                holder.checkBox.isChecked = false
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
            Album.instance.albumListener?.onCheckBoxAlbum(multipleList.size, albumBundle.multipleMaxCount)
        })
    }

    override fun getItemCount(): Int = albumList.size

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

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int, albumEntity: AlbumEntity)
    }

    class ViewHolder(itemView: View, private val albumBundle: AlbumBundle) : RecyclerView.ViewHolder(itemView) {

        val container: FrameLayout = itemView.findViewById(R.id.album_container)
        val checkBox: AppCompatCheckBox = itemView.findViewById(R.id.album_check_box)

        fun camera() {

            container.removeAllViews()

            val linearLayout = LinearLayout(itemView.context)
            linearLayout.orientation = LinearLayout.VERTICAL

            val cameraIv = AppCompatImageView(linearLayout.context)
            val cameraTv = AppCompatTextView(linearLayout.context)
            cameraTv.gravity = Gravity.CENTER

            linearLayout.addView(cameraIv, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f))
            linearLayout.addView(cameraTv)
            container.addView(linearLayout)

            val drawable = ContextCompat.getDrawable(itemView.context, albumBundle.cameraDrawable)
            drawable?.setColorFilter(ContextCompat.getColor(itemView.context, albumBundle.cameraDrawableColor), PorterDuff.Mode.SRC_ATOP)
            cameraTv.setText(albumBundle.cameraText)
            cameraTv.textSize = albumBundle.cameraTextSize
            cameraTv.setTextColor(ContextCompat.getColor(itemView.context, albumBundle.cameraTextColor))
            container.setBackgroundColor(ContextCompat.getColor(itemView.context, albumBundle.cameraBackgroundColor))
            cameraIv.setImageDrawable(drawable)
            checkBox.hide()

        }
    }
}
