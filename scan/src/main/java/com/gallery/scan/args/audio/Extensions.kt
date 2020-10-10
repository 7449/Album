package com.gallery.scan.args.audio

import android.database.Cursor
import android.os.Parcelable
import android.provider.MediaStore
import androidx.kotlin.expand.database.getLongOrDefault
import androidx.kotlin.expand.database.getStringOrDefault
import com.gallery.scan.args.ScanEntityFactory

/** 音频实体生成 */
fun ScanEntityFactory.Companion.audioExpand(): ScanEntityFactory {
    return object : ScanEntityFactory {
        override fun cursorMoveToNext(cursor: Cursor): Parcelable {
            return ScanAudioEntity(
                    cursor.getLongOrDefault(MediaStore.Audio.Media._ID),
                    cursor.getLongOrDefault(MediaStore.Audio.Media.SIZE),
                    cursor.getStringOrDefault(MediaStore.Audio.Media.DISPLAY_NAME),
                    cursor.getStringOrDefault(MediaStore.Audio.Media.TITLE),
                    cursor.getLongOrDefault(MediaStore.Audio.Media.DATE_ADDED),
                    cursor.getLongOrDefault(MediaStore.Audio.Media.DATE_MODIFIED),
                    cursor.getStringOrDefault(MediaStore.Audio.Media.MIME_TYPE),
            )
        }
    }
}