package com.gallery.scan.args.audio

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
            MediaStore.Audio.Media.MIME_TYPE,
            MediaStore.Audio.Media.WIDTH,
            MediaStore.Audio.Media.HEIGHT
    )
}