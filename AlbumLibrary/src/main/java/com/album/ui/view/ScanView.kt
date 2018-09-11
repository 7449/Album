package com.album.ui.view

import android.database.Cursor
import com.album.entity.AlbumEntity
import com.album.entity.FinderEntity
import com.album.ui.widget.ScanCallBack
import java.util.*

/**
 * by y on 17/08/2017.
 */


interface ScanView {

    fun start(scanCallBack: ScanCallBack, bucketId: String, page: Int, count: Int)

    fun resultScan(scanCallBack: ScanCallBack, path: String)

    fun scanCursor(albumEntityList: ArrayList<AlbumEntity>, dataColumnIndex: Int, idColumnIndex: Int, sizeColumnIndex: Int, cursor: Cursor)

    fun cursorFinder(finderEntityList: ArrayList<FinderEntity>)

    fun cursorCount(bucketId: String): Int

    fun getCursor(bucketId: String, page: Int, count: Int): Cursor?

    fun getSelectionArgs(bucketId: String): Array<String>

}
