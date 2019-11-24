package com.album.sample.kt

import android.view.View
import android.widget.FrameLayout
import com.album.core.action.AlbumImageLoader
import com.album.scan.ScanEntity

class SimpleAlbumImageLoaderKt {

    private lateinit var displayAlbum: ((width: Int, height: Int, albumEntity: ScanEntity, container: FrameLayout) -> View)
    private lateinit var displayAlbumThumbnails: ((finderEntity: ScanEntity, container: FrameLayout) -> View)
    private lateinit var displayAlbumPreview: ((albumEntity: ScanEntity, container: FrameLayout) -> View)

    fun displayAlbum(displayAlbum: (width: Int, height: Int, albumEntity: ScanEntity, container: FrameLayout) -> View) {
        this.displayAlbum = displayAlbum
    }

    fun displayAlbumThumbnails(displayAlbumThumbnails: (finderEntity: ScanEntity, container: FrameLayout) -> View) {
        this.displayAlbumThumbnails = displayAlbumThumbnails
    }

    fun displayAlbumPreview(displayAlbumPreview: (albumEntity: ScanEntity, container: FrameLayout) -> View) {
        this.displayAlbumPreview = displayAlbumPreview
    }

    internal fun build(): AlbumImageLoader {
        return object : AlbumImageLoader {
            override fun displayAlbum(width: Int, height: Int, albumEntity: ScanEntity, container: FrameLayout): View = displayAlbum.invoke(width, height, albumEntity, container)
            override fun displayAlbumThumbnails(finderEntity: ScanEntity, container: FrameLayout): View = displayAlbumThumbnails.invoke(finderEntity, container)
            override fun displayAlbumPreview(albumEntity: ScanEntity, container: FrameLayout): View = displayAlbumPreview.invoke(albumEntity, container)
        }
    }
}
