package com.gallery.scan.impl.file

import android.provider.MediaStore
import com.gallery.scan.args.ScanEntityFactory
import com.gallery.scan.extensions.getIntOrDefault
import com.gallery.scan.extensions.getLongOrDefault
import com.gallery.scan.extensions.getStringOrDefault

/** 文件实体生成 [FileColumns.columns] */
fun ScanEntityFactory.Companion.file(): ScanEntityFactory {
    return action {
        FileScanEntity(
                it.getLongOrDefault(MediaStore.Files.FileColumns._ID),

                it.getLongOrDefault(MediaStore.Files.FileColumns.SIZE),
                it.getStringOrDefault(MediaStore.Files.FileColumns.DISPLAY_NAME),
                it.getStringOrDefault(MediaStore.Files.FileColumns.TITLE),
                it.getLongOrDefault(MediaStore.Files.FileColumns.DATE_ADDED),
                it.getLongOrDefault(MediaStore.Files.FileColumns.DATE_MODIFIED),
                it.getStringOrDefault(MediaStore.Files.FileColumns.MIME_TYPE),
                it.getIntOrDefault(MediaStore.Files.FileColumns.WIDTH),
                it.getIntOrDefault(MediaStore.Files.FileColumns.HEIGHT),

                it.getLongOrDefault(MediaStore.Files.FileColumns.PARENT),
                it.getStringOrDefault(MediaStore.Files.FileColumns.MEDIA_TYPE),

                it.getIntOrDefault(MediaStore.Images.ImageColumns.ORIENTATION),
                it.getStringOrDefault(MediaStore.Images.ImageColumns.BUCKET_ID),
                it.getStringOrDefault(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME),

                it.getLongOrDefault(MediaStore.Video.VideoColumns.DURATION)
        )
    }
}