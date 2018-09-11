package com.album.ui.view

import android.net.Uri
import android.os.Bundle

import com.album.annotation.AlbumResultType
import com.album.entity.FinderEntity

/**
 * by y on 15/08/2017.
 */

interface AlbumMethodFragmentView {

    fun getFinderEntity(): List<FinderEntity>

    fun initRecyclerView()

    fun disconnectMediaScanner()

    fun onScanAlbum(bucketId: String, isFinder: Boolean, result: Boolean)

    fun openCamera()

    fun openUCrop(path: String, uri: Uri)

    fun refreshMedia(@AlbumResultType type: Int)

    fun multiplePreview()

    fun multipleSelect()

    fun onResultPreview(bundle: Bundle)
}
