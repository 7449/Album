package com.gallery.core.ui.adapter.vh

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.gallery.core.Album
import com.gallery.core.AlbumBundle
import com.gallery.core.R
import com.gallery.core.action.AlbumAction
import com.gallery.core.ext.addChildView
import com.gallery.core.ext.fileExists
import com.gallery.core.ext.show
import com.gallery.scan.ScanEntity

class PhotoViewHolder(itemView: View,
                      private val albumBundle: AlbumBundle,
                      private val display: Int,
                      private val layoutParams: ViewGroup.LayoutParams,
                      private val albumAction: AlbumAction?) : RecyclerView.ViewHolder(itemView) {

    private val container: FrameLayout = itemView.findViewById(R.id.galleryContainer)
    private val checkBox: AppCompatCheckBox = itemView.findViewById(R.id.galleryCheckBox)

    fun photo(position: Int, albumEntity: ScanEntity, multipleList: ArrayList<ScanEntity>) {
        container.addChildView(Album.instance.albumImageLoader?.displayAlbum(display, display, albumEntity, container), layoutParams)
        container.setBackgroundColor(ContextCompat.getColor(itemView.context, albumBundle.photoBackgroundColor))
        if (albumBundle.radio) {
            return
        }
        checkBox.show()
        checkBox.isChecked = albumEntity.isCheck
        checkBox.setBackgroundResource(albumBundle.checkBoxDrawable)
        checkBox.setOnClickListener {
            if (!albumEntity.path.fileExists()) {
                checkBox.isChecked = false
                if (multipleList.contains(albumEntity)) {
                    multipleList.remove(albumEntity)
                }
                Album.instance.albumListener?.onAlbumCheckFileNotExist()
                return@setOnClickListener
            }
            if (albumAction?.onAlbumCheckBoxFilter(itemView, position, albumEntity) == true) {
                return@setOnClickListener
            }
            if (!multipleList.contains(albumEntity) && multipleList.size >= albumBundle.multipleMaxCount) {
                checkBox.isChecked = false
                Album.instance.albumListener?.onAlbumMaxCount()
                return@setOnClickListener
            }
            if (!albumEntity.isCheck) {
                albumEntity.isCheck = true
                multipleList.add(albumEntity)
            } else {
                multipleList.remove(albumEntity)
                albumEntity.isCheck = false
            }
            albumAction?.onChangedCheckBoxCount(itemView, multipleList.size, albumEntity)
            Album.instance.albumListener?.onAlbumCheckBox(multipleList.size, albumBundle.multipleMaxCount)
        }
    }
}