package com.gallery.scan.args.picture

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

/** 获取可使用的多个图片扫描Bundle */
fun Long.multipleFileExpand(): Bundle {
    return Bundle().apply { putLong(MediaStore.Files.FileColumns.PARENT, this@multipleFileExpand) }
}

/** 获取可使用的单个图片扫描Bundle */
fun Long.singleFileExpand(): Bundle {
    return Bundle().apply { putLong(BaseColumns._ID, this@singleFileExpand) }
}

/** 获取可使用的uri */
val ScanPictureEntity.externalUriExpand: Uri
    get() = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

/** 图片是否是动态图 */
val ScanPictureEntity.isGifExpand: Boolean
    get() = mimeType.contains("gif")

/** 图片实体生成 */
fun ScanEntityFactory.Companion.pictureExpand(): ScanEntityFactory {
    return object : ScanEntityFactory {
        override fun cursorMoveToNext(cursor: Cursor): ScanEntityFactory {
            return ScanPictureEntity(
                    cursor.getLongOrDefault(MediaStore.Images.Media._ID),
                    cursor.getLongOrDefault(MediaStore.Images.Media.SIZE),
                    cursor.getStringOrDefault(MediaStore.Images.Media.DISPLAY_NAME),
                    cursor.getStringOrDefault(MediaStore.Images.Media.TITLE),
                    cursor.getLongOrDefault(MediaStore.Images.Media.DATE_ADDED),
                    cursor.getLongOrDefault(MediaStore.Images.Media.DATE_MODIFIED),
                    cursor.getStringOrDefault(MediaStore.Images.Media.MIME_TYPE),
                    cursor.getIntOrDefault(MediaStore.Images.Media.WIDTH),
                    cursor.getIntOrDefault(MediaStore.Images.Media.HEIGHT),
                    cursor.getIntOrDefault(MediaStore.Images.Media.ORIENTATION),
                    cursor.getStringOrDefault(MediaStore.Images.Media.BUCKET_ID),
                    cursor.getStringOrDefault(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
            )
        }
    }
}