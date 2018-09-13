package com.album.ui.view

import android.os.Bundle
import com.album.annotation.AlbumResultType
import com.album.entity.FinderEntity
import java.io.File

/**
 * by y on 15/08/2017.
 */

interface AlbumMethodFragmentView {

    fun getFinderEntity(): List<FinderEntity>

    fun initRecyclerView()

    fun disconnectMediaScanner()

    fun onScanAlbum(bucketId: String, isFinder: Boolean, result: Boolean)

    fun openCamera()

    fun openUCrop(path: String)

    fun refreshMedia(@AlbumResultType type: Int, file: File)

    fun multiplePreview()

    fun multipleSelect()

    fun onResultPreview(bundle: Bundle)
}
