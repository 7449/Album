package com.album.sample.kt

import android.view.View
import android.widget.FrameLayout
import com.album.core.scan.AlbumEntity
import com.album.action.AlbumImageLoader

class SimpleAlbumImageLoaderKt {

    private lateinit var displayAlbum: ((width: Int, height: Int, albumEntity: AlbumEntity, container: FrameLayout) -> View)
    private lateinit var displayAlbumThumbnails: ((finderEntity: AlbumEntity, container: FrameLayout) -> View)
    private lateinit var displayAlbumPreview: ((albumEntity: AlbumEntity, container: FrameLayout) -> View)

    fun displayAlbum(displayAlbum: (width: Int, height: Int, albumEntity: AlbumEntity, container: FrameLayout) -> View) {
        this.displayAlbum = displayAlbum
    }

    fun displayAlbumThumbnails(displayAlbumThumbnails: (finderEntity: AlbumEntity, container: FrameLayout) -> View) {
        this.displayAlbumThumbnails = displayAlbumThumbnails
    }

    fun displayAlbumPreview(displayAlbumPreview: (albumEntity: AlbumEntity, container: FrameLayout) -> View) {
        this.displayAlbumPreview = displayAlbumPreview
    }

    internal fun build(): AlbumImageLoader {
        return object : AlbumImageLoader {
            override fun displayAlbum(width: Int, height: Int, albumEntity: AlbumEntity, container: FrameLayout): View = displayAlbum.invoke(width, height, albumEntity, container)
            override fun displayAlbumThumbnails(finderEntity: AlbumEntity, container: FrameLayout): View = displayAlbumThumbnails.invoke(finderEntity, container)
            override fun displayAlbumPreview(albumEntity: AlbumEntity, container: FrameLayout): View = displayAlbumPreview.invoke(albumEntity, container)
        }
    }
}
