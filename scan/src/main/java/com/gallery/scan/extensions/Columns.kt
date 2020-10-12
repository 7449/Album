package com.gallery.scan.extensions

import android.net.Uri
import android.provider.MediaStore

internal object AudioColumns {

    internal val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

    internal val columns: Array<String> = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.SIZE,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATE_ADDED,
            MediaStore.Audio.Media.DATE_MODIFIED,
            MediaStore.Audio.Media.MIME_TYPE
    )
}

internal object PictureColumns {

    internal val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

    internal val columns: Array<String> = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.TITLE,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.DATE_MODIFIED,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.WIDTH,
            MediaStore.Images.Media.HEIGHT,
            MediaStore.Images.Media.ORIENTATION,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME
    )
}

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

            /**
             * audio columns
             * DO NOTHING
             */
    )
}