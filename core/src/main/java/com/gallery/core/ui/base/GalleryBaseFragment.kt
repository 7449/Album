package com.gallery.core.ui.base

import android.content.pm.PackageManager
import androidx.fragment.app.Fragment
import androidx.kotlin.expand.PermissionCode
import androidx.kotlin.expand.orEmptyExpand

/**
 * @author y
 */
abstract class GalleryBaseFragment(layoutId: Int) : Fragment(layoutId) {

    val bundle by lazy { arguments.orEmptyExpand() }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PermissionCode.WRITE.code -> {
                if (grantResults.isEmpty()) {
                    return
                }
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    permissionsDenied(PermissionCode.WRITE)
                } else {
                    permissionsGranted(PermissionCode.WRITE)
                }
            }
            PermissionCode.READ.code -> {
                if (grantResults.isEmpty()) {
                    return
                }
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    permissionsDenied(PermissionCode.READ)
                } else {
                    permissionsGranted(PermissionCode.READ)
                }
            }
        }
    }

    protected abstract fun permissionsGranted(type: PermissionCode)

    protected abstract fun permissionsDenied(type: PermissionCode)
}

