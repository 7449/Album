package com.album.core.ui.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.album.core.*
import com.album.core.R
import com.album.core.action.AlbumAction
import com.album.core.ui.adapter.AlbumAdapter
import com.album.core.ui.widget.SimpleGridDivider
import com.album.scan.*
import com.album.scan.scan.AlbumEntity
import com.album.scan.scan.AlbumScanImpl
import com.album.scan.scan.AlbumSingleMediaScanner
import com.album.scan.scan.mergeEntity
import com.album.scan.ui.AlbumBaseFragment
import com.album.scan.view.AlbumView
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.album_fragment_album.*
import java.io.File

class AlbumFragment : AlbumBaseFragment(), AlbumView, SimpleAlbumFragmentInterface, AlbumAdapter.OnAlbumItemClickListener, AlbumSingleMediaScanner.SingleScannerListener {

    companion object {
        fun newInstance(albumBundle: AlbumBundle) = AlbumFragment().apply { arguments = Bundle().apply { putParcelable(AlbumConst.EXTRA_ALBUM_OPTIONS, albumBundle) } }
    }

    private var albumAction: AlbumAction? = null
    private var singleMediaScanner: AlbumSingleMediaScanner? = null
    private lateinit var albumBundle: AlbumBundle
    private lateinit var albumAdapter: AlbumAdapter
    private lateinit var albumScan: AlbumScanImpl
    private var fileUri: Uri = Uri.EMPTY
    private var fileProviderPath: String = ""
    var parent: Long = AlbumScanConst.ALL
    var finderName: String = ""
    lateinit var finderList: ArrayList<AlbumEntity>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment is AlbumAction) {
            albumAction = parentFragment as AlbumAction
        } else if (context is AlbumAction) {
            albumAction = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        albumBundle = bundle.getParcelable(AlbumConst.EXTRA_ALBUM_OPTIONS) ?: AlbumBundle()
        if (savedInstanceState == null) {
            return
        }
        parent = savedInstanceState.getLong(AlbumInternalConst.TYPE_STATE_PARENT, AlbumScanConst.ALL)
        finderName = savedInstanceState.getString(AlbumInternalConst.TYPE_STATE_FINDER_NAME, "")
        fileUri = savedInstanceState.getParcelable(AlbumInternalConst.TYPE_STATE_IMAGE_URI)
                ?: Uri.EMPTY
        fileProviderPath = savedInstanceState.getString(AlbumInternalConst.TYPE_STATE_IMAGE_PATH, "")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(AlbumInternalConst.TYPE_STATE_SELECT, getSelectEntity())
        outState.putLong(AlbumInternalConst.TYPE_STATE_PARENT, parent)
        outState.putString(AlbumInternalConst.TYPE_STATE_FINDER_NAME, finderName)
        outState.putParcelable(AlbumInternalConst.TYPE_STATE_IMAGE_URI, fileUri)
        outState.putString(AlbumInternalConst.TYPE_STATE_IMAGE_PATH, fileProviderPath)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        albumRootView.setBackgroundColor(ContextCompat.getColor(mActivity, albumBundle.rootViewBackground))
        val drawable = ContextCompat.getDrawable(mActivity, albumBundle.photoEmptyDrawable)
        drawable?.setColorFilter(ContextCompat.getColor(mActivity, albumBundle.photoEmptyDrawableColor), PorterDuff.Mode.SRC_ATOP)
        albumEmpty.setImageDrawable(drawable)
        albumEmpty.setOnClickListener { v ->
            if (Album.instance.emptyClickListener == null) {
                startCamera()
                return@setOnClickListener
            }
            Album.instance.emptyClickListener?.invoke(v)
        }
        finderList = ArrayList()
        albumScan = AlbumScanImpl(this)
        albumRecyclerView.setHasFixedSize(true)
        val gridLayoutManager = GridLayoutManager(mActivity, albumBundle.spanCount)
        albumRecyclerView.layoutManager = gridLayoutManager
        albumRecyclerView.addItemDecoration(SimpleGridDivider(albumBundle.dividerWidth))
        albumAdapter = AlbumAdapter(mActivity.square(albumBundle.spanCount), albumBundle, albumAction, this)

        val selectList = savedInstanceState?.getParcelableArrayList<AlbumEntity>(AlbumInternalConst.TYPE_STATE_SELECT)

        if (!selectList.isNullOrEmpty()) {
            albumAdapter.multipleList = selectList
        } else if (!albumBundle.radio && !Album.instance.selectList.isNullOrEmpty()) {
            albumAdapter.multipleList = Album.instance.selectList ?: ArrayList()
        }

        albumRecyclerView.adapter = albumAdapter
        savedInstanceState?.let { albumAction?.onAlbumScreenChanged(albumAdapter.multipleList.size) }
        onScanAlbum(parent, isFinder = false, result = false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_CANCELED ->
                when (requestCode) {
                    AlbumConst.TYPE_PRE_REQUEST_CODE -> onResultPreview(data?.extras.orEmpty())
                    UCrop.REQUEST_CROP -> Album.instance.albumListener?.onAlbumCropCanceled()
                    AlbumCameraConst.CAMERA_REQUEST_CODE -> Album.instance.albumListener?.onAlbumCameraCanceled()
                }
            UCrop.RESULT_ERROR -> {
                Album.instance.albumListener?.onAlbumUCropError(UCrop.getError(data.orEmpty()))
                if (albumBundle.cropErrorFinish) {
                    mActivity.finish()
                }
            }
            Activity.RESULT_OK ->
                when (requestCode) {
                    AlbumCameraConst.CUSTOM_CAMERA_REQUEST_CODE -> {
                        data?.extras?.let {
                            val customizePath = it.getString(AlbumCameraConst.RESULT_PATH)
                            if (customizePath != null && customizePath.isNotEmpty()) {
                                refreshMedia(AlbumConst.TYPE_RESULT_CAMERA, customizePath)
                                if (albumBundle.cameraCrop) {
                                    openUCrop(customizePath)
                                }
                            }
                        }
                    }
                    AlbumCameraConst.CAMERA_REQUEST_CODE -> {
                        refreshMedia(AlbumConst.TYPE_RESULT_CAMERA, mActivity.scanFilePath(fileUri, fileProviderPath).orEmpty())
                        if (albumBundle.cameraCrop) {
                            openUCrop(mActivity.scanFilePath(fileUri, fileProviderPath).orEmpty())
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
            albumEmpty.show()
            Album.instance.albumListener?.onAlbumEmpty()
            return
        }
        albumEmpty.hide()
        if (parent == AlbumScanConst.ALL && !albumBundle.hideCamera) {
            arrayList.add(0, AlbumEntity(path = AlbumInternalConst.CAMERA))
        }
        albumAdapter.addAll(arrayList)
    }

    override fun scanFinderSuccess(finderList: ArrayList<AlbumEntity>) {
        finderList.find { it.bucketDisplayName == "0" }?.bucketDisplayName = getString(albumBundle.sdName)
        finderList.find { it.parent == AlbumScanConst.ALL }?.bucketDisplayName = getString(albumBundle.allName)
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
        albumAction?.onAlbumItemClick(getSelectEntity(), position, parent)
    }

    override fun onScanAlbum(parent: Long, isFinder: Boolean, result: Boolean) {
        if (isFinder) {
            albumAdapter.removeAll()
        }
        this.parent = parent
        if (!permissionStorage()) {
            return
        }
        if (result && albumAdapter.albumList.isNotEmpty()) {
            albumScan.scanResult(mActivity.scanFilePath(fileUri, fileProviderPath).orEmpty())
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

    override fun startCamera() {
        val albumCameraListener = Album.instance.customCameraListener
        if (albumCameraListener != null) {
            albumCameraListener.invoke(this)
            return
        }
        val file = if (hasQ()) null else mActivity.albumPathFile(albumBundle.cameraPath, albumBundle.cameraName, albumBundle.cameraSuffix)
        fileProviderPath = file?.path.orEmpty()
        fileUri = mActivity.uri(file)
        val i = openCamera(fileUri, albumBundle.scanType == AlbumScanConst.VIDEO)
        if (i == AlbumCameraConst.CAMERA_ERROR) {
            Album.instance.albumListener?.onAlbumOpenCameraError()
        }
    }

    override fun refreshMedia(type: Int, path: String) {
        disconnectMediaScanner()
        singleMediaScanner = AlbumSingleMediaScanner(mActivity, path, type, this@AlbumFragment)
    }

    override fun getSelectEntity(): ArrayList<AlbumEntity> = albumAdapter.multipleList

    override fun selectPreview(): ArrayList<AlbumEntity> {
        if (getSelectEntity().isEmpty()) {
            Album.instance.albumListener?.onAlbumPreEmpty()
            return ArrayList()
        }
        return getSelectEntity()
    }

    override fun allPreview(): ArrayList<AlbumEntity> = albumAdapter.albumList

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
        val onAlbumCustomCrop = albumAction?.onAlbumCustomCrop(path) ?: false
        if (onAlbumCustomCrop) {
            return
        }
        UCrop.of(Uri.fromFile(File(path)), Uri.fromFile(mActivity.albumPathFile(albumBundle.uCropPath, albumBundle.cameraName, albumBundle.cameraSuffix)))
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
        albumAction?.onPrevChangedCount(getSelectEntity().size)
    }

    override fun onDialogResultPreview(bundle: Bundle) {
        val previewAlbumEntity = bundle.getParcelableArrayList<AlbumEntity>(AlbumConst.TYPE_PRE_SELECT)
        val isRefreshUI = bundle.getBoolean(AlbumInternalConst.TYPE_PRE_REFRESH_UI, true)
        if (!isRefreshUI || previewAlbumEntity == null) {
            return
        }
        albumAdapter.albumList.mergeEntity(previewAlbumEntity)
        albumAdapter.multipleList = previewAlbumEntity
        albumAction?.onPrevChangedCount(getSelectEntity().size)
    }

    override fun permissionsGranted(type: Int) {
        when (type) {
            AlbumPermissionConst.ALBUM -> onScanAlbum(parent, isFinder = false, result = false)
            AlbumPermissionConst.CAMERA -> startCamera()
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

internal interface SimpleAlbumFragmentInterface {
    /**
     * 打开相机
     */
    fun startCamera()

    /**
     * 扫描设备
     * [isFinder] 是否点击文件夹扫描
     * [result] 是否是拍照之后的扫描
     */
    fun onScanAlbum(parent: Long, isFinder: Boolean, result: Boolean)

    /**
     * 扫描裁剪之后的信息
     */
    fun onScanCropAlbum(path: String)

    /**
     * 裁剪
     */
    fun openUCrop(path: String)

    /**
     * 刷新图库
     */
    fun refreshMedia(type: Int, path: String)

    /**
     * 选择选中的数据
     */
    fun selectPreview(): ArrayList<AlbumEntity>

    /**
     * 确定数据
     */
    fun multipleSelect()

    /**
     * 全部数据
     */
    fun allPreview(): ArrayList<AlbumEntity>

    /**
     * 刷新[FragmentActivity.onActivityResult]数据
     */
    fun onResultPreview(bundle: Bundle)

    /**
     * 刷新数据
     */
    fun onDialogResultPreview(bundle: Bundle)

    /**
     * 断掉MediaScanner
     */
    fun disconnectMediaScanner()
}