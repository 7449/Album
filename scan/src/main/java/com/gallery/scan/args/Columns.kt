package com.gallery.scan.args

import android.net.Uri
import android.provider.BaseColumns
import android.provider.MediaStore

object Columns {

    val fileUri: Uri = MediaStore.Files.getContentUri("external")

    val minimumColumns = arrayOf(
            BaseColumns._ID,

            /**
             * base columns
             */
            MediaStore.MediaColumns.SIZE,
            MediaStore.MediaColumns.DISPLAY_NAME,
            MediaStore.MediaColumns.TITLE,
            MediaStore.MediaColumns.DATE_ADDED,
            MediaStore.MediaColumns.DATE_MODIFIED,
            MediaStore.MediaColumns.MIME_TYPE,
            MediaStore.MediaColumns.WIDTH,
            MediaStore.MediaColumns.HEIGHT,

            /**
             * file columns
             */
            MediaStore.Files.FileColumns.PARENT,
            MediaStore.Files.FileColumns.MEDIA_TYPE,

            /**
             * image columns
             */
            MediaStore.Images.Media.ORIENTATION,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,

            /**
             * video columns
             */
            MediaStore.Video.Media.DURATION

            /**
             * audio columns
             * DO NOTHING
             */
    )
}