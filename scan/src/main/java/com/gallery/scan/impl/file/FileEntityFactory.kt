package com.gallery.scan.impl.file

import android.provider.MediaStore
import com.gallery.scan.args.MediaScanEntityFactory
import com.gallery.scan.extensions.intOrDefault
import com.gallery.scan.extensions.longOrDefault
import com.gallery.scan.extensions.stringOrDefault

internal fun MediaScanEntityFactory.Companion.file(): MediaScanEntityFactory {
    return action {
        FileScanEntity(
            it.longOrDefault(MediaStore.Files.FileColumns._ID),

            it.longOrDefault(MediaStore.Files.FileColumns.SIZE),
            it.stringOrDefault(MediaStore.Files.FileColumns.DISPLAY_NAME),
            it.stringOrDefault(MediaStore.Files.FileColumns.TITLE),
            it.longOrDefault(MediaStore.Files.FileColumns.DATE_ADDED),
            it.longOrDefault(MediaStore.Files.FileColumns.DATE_MODIFIED),
            it.stringOrDefault(MediaStore.Files.FileColumns.MIME_TYPE),
            it.intOrDefault(MediaStore.Files.FileColumns.WIDTH),
            it.intOrDefault(MediaStore.Files.FileColumns.HEIGHT),

            it.longOrDefault(MediaStore.Files.FileColumns.PARENT),
            it.stringOrDefault(MediaStore.Files.FileColumns.MEDIA_TYPE),

            it.intOrDefault(MediaStore.Images.ImageColumns.ORIENTATION),
            it.stringOrDefault(MediaStore.Images.ImageColumns.BUCKET_ID),
            it.stringOrDefault(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME),

            it.longOrDefault(MediaStore.Video.VideoColumns.DURATION)
        )
    }
}