package com.gallery.scan.args.file

import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.BaseColumns
import android.provider.MediaStore
import androidx.kotlin.expand.database.getIntOrDefault
import androidx.kotlin.expand.database.getLongOrDefault
import androidx.kotlin.expand.database.getStringOrDefault
import com.gallery.scan.args.ScanEntityFactory

/** 获取可使用的多个文件扫描Bundle */
fun Long.multipleFileExpand(): Bundle {
    return Bundle().apply { putLong(MediaStore.Files.FileColumns.PARENT, this@multipleFileExpand) }
}

/** 获取可使用的单个文件扫描Bundle */
fun Long.singleFileExpand(): Bundle {
    return Bundle().apply { putLong(BaseColumns._ID, this@singleFileExpand) }
}

/** 获取可使用的uri */
val ScanFileEntity.externalUriExpand: Uri
    get() = when (mediaType) {
        MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString() -> ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
        MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString() -> ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)
        MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO.toString() -> ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)
        else -> Uri.EMPTY
    }

/** 文件是否是动态图 */
val ScanFileEntity.isGifExpand: Boolean
    get() = mimeType.contains("gif")

/** 文件是否是视频 */
val ScanFileEntity.isVideoExpand: Boolean
    get() = mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()

/** 文件是否是图片 */
val ScanFileEntity.isImageExpand: Boolean
    get() = mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString()

/** 文件是否是音频 */
val ScanFileEntity.isAudioExpand: Boolean
    get() = mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()

/** 文件实体生成 */
fun ScanEntityFactory.Companion.scanFileFactory(): ScanEntityFactory {
    return object : ScanEntityFactory {
        override fun onCreateCursor(cursor: Cursor): ScanEntityFactory {
            return ScanFileEntity(
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