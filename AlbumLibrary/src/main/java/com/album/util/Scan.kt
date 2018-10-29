package com.album.util

import android.content.ContentResolver
import android.database.Cursor
import android.provider.MediaStore
import com.album.AlbumEntity
import com.album.FinderEntity
import java.util.*

enum class ScanType {
    VIDEO,
    IMAGE,
//    MIXING
}

interface ScanView {
    fun scan(scanCallBack: ScanCallBack, bucketId: String, page: Int, count: Int)
    fun resultScan(scanCallBack: ScanCallBack, path: String)
    fun scanCursor(albumEntityList: ArrayList<AlbumEntity>, dataColumnIndex: Int, idColumnIndex: Int, sizeColumnIndex: Int, cursor: Cursor)
    fun cursorFinder(finderEntityList: ArrayList<FinderEntity>)
    fun cursorCount(bucketId: String): Int
    fun getCursor(bucketId: String, page: Int, count: Int): Cursor?
    fun getSelectionArgs(bucketId: String): Array<String>
}

interface ScanCallBack {
    fun scanCallBack(albumEntityList: ArrayList<AlbumEntity>, list: ArrayList<FinderEntity>)
    fun resultCallBack(albumEntity: AlbumEntity?, finderEntityList: ArrayList<FinderEntity>)
}

abstract class ScanBase(private val contentResolver: ContentResolver) : ScanView {
    override fun scan(scanCallBack: ScanCallBack, bucketId: String, page: Int, count: Int) {
        val albumEntityList = ArrayList<AlbumEntity>()
        val finderEntityList = ArrayList<FinderEntity>()
        val cursor = getCursor(bucketId, page, count)
        if (cursor != null) {
            val dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
            val idColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
            val sizeColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.SIZE)
            while (cursor.moveToNext()) {
                scanCursor(albumEntityList, dataColumnIndex, idColumnIndex, sizeColumnIndex, cursor)
            }
            cursor.close()
            cursorFinder(finderEntityList)
            scanCallBack.scanCallBack(albumEntityList, finderEntityList)
        }
    }
}