package com.gallery.scan.extensions

import android.provider.MediaStore
import androidx.kotlin.expand.database.getIntOrDefault
import androidx.kotlin.expand.database.getLongOrDefault
import androidx.kotlin.expand.database.getStringOrDefault
import com.gallery.scan.args.ScanEntityFactory

/** 音频实体生成 [AudioColumns.columns] */
fun ScanEntityFactory.Companion.audioExpand(): ScanEntityFactory {
    return action {
        ScanAudioEntity(
                it.getLongOrDefault(MediaStore.Audio.Media._ID),
                it.getLongOrDefault(MediaStore.Audio.Media.SIZE),
                it.getStringOrDefault(MediaStore.Audio.Media.DISPLAY_NAME),
                it.getStringOrDefault(MediaStore.Audio.Media.TITLE),
                it.getLongOrDefault(MediaStore.Audio.Media.DATE_ADDED),
                it.getLongOrDefault(MediaStore.Audio.Media.DATE_MODIFIED),
                it.getStringOrDefault(MediaStore.Audio.Media.MIME_TYPE),
        )
    }
}

/** 图片实体生成 [PictureColumns.columns] */
fun ScanEntityFactory.Companion.pictureExpand(): ScanEntityFactory {
    return action {
        ScanPictureEntity(
                it.getLongOrDefault(MediaStore.Images.Media._ID),
                it.getLongOrDefault(MediaStore.Images.Media.SIZE),
                it.getStringOrDefault(MediaStore.Images.Media.DISPLAY_NAME),
                it.getStringOrDefault(MediaStore.Images.Media.TITLE),
                it.getLongOrDefault(MediaStore.Images.Media.DATE_ADDED),
                it.getLongOrDefault(MediaStore.Images.Media.DATE_MODIFIED),
                it.getStringOrDefault(MediaStore.Images.Media.MIME_TYPE),
                it.getIntOrDefault(MediaStore.Images.Media.WIDTH),
                it.getIntOrDefault(MediaStore.Images.Media.HEIGHT),
                it.getIntOrDefault(MediaStore.Images.Media.ORIENTATION),
                it.getStringOrDefault(MediaStore.Images.Media.BUCKET_ID),
                it.getStringOrDefault(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        )
    }
}

/** 文件实体生成 [FileColumns.columns] */
fun ScanEntityFactory.Companion.fileExpand(): ScanEntityFactory {
    return action {
        ScanFileEntity(
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