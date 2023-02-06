package develop.file.gallery.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContract
import androidx.fragment.app.Fragment

enum class CameraStatus {
    SUCCESS,
    ERROR,
    PERMISSION
}

internal fun Fragment.checkCameraStatus(
    uri: CameraUri,
    action: (uri: CameraUri) -> Unit
): CameraStatus {
    val intent =
        if (uri.type.contains(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE))
            Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        else
            Intent(MediaStore.ACTION_VIDEO_CAPTURE)
    return intent.resolveActivity(requireActivity().packageManager)?.let {
        action.invoke(uri)
        CameraStatus.SUCCESS
    } ?: CameraStatus.ERROR
}

internal class CameraUri(val type: List<Int>, val uri: Uri)

internal class CameraResultContract : ActivityResultContract<CameraUri, Int>() {
    override fun createIntent(context: Context, input: CameraUri): Intent =
        Intent(
            if (input.type.contains(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE))
                Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            else Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        ).putExtra(MediaStore.EXTRA_OUTPUT, input.uri)

    override fun parseResult(resultCode: Int, intent: Intent?): Int = resultCode
}