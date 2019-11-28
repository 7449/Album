package com.gallery.core.ui.base

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.gallery.core.AlbumPermissionConst
import com.gallery.core.ext.orEmpty

/**
 * @author y
 */
abstract class AlbumBaseFragment : Fragment() {

    lateinit var bundle: Bundle
    lateinit var mActivity: FragmentActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bundle = arguments.orEmpty()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as FragmentActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(layoutId, container, false)

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            AlbumPermissionConst.WRITE_REQUEST_CODE -> {
                if (grantResults.isEmpty()) {
                    return
                }
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    permissionsDenied(AlbumPermissionConst.ALBUM)
                } else {
                    permissionsGranted(AlbumPermissionConst.ALBUM)
                }
            }
            AlbumPermissionConst.CAMERA_REQUEST_CODE -> {
                if (grantResults.isEmpty()) {
                    return
                }
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    permissionsDenied(AlbumPermissionConst.CAMERA)
                } else {
                    permissionsGranted(AlbumPermissionConst.CAMERA)
                }
            }
        }
    }

    protected abstract fun permissionsGranted(type: Int)

    protected abstract fun permissionsDenied(type: Int)

    protected abstract val layoutId: Int
}

