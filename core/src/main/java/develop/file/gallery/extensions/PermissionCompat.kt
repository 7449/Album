package develop.file.gallery.extensions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

internal object PermissionCompat {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    internal fun Fragment.checkPermissionAndRequestMediaImages(launcher: ActivityResultLauncher<String>): Boolean {
        return if (!checkMediaImagesPermission()) {
            launcher.launch(Manifest.permission.READ_MEDIA_IMAGES)
            false
        } else {
            true
        }
    }

    internal fun Fragment.checkPermissionAndRequestRead(launcher: ActivityResultLauncher<String>): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return checkPermissionAndRequestMediaImages(launcher)
        }
        return if (!checkReadPermission()) {
            launcher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            false
        } else {
            true
        }
    }

    internal fun Fragment.checkPermissionAndRequestCamera(launcher: ActivityResultLauncher<String>): Boolean {
        return if (!checkCameraPermission()) {
            launcher.launch(Manifest.permission.CAMERA)
            false
        } else {
            true
        }
    }

    private fun Fragment.checkCameraPermission() =
        requireContext().checkSelfPermissions(Manifest.permission.CAMERA)

    private fun Fragment.checkReadPermission() =
        requireContext().checkSelfPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun Fragment.checkMediaImagesPermission() =
        requireContext().checkSelfPermissions(Manifest.permission.READ_MEDIA_IMAGES)

    private fun Context.checkSelfPermissions(name: String) =
        ContextCompat.checkSelfPermission(this, name) == PackageManager.PERMISSION_GRANTED

}