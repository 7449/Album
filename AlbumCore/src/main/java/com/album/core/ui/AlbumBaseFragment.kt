package com.album.core.ui

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.album.core.AlbumCore.orEmpty
import com.album.core.AlbumPermission.CAMERA_REQUEST_CODE
import com.album.core.AlbumPermission.TYPE_PERMISSIONS_ALBUM
import com.album.core.AlbumPermission.TYPE_PERMISSIONS_CAMERA
import com.album.core.AlbumPermission.WRITE_EXTERNAL_STORAGE_REQUEST_CODE

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

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mActivity = context as? FragmentActivity ?: activity!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(layoutId, container, false)

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            WRITE_EXTERNAL_STORAGE_REQUEST_CODE -> {
                if (grantResults.isEmpty()) {
                    return
                }
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    permissionsDenied(TYPE_PERMISSIONS_ALBUM)
                } else {
                    permissionsGranted(TYPE_PERMISSIONS_ALBUM)
                }
            }
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isEmpty()) {
                    return
                }
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    permissionsDenied(TYPE_PERMISSIONS_CAMERA)
                } else {
                    permissionsGranted(TYPE_PERMISSIONS_CAMERA)
                }
            }
        }
    }

    protected abstract fun permissionsGranted(type: Int)

    protected abstract fun permissionsDenied(type: Int)

    protected abstract val layoutId: Int
}

