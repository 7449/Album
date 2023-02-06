package develop.file.gallery.extensions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

internal object PermissionCompat {

    internal fun Fragment.checkPermissionAndRequestWrite(launcher: ActivityResultLauncher<String>): Boolean {
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

    private fun Context.checkSelfPermissions(name: String) =
        ContextCompat.checkSelfPermission(this, name) == PackageManager.PERMISSION_GRANTED

}