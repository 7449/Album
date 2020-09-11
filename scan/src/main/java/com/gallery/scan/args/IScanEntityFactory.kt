package com.gallery.scan.args

import android.database.Cursor
import android.provider.MediaStore
import androidx.kotlin.expand.database.getIntOrDefault
import androidx.kotlin.expand.database.getLongOrDefault
import androidx.kotlin.expand.database.getStringOrDefault

/**
 * 自定义参数类
 * 调用[onCreateCursor]可避免泛型强制转换
 */
interface IScanEntityFactory {

    companion object {

        fun api19Factory(): IScanEntityFactory {
            return object : IScanEntityFactory {
                override fun onCreateCursor(cursor: Cursor): IScanEntityFactory {
                    return ScanMinimumEntity(
                            cursor.getLongOrDefault(MediaStore.Files.FileColumns._ID),

                            cursor.getLongOrDefault(MediaStore.Files.FileColumns.SIZE),
                            cursor.getStringOrDefault(MediaStore.Files.FileColumns.DISPLAY_NAME),
                            cursor.getStringOrDefault(MediaStore.Files.FileColumns.TITLE),
                            cursor.getLongOrDefault(MediaStore.Files.FileColumns.DATE_ADDED),
                            cursor.getLongOrDefault(MediaStore.Files.FileColumns.DATE_MODIFIED),
                            cursor.getStringOrDefault(MediaStore.Files.FileColumns.MIME_TYPE),
                            cursor.getIntOrDefault(MediaStore.Files.FileColumns.WIDTH),
                            cursor.getIntOrDefault(MediaStore.Files.FileColumns.HEIGHT),

                            cursor.getLongOrDefault(MediaStore.Files.FileColumns.PARENT),
                            cursor.getStringOrDefault(MediaStore.Files.FileColumns.MEDIA_TYPE),

                            cursor.getIntOrDefault(MediaStore.Images.ImageColumns.ORIENTATION),
                            cursor.getStringOrDefault(MediaStore.Images.ImageColumns.BUCKET_ID),
                            cursor.getStringOrDefault(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME),

                            cursor.getLongOrDefault(MediaStore.Video.VideoColumns.DURATION),

                            0,
                            false)
                }
            }
        }

    }

    fun onCreateCursor(cursor: Cursor): IScanEntityFactory

    @Suppress("UNCHECKED_CAST")
    fun <ENTITY : IScanEntityFactory> onCreateCursorGeneric(cursor: Cursor): ENTITY = onCreateCursor(cursor) as ENTITY
}