package com.gallery.scan.impl.file

import android.net.Uri
import android.provider.MediaStore

internal object FileColumns {

    internal val uri: Uri = MediaStore.Files.getContentUri("external")

    internal val columns: Array<String> = arrayOf(

        /**
         * base columns
         */
        MediaStore.Files.FileColumns._ID,
        MediaStore.Files.FileColumns.SIZE,
        MediaStore.Files.FileColumns.DISPLAY_NAME,
        MediaStore.Files.FileColumns.TITLE,
        MediaStore.Files.FileColumns.DATE_ADDED,
        MediaStore.Files.FileColumns.DATE_MODIFIED,
        MediaStore.Files.FileColumns.MIME_TYPE,
        MediaStore.Files.FileColumns.WIDTH,
        MediaStore.Files.FileColumns.HEIGHT,

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

    )

}