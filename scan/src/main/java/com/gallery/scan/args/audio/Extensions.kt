package com.gallery.scan.args.audio

import android.database.Cursor
import android.provider.MediaStore
import androidx.kotlin.expand.database.getIntOrDefault
import androidx.kotlin.expand.database.getLongOrDefault
import androidx.kotlin.expand.database.getStringOrDefault
import com.gallery.scan.args.ScanEntityFactory

/** 音频实体生成 */
fun ScanEntityFactory.Companion.scanAudioFactory(): ScanEntityFactory {
    return object : ScanEntityFactory {
        override fun onCreateCursor(cursor: Cursor): ScanEntityFactory {
            return ScanAudioEntity(
                    cursor.getLongOrDefault(MediaStore.Files.FileColumns._ID),

                    cursor.getLongOrDefault(MediaStore.Files.FileColumns.SIZE),
                    cursor.getStringOrDefault(MediaStore.Files.FileColumns.DISPLAY_NAME),
                    cursor.getStringOrDefault(MediaStore.Files.FileColumns.TITLE),
                    cursor.getLongOrDefault(MediaStore.Files.FileColumns.DATE_ADDED),
                    cursor.getLongOrDefault(MediaStore.Files.FileColumns.DATE_MODIFIED),
                    cursor.getStringOrDefault(MediaStore.Files.FileColumns.MIME_TYPE),
                    cursor.getIntOrDefault(MediaStore.Files.FileColumns.WIDTH),
                    cursor.getIntOrDefault(MediaStore.Files.FileColumns.HEIGHT),

                    0,
                    false)
        }
    }
}