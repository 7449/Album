package com.album.ui.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.album.*
import com.album.R
import com.album.core.*
import com.album.core.scan.AlbumEntity
import com.album.core.scan.AlbumScanImpl
import com.album.core.scan.AlbumSingleMediaScanner
import com.album.core.ui.AlbumBaseFragment
import com.album.core.view.AlbumView
import com.album.listener.AlbumCallback
import com.album.listener.OnAlbumItemClickListener
import com.album.simple.SimpleAlbumFragmentInterface
import com.album.simple.SimpleGridDivider
import com.album.ui.adapter.AlbumAdapter
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.album_fragment_album.*
import java.io.File

/**
 * by y on 14/08/2017.
 */

class AlbumFragment : AlbumBaseFragment(),
        AlbumView,
        SimpleAlbumFragmentInterface,
        OnAlbumItemClickListener,
        AlbumSingleMediaScanner.SingleScannerListener {

    companion object {
        @JvmStatic
        fun newInstance(albumBundle: AlbumBundle) = AlbumFragment().apply { arguments = Bundle().apply { putParcelable(AlbumConst.EXTRA_ALBUM_OPTIONS, albumBundle) } }
    }

    private var albumCallback: AlbumCallback? = null
    private var singleMediaScanner: AlbumSingleMediaScanner? = null
    private lateinit var albumBundle: AlbumBundle
    private lateinit var albumAdapter: AlbumAdapter
    private lateinit var albumScan: AlbumScanImpl
    lateinit var finderList: ArrayList<AlbumEntity>
    private lateinit var imagePath: Uri
    var parent: Long = AlbumScanConst.ALL_PARENT
    var finderName: String = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment is AlbumCallback) {
            albumCallback = parentFragment as AlbumCallback
        } else if (context is AlbumCallback) {
            albumCallback = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        albumBundle = bundle.getParcelable(AlbumConst.EXTRA_ALBUM_OPTIONS) ?: AlbumBundle()
        if (savedInstanceState == null) {
            imagePath = Uri.fromFile(mActivity.cameraFile(albumBundle.cameraPath, albumBundle.cameraName, albumBundle.cameraSuffix))
            return
        }
        parent = savedInstanceState.getLong(AlbumInternalConst.TYPE_STATE_PARENT, AlbumScanConst.ALL_PARENT)
        finderName = savedInstanceState.getString(AlbumInternalConst.TYPE_STATE_FINDER_NAME, "")
        imagePath = savedInstanceState.getParcelable(AlbumInternalConst.TYPE_STATE_IMAGE_PATH)
                ?: Uri.EMPTY
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(AlbumInternalConst.TYPE_STATE_SELECT, getSelectEntity())
        outState.putLong(AlbumInternalConst.TYPE_STATE_PARENT, parent)
        outState.putString(AlbumInternalConst.TYPE_STATE_FINDER_NAME, finderName)
        outState.putParcelable(AlbumInternalConst.TYPE_STATE_IMAGE_PATH, imagePath)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        albumRootView.setBackgroundColor(ContextCompat.getColor(mActivity, albumBundle.rootViewBackground))
        val drawable = ContextCompat.getDrawable(mActivity, albumBundle.photoEmptyDrawable)
        drawable?.setColorFilter(ContextCompat.getColor(mActivity, albumBundle.photoEmptyDrawableColor), PorterDuff.Mode.SRC_ATOP)
        albumEmpty.setImageDrawable(drawable)
        albumEmpty.setOnClickListener { v ->
            if (Album.instance.emptyClickListener?.invoke(v) == true) {
                startCamera()
            }
        }
        finderList = ArrayList()
        albumScan = AlbumScanImpl.newInstance(this)
        albumRecyclerView.setHasFixedSize(true)
        val gridLayoutManager = GridLayoutManager(mActivity, albumBundle.spanCount)
        albumRecyclerView.layoutManager = gridLayoutManager
        albumRecyclerView.addItemDecoration(SimpleGridDivider(albumBundle.dividerWidth))
        albumAdapter = AlbumAdapter(mActivity.square(albumBundle.spanCount), albumBundle, albumCallback, this)

        val selectList = savedInstanceState?.getParcelableArrayList<AlbumEntity>(AlbumInternalConst.TYPE_STATE_SELECT)

        if (!selectList.isNullOrEmpty()) {
            albumAdapter.multipleList = selectList
        } else if (!albumBundle.radio && !Album.instance.selectList.isNullOrEmpty()) {
            albumAdapter.multipleList = Album.instance.selectList ?: ArrayList()
        }

        albumRecyclerView.adapter = albumAdapter
        savedInstanceState?.let { albumCallback?.onAlbumScreenChanged(albumAdapter.multipleList.size) }
        onScanAlbum(parent, isFinder = false, result = false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_CANCELED -> when (requestCode) {
                AlbumConst.TYPE_PRE_REQUEST_CODE -> onResultPreview(data?.extras.orEmpty())
                UCrop.REQUEST_CROP -> Album.instance.albumListener?.onAlbumCropCanceled()
                AlbumCameraConst.OPEN_CAMERA_REQUEST_CODE -> {
                    val path = imagePath.path.orEmpty()
                    if (path.fileExists() && path.toFile()?.length() ?: 0 > 0) {
                        refreshMedia(AlbumConst.TYPE_RESULT_CAMERA, imagePath.path.orEmpty())
                        Album.instance.albumListener?.onAlbumCameraSuccessCanceled()
                    } else {
                        path.toFile()?.delete()
                        Album.instance.albumListener?.onAlbumCameraCanceled()
                    }
                }
            }
            UCrop.RESULT_ERROR -> {
                Album.instance.albumListener?.onAlbumUCropError(UCrop.getError(data.orEmpty()))
                if (albumBundle.cropErrorFinish) {
                    mActivity.finish()
                }
            }
            Activity.RESULT_OK -> when (requestCode) {
                AlbumCameraConst.CUSTOM_CAMERA_REQUEST_CODE -> {
                    data?.extras?.let {
                        val customizePath = it.getString(AlbumCameraConst.CUSTOM_CAMERA_RESULT_PATH)
                        if (!TextUtils.isEmpty(customizePath)) {
                            imagePath = Uri.fromFile(File(customizePath))
                            refreshMedia(AlbumConst.TYPE_RESULT_CAMERA, imagePath.path.orEmpty())
                            if (albumBundle.cameraCrop) {
                                openUCrop(imagePath.path.orEmpty())
                            }
                        }
                    }
                }
                AlbumCameraConst.OPEN_CAMERA_REQUEST_CODE -> {
                    refreshMedia(AlbumConst.TYPE_RESULT_CAMERA, imagePath.path.orEmpty())
                    if (albumBundle.cameraCrop) {
                        openUCrop(imagePath.path.orEmpty())
                    }
                }
                UCrop.REQUEST_CROP -> {
                    if (data == null) {
                        Album.instance.albumListener?.onAlbumUCropError(null)
                        return
                    }
                    val path = data.extras?.getParcelable<Uri>(UCrop.EXTRA_OUTPUT_URI)?.path.orEmpty()
                    path.toFile()?.let { Album.instance.albumListener?.onAlbumUCropResources(it) }
                    refreshMedia(AlbumConst.TYPE_RESULT_CROP, path)
                    if (albumBundle.cropFinish) {
                        mActivity.finish()
                    }
                }
                AlbumConst.TYPE_PRE_REQUEST_CODE -> onResultPreview(data?.extras.orEmpty())
            }
        }
    }

    override fun scanSuccess(arrayList: ArrayList<AlbumEntity>) {
        if (arrayList.isEmpty()) {
            Album.instance.albumListener?.onAlbumEmpty()
            return
        }
        if (parent == AlbumScanConst.ALL_PARENT && !albumBundle.hideCamera) {
            arrayList.add(0, AlbumEntity(path = AlbumInternalConst.CAMERA))
        }
        albumAdapter.addAll(arrayList)
    }

    override fun scanFinderSuccess(finderList: ArrayList<AlbumEntity>) {
        finderList.find { it.bucketDisplayName == "0" }?.bucketDisplayName = getString(albumBundle.sdName)
        finderList.find { it.parent == AlbumScanConst.ALL_PARENT }?.bucketDisplayName = getString(albumBundle.allName)
        this.finderList.clear()
        this.finderList.addAll(finderList)
    }

    override fun resultSuccess(albumEntity: AlbumEntity?) {
        if (albumEntity == null) {
            Album.instance.albumListener?.onAlbumResultCameraError()
        } else {
            albumAdapter.albumList.add(1, albumEntity)
            albumAdapter.notifyDataSetChanged()
            albumScan.refreshResultFinder(finderList, albumEntity)
        }
    }

    override fun onCameraItemClick(view: View, position: Int, albumEntity: AlbumEntity) {
        if (permissionCamera()) {
            startCamera()
        }
    }

    override fun onPhotoItemClick(view: View, position: Int, albumEntity: AlbumEntity) {
        if (!albumEntity.path.fileExists()) {
            Album.instance.albumListener?.onAlbumFileNotExist()
            return
        }
        if (albumBundle.scanType == AlbumScanConst.VIDEO) {
            try {
                val openVideo = Intent(Intent.ACTION_VIEW)
                openVideo.setDataAndType(Uri.parse(albumEntity.path), "video/*")
                startActivity(openVideo)
            } catch (e: Exception) {
                Album.instance.albumListener?.onAlbumVideoPlayError()
            }
            return
        }
        if (albumBundle.radio) {
            if (albumBundle.crop) {
                openUCrop(albumEntity.path)
            } else {
                val list = ArrayList<AlbumEntity>()
                list.add(albumEntity)
                Album.instance.albumListener?.onAlbumResources(list)
                if (albumBundle.selectImageFinish) {
                    mActivity.finish()
                }
            }
            return
        }
        if (albumBundle.noPreview) {
            return
        }
        albumCallback?.onAlbumItemClick(getSelectEntity(), position, parent)
    }

    /**
     * 扫描图库
     * [parent] 文件夹 parent
     * [isFinder] 是否是点击相册名称扫描
     * [result] 是否是拍照之后扫描
     */
    override fun onScanAlbum(parent: Long, isFinder: Boolean, result: Boolean) {
        if (isFinder) {
            albumAdapter.removeAll()
        }
        this.parent = parent
        if (!permissionStorage()) {
            return
        }
        // 如果 albumList 为空则是没有图片拍照的第一张图片,这时直接扫描整个图库即可
        if (result && albumAdapter.albumList.isNotEmpty()) {
            albumScan.scanResult(imagePath.path.orEmpty())
            return
        }
        albumScan.scanAll(parent)
    }

    override fun onScanCropAlbum(path: String) {
        albumScan.scanResult(path)
    }

    override fun onScanStart() {}

    override fun onScanCompleted(type: Int, path: String) {
        mActivity.runOnUiThread {
            if (type == AlbumConst.TYPE_RESULT_CROP) {
                onScanCropAlbum(path)
            } else {
                onScanAlbum(parent, isFinder = false, result = true)
            }
        }
    }

    override fun disconnectMediaScanner() {
        singleMediaScanner?.disconnect()
    }

    /**
     * 启动相机
     * [imagePath]每次都赋值,确保拍照时不会重复
     */
    override fun startCamera() {
        val albumCameraListener = Album.instance.customCameraListener
        if (albumCameraListener != null) {
            albumCameraListener.invoke(this)
            return
        }
        imagePath = Uri.fromFile(mActivity.cameraFile(albumBundle.cameraPath, albumBundle.cameraName, albumBundle.cameraSuffix))
        val i = openCamera(imagePath, albumBundle.scanType == AlbumScanConst.VIDEO)
        if (i == AlbumCameraConst.OPEN_CAMERA_ERROR) {
            Album.instance.albumListener?.onAlbumOpenCameraError()
        }
    }

    override fun refreshMedia(type: Int, path: String) {
        disconnectMediaScanner()
        singleMediaScanner = AlbumSingleMediaScanner.newInstance(mActivity, path, type, this@AlbumFragment)
    }

    override fun getSelectEntity(): ArrayList<AlbumEntity> = albumAdapter.multipleList

    /**
     * 预览时多选的数据,可能为空,[multipleSelect]结果类似
     */
    override fun selectPreview(): ArrayList<AlbumEntity> {
        if (getSelectEntity().isEmpty()) {
            Album.instance.albumListener?.onAlbumPreviewEmpty()
            return ArrayList()
        }
        return getSelectEntity()
    }

    override fun allPreview(): ArrayList<AlbumEntity> = albumAdapter.albumList

    /**
     * 获取多选时的数据,可能为空
     */
    override fun multipleSelect() {
        if (getSelectEntity().isEmpty()) {
            Album.instance.albumListener?.onAlbumSelectEmpty()
            return
        }
        Album.instance.albumListener?.onAlbumResources(getSelectEntity())
        if (albumBundle.selectImageFinish) {
            mActivity.finish()
        }
    }

    override fun openUCrop(path: String) {
        UCrop.of(Uri.fromFile(File(path)), Uri.fromFile(mActivity.cameraFile(albumBundle.uCropPath, albumBundle.cameraName, albumBundle.cameraSuffix)))
                .withOptions(Album.instance.options ?: UCrop.Options())
                .start(mActivity, this)
    }

    override fun onResultPreview(bundle: Bundle) {
        val previewAlbumEntity = bundle.getParcelableArrayList<AlbumEntity>(AlbumConst.TYPE_PRE_SELECT)
        val isRefreshUI = bundle.getBoolean(AlbumInternalConst.TYPE_PRE_REFRESH_UI, true)
        val isFinish = bundle.getBoolean(AlbumInternalConst.TYPE_PRE_DONE_FINISH, false)
        if (isFinish) {
            mActivity.finish()
            return
        }
        if (!isRefreshUI || previewAlbumEntity == null || getSelectEntity() == previewAlbumEntity) {
            return
        }
        albumAdapter.albumList.mergeEntity(previewAlbumEntity)
        albumAdapter.multipleList = previewAlbumEntity
        albumCallback?.onPrevChangedCount(getSelectEntity().size)
    }

    override fun onDialogResultPreview(bundle: Bundle) {
        val previewAlbumEntity = bundle.getParcelableArrayList<AlbumEntity>(AlbumConst.TYPE_PRE_SELECT)
        val isRefreshUI = bundle.getBoolean(AlbumInternalConst.TYPE_PRE_REFRESH_UI, true)
        if (!isRefreshUI || previewAlbumEntity == null) {
            return
        }
        albumAdapter.albumList.mergeEntity(previewAlbumEntity)
        albumAdapter.multipleList = previewAlbumEntity
        albumCallback?.onPrevChangedCount(getSelectEntity().size)
    }

    override fun permissionsGranted(type: Int) {
        when (type) {
            AlbumPermissionConst.TYPE_PERMISSIONS_ALBUM -> onScanAlbum(parent, isFinder = false, result = false)
            AlbumPermissionConst.TYPE_PERMISSIONS_CAMERA -> startCamera()
        }
    }

    override fun permissionsDenied(type: Int) {
        Album.instance.albumListener?.onAlbumPermissionsDenied(type)
        if (albumBundle.permissionsDeniedFinish) {
            mActivity.finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disconnectMediaScanner()
    }

    override fun refreshUI() {
        albumAdapter.notifyDataSetChanged()
    }

    override fun currentScanType(): Int = albumBundle.scanType

    override fun getAlbumContext(): FragmentActivity = mActivity

    override val layoutId: Int = R.layout.album_fragment_album
}

