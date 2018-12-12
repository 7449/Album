package com.album.ui.view

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.album.AlbumEntity
import com.album.FinderEntity
import java.io.File
import java.util.*

interface AlbumView {

    fun getSelectEntity(): ArrayList<AlbumEntity>

    fun getAlbumActivity(): FragmentActivity

    fun getPage(): Int

    fun showProgress()

    fun hideProgress()

    fun scanSuccess(albumEntityList: ArrayList<AlbumEntity>)

    fun scanFinderSuccess(list: ArrayList<FinderEntity>)

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

    fun refreshMedia(type: Int, file: File)

    fun selectPreview(): ArrayList<AlbumEntity>?

    fun multipleSelect()

    fun onResultPreview(bundle: Bundle)
}


interface PrevView {

    fun getPrevContext(): FragmentActivity

    fun scanSuccess(albumEntityList: ArrayList<AlbumEntity>)

    fun hideProgress()

    fun showProgress()
}
