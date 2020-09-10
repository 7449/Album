package com.gallery.core.ui.widget

import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContract
import com.gallery.core.expand.CameraUri

class CameraResultContract : ActivityResultContract<CameraUri, Int>() {
    override fun createIntent(context: Context, input: CameraUri): Intent =
            Intent(if (input.type.contains(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE))
                Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            else Intent(MediaStore.ACTION_VIDEO_CAPTURE))
                    .putExtra(MediaStore.EXTRA_OUTPUT, input.uri)

    override fun parseResult(resultCode: Int, intent: Intent?): Int = resultCode
}