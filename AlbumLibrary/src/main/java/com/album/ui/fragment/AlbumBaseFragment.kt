package com.album.ui.fragment

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.album.Album
import com.album.AlbumConstant
import com.album.PermissionsType
import com.album.util.AlbumTask
import com.album.util.PermissionUtils

/**
 * @author y
 */
abstract class AlbumBaseFragment : Fragment() {

    lateinit var bundle: Bundle
    var albumConfig = Album.instance.config
    lateinit var mActivity: Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bundle = arguments ?: Bundle()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mActivity = context as? Activity ?: activity!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(getLayoutId(), container, false)
        initView(view)
        initCreate(savedInstanceState)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initActivityCreated(savedInstanceState)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PermissionUtils.WRITE_EXTERNAL_STORAGE_REQUEST_CODE -> {
                if (grantResults.isEmpty()) {
                    return
                }
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    permissionsDenied(AlbumConstant.TYPE_PERMISSIONS_ALBUM)
                } else {
                    permissionsGranted(AlbumConstant.TYPE_PERMISSIONS_ALBUM)
                }
            }
            PermissionUtils.CAMERA_REQUEST_CODE -> {
                if (grantResults.isEmpty()) {
                    return
                }
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    permissionsDenied(AlbumConstant.TYPE_PERMISSIONS_CAMERA)
                } else {
                    permissionsGranted(AlbumConstant.TYPE_PERMISSIONS_CAMERA)
                }
            }
        }
    }

    protected abstract fun initCreate(savedInstanceState: Bundle?)

    protected abstract fun initView(view: View)

    protected abstract fun initActivityCreated(savedInstanceState: Bundle?)

    protected abstract fun permissionsGranted(@PermissionsType type: Int)

    protected abstract fun permissionsDenied(@PermissionsType type: Int)

    protected abstract fun getLayoutId(): Int

    override fun onDestroyView() {
        super.onDestroyView()
        AlbumTask.instance.quit()
    }
}
