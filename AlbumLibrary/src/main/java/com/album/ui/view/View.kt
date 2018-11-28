package com.album.ui.view

import android.app.Activity
import android.os.Bundle
import com.album.AlbumEntity
import com.album.AlbumResultType
import com.album.FinderEntity
import java.io.File
import java.util.*

interface AlbumView {

    fun getSelectEntity(): ArrayList<AlbumEntity>

    fun getAlbumActivity(): Activity

    fun getPage(): Int

    fun showProgress()

    fun hideProgress()

    fun scanSuccess(albumEntityList: ArrayList<AlbumEntity>)

    fun scanFinder(list: ArrayList<FinderEntity>)

    fun onAlbumNoMore()

    fun onAlbumEmpty()

    fun resultSuccess(albumEntity: AlbumEntity?)
}

interface AlbumMethodFragmentView {

    fun getFinderEntity(): List<FinderEntity>

    fun disconnectMediaScanner()

    fun onScanAlbum(bucketId: String, isFinder: Boolean, result: Boolean)

    fun openCamera()

    fun openUCrop(path: String)

    fun refreshMedia(@AlbumResultType type: Int, file: File)

    fun multiplePreview(): ArrayList<AlbumEntity>?

    fun multipleSelect()

    fun onResultPreview(bundle: Bundle)
}


interface PrevView {
    fun getPreViewActivity(): Activity

    fun scanSuccess(albumEntityList: ArrayList<AlbumEntity>)

    fun hideProgress()

    fun showProgress()
}

interface PrevFragmentToAtyListener {
    fun onChangedCount(currentPos: Int)
    fun onChangedToolbarCount(currentPos: Int, maxPos: Int)
}