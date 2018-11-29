package com.album.util.scan

import android.content.ContentResolver
import android.database.Cursor
import android.provider.MediaStore
import com.album.AlbumEntity
import com.album.FinderEntity
import java.util.*

interface ScanView {
    fun startScan(scanCallBack: ScanCallBack, bucketId: String, page: Int, count: Int, filterImage: Boolean, sdName: String)

    fun resultScan(scanCallBack: ScanCallBack, path: String, filterImage: Boolean, sdName: String)

    fun scanCursor(albumEntityList: ArrayList<AlbumEntity>, dataColumnIndex: Int, idColumnIndex: Int, sizeColumnIndex: Int, filterImage: Boolean, cursor: Cursor)

    fun cursorFinder(finderEntityList: ArrayList<FinderEntity>, filterImage: Boolean, sdName: String)

    fun cursorCount(bucketId: String, filterImage: Boolean): Int

    fun getCursor(bucketId: String, page: Int, count: Int, filterImage: Boolean): Cursor?

    fun getSelectionArgs(bucketId: String): Array<String>
}

interface ScanCallBack {
    fun scanCallBack(imageList: ArrayList<AlbumEntity>, finderList: ArrayList<FinderEntity>)

    fun resultCallBack(image: AlbumEntity?, finderList: ArrayList<FinderEntity>)
}

abstract class ScanBase(private val contentResolver: ContentResolver) : ScanView {
    override fun startScan(scanCallBack: ScanCallBack, bucketId: String, page: Int, count: Int, filterImage: Boolean, sdName: String) {
        val albumEntityList = ArrayList<AlbumEntity>()
        val finderEntityList = ArrayList<FinderEntity>()
        val cursor = getCursor(bucketId, page, count, filterImage)
        if (cursor != null) {
            val dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
            val idColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
            val sizeColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.SIZE)
            while (cursor.moveToNext()) {
                scanCursor(albumEntityList, dataColumnIndex, idColumnIndex, sizeColumnIndex, filterImage, cursor)
            }
            cursor.close()
            cursorFinder(finderEntityList, filterImage, sdName)
            scanCallBack.scanCallBack(albumEntityList, finderEntityList)
        }
    }
}