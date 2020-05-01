package com.gallery.core.ext

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContract
import com.gallery.scan.ScanType

class CameraUri(val type: ScanType, val uri: Uri)

class CameraResultContract : ActivityResultContract<CameraUri, Int>() {
    override fun createIntent(context: Context, input: CameraUri): Intent =
            Intent(if (input.type == ScanType.VIDEO) MediaStore.ACTION_VIDEO_CAPTURE else MediaStore.ACTION_IMAGE_CAPTURE)
                    .putExtra(MediaStore.EXTRA_OUTPUT, input.uri)

    override fun parseResult(resultCode: Int, intent: Intent?): Int = resultCode
}