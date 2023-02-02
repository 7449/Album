package com.gallery.core.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContract

enum class CameraStatus {
    SUCCESS,
    ERROR,
    PERMISSION
}

internal class CameraUri(val type: Array<String>, val uri: Uri)

internal class CameraResultContract : ActivityResultContract<CameraUri, Int>() {
    override fun createIntent(context: Context, input: CameraUri): Intent =
        Intent(
            if (input.type.contains(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString()))
                Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            else Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        ).putExtra(MediaStore.EXTRA_OUTPUT, input.uri)

    override fun parseResult(resultCode: Int, intent: Intent?): Int = resultCode
}