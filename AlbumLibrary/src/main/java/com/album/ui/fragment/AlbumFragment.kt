package com.album.ui.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.album.*
import com.album.core.AlbumCamera.CUSTOMIZE_CAMERA_REQUEST_CODE
import com.album.core.AlbumCamera.CUSTOMIZE_CAMERA_RESULT_PATH_KEY
import com.album.core.AlbumCamera.OPEN_CAMERA_ERROR
import com.album.core.AlbumCamera.OPEN_CAMERA_REQUEST_CODE
import com.album.core.AlbumCamera.getCameraFile
import com.album.core.AlbumCamera.openCamera
import com.album.core.AlbumCore.imageViewWidthAndHeight
import com.album.core.AlbumCore.orEmpty
import com.album.core.scan.AlbumEntity
import com.album.core.AlbumFile.fileExists
import com.album.core.AlbumFile.pathToFile
import com.album.core.AlbumPermission.TYPE_PERMISSIONS_ALBUM
import com.album.core.AlbumPermission.TYPE_PERMISSIONS_CAMERA
import com.album.core.AlbumPermission.permissionCamera
import com.album.core.AlbumPermission.permissionStorage
import com.album.core.scan.AlbumScan.VIDEO
import com.album.core.scan.AlbumSingleMediaScanner
import com.album.core.scan.FinderEntity
import com.album.core.scan.AlbumScanImpl
import com.album.core.ui.AlbumBaseFragment
import com.album.core.view.AlbumView
import com.album.ui.adapter.AlbumAdapter
import com.album.ui.widget.LoadMoreRecyclerView
import com.album.ui.widget.SimpleGridDivider
import com.yalantis.ucrop.UCrop
import java.io.File


interface AlbumMethodFragmentView {

    /**
     * 获取文件夹list
     */
    fun getFinderEntity(): List<FinderEntity>

    /**
     * 断掉[SingleMediaScanner]
     */
    fun disconnectMediaScanner()

    /**
     * 扫描设备
     * [bucketId] 文件夹唯一
     * [isFinder] 是否点击文件夹扫描
     * [result] 是否是拍照之后的扫描
     */
    fun onScanAlbum(bucketId: String, isFinder: Boolean, result: Boolean)

    /**
     * 打开相机
     */
    fun startCamera()

    /**
     * 裁剪
     */
    fun openUCrop(path: String)

    /**
     * 刷新图库
     */
    fun refreshMedia(type: Int, file: File)

    /**
     * 选择选中的数据
     */
    fun selectPreview(): ArrayList<AlbumEntity>

    /**
     * 确定数据[AlbumListener.onAlbumResources]
     */
    fun multipleSelect()

    /**
     * 刷新[FragmentActivity.onActivityResult]数据
     */
    fun onResultPreview(bundle: Bundle)
}


/**
 * by y on 14/08/2017.
 */

class AlbumFragment : AlbumBaseFragment(), AlbumView, AlbumMethodFragmentView, AlbumAdapter.OnItemClickListener, AlbumSingleMediaScanner.SingleScannerListener, LoadMoreRecyclerView.LoadMoreListener {

    companion object {
        fun newInstance(albumBundle: AlbumBundle) = AlbumFragment().apply {
            arguments = Bundle().apply { putParcelable(EXTRA_ALBUM_OPTIONS, albumBundle) }
        }
    }

    private lateinit var recyclerView: LoadMoreRecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var albumAdapter: AlbumAdapter
    private lateinit var emptyView: AppCompatImageView
    private lateinit var albumScan: AlbumScanImpl

    var albumParentListener: AlbumParentListener? = null

    private lateinit var finderEntityList: ArrayList<FinderEntity>
    private lateinit var imagePath: Uri

    private lateinit var albumBundle: AlbumBundle
    private lateinit var selectAlbumEntity: ArrayList<AlbumEntity>

    private var singleMediaScanner: AlbumSingleMediaScanner? = null
    var bucketId: String = ""
    private var page = 0

    var finderName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        albumBundle = bundle.getParcelable(EXTRA_ALBUM_OPTIONS) ?: AlbumBundle()
        selectAlbumEntity = savedInstanceState?.getParcelableArrayList(TYPE_ALBUM_STATE_SELECT)
                ?: ArrayList()
        if (savedInstanceState == null) {
            imagePath = Uri.fromFile(mActivity.getCameraFile(albumBundle.cameraPath, albumBundle.scanType == VIDEO))
            return
        }
        bucketId = savedInstanceState.getString(TYPE_ALBUM_STATE_BUCKET_ID, "")
        finderName = savedInstanceState.getString(TYPE_ALBUM_STATE_FINDER_NAME, "")
        imagePath = savedInstanceState.getParcelable(TYPE_ALBUM_STATE_IMAGE_PATH) ?: Uri.EMPTY
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(TYPE_ALBUM_STATE_SELECT, albumAdapter.getMultiplePreviewList())
        outState.putString(TYPE_ALBUM_STATE_BUCKET_ID, bucketId)
        outState.putString(TYPE_ALBUM_STATE_FINDER_NAME, finderName)
        outState.putParcelable(TYPE_ALBUM_STATE_IMAGE_PATH, imagePath)
    }

    override fun initCreate(savedInstanceState: Bundle?) {}

    override fun initView(view: View) {
        view.findViewById<View>(R.id.album_content_view).setBackgroundColor(ContextCompat.getColor(view.context, albumBundle.rootViewBackground))
        recyclerView = view.findViewById(R.id.album_recyclerView)
        progressBar = view.findViewById(R.id.album_progress)
        emptyView = view.findViewById(R.id.album_empty)
        val drawable = ContextCompat.getDrawable(mActivity, albumBundle.photoEmptyDrawable)
        drawable?.setColorFilter(ContextCompat.getColor(mActivity, albumBundle.photoEmptyDrawableColor), PorterDuff.Mode.SRC_ATOP)
        emptyView.setImageDrawable(drawable)
        emptyView.setOnClickListener { v ->
            val emptyClickListener = Album.instance.emptyClickListener
            if (emptyClickListener != null) {
                if (emptyClickListener.click(v)) {
                    startCamera()
                }
            }
        }
    }

    override fun initActivityCreated(savedInstanceState: Bundle?) {
        finderEntityList = ArrayList()
        albumScan = AlbumScanImpl.newInstance(this, albumBundle.scanType, albumBundle.scanCount, albumBundle.allName, albumBundle.sdName, albumBundle.filterImg)
        recyclerView.setHasFixedSize(true)
        val gridLayoutManager = GridLayoutManager(mActivity, albumBundle.spanCount)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setLoadingListener(this)
        recyclerView.addItemDecoration(SimpleGridDivider(albumBundle.dividerWidth))
        albumAdapter = AlbumAdapter(mActivity.imageViewWidthAndHeight(albumBundle.spanCount))
        albumAdapter.setOnItemClickListener(this)
        albumAdapter.setAlbumBundle(albumBundle)
        recyclerView.adapter = albumAdapter
        onScanAlbum(bucketId, isFinder = false, result = false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_CANCELED -> when (requestCode) {
                TYPE_PREVIEW_CODE -> onResultPreview(data?.extras.orEmpty())
                UCrop.REQUEST_CROP -> Album.instance.albumListener?.onAlbumCropCanceled()
                OPEN_CAMERA_REQUEST_CODE -> Album.instance.albumListener?.onAlbumCameraCanceled()
            }
            UCrop.RESULT_ERROR -> {
                Album.instance.albumListener?.onAlbumUCropError(UCrop.getError(data.orEmpty()))
                if (albumBundle.cropErrorFinish) {
                    mActivity.finish()
                }
            }
            Activity.RESULT_OK -> when (requestCode) {
                CUSTOMIZE_CAMERA_REQUEST_CODE -> {
                    val extras = data?.extras
                    if (extras != null) {
                        val customizePath = extras.getString(CUSTOMIZE_CAMERA_RESULT_PATH_KEY)
                        if (!TextUtils.isEmpty(customizePath)) {
                            imagePath = Uri.fromFile(File(customizePath))
                            refreshMedia(TYPE_RESULT_CAMERA, imagePath.path.orEmpty().pathToFile())
                            if (albumBundle.cameraCrop) {
                                openUCrop(imagePath.path.orEmpty())
                            }
                        }
                    }
                }
                OPEN_CAMERA_REQUEST_CODE -> {
                    refreshMedia(TYPE_RESULT_CAMERA, imagePath.path.orEmpty().pathToFile())
                    if (albumBundle.cameraCrop) {
                        openUCrop(imagePath.path.orEmpty())
                    }
                }
                UCrop.REQUEST_CROP -> {
                    if (data == null) {
                        Album.instance.albumListener?.onAlbumUCropError(null)
                    } else {
                        val path = data.extras?.getParcelable<Uri>(UCrop.EXTRA_OUTPUT_URI)?.path.orEmpty()
                        Album.instance.albumListener?.onAlbumUCropResources(path.pathToFile())
                        refreshMedia(TYPE_RESULT_CROP, path.pathToFile())
                    }
                    if (albumBundle.cropFinish) {
                        mActivity.finish()
                    }
                }
                TYPE_PREVIEW_CODE -> onResultPreview(data?.extras.orEmpty())
            }
        }
    }


    override fun scanSuccess(albumEntityList: ArrayList<AlbumEntity>) {
        if (emptyView.visibility == View.VISIBLE) {
            emptyView.visibility = View.GONE
        }
        if (TextUtils.isEmpty(bucketId) && !albumBundle.hideCamera && page == 0 && !albumEntityList.isEmpty()) {
            albumEntityList.add(0, AlbumEntity(path = CAMERA))
        }
        albumAdapter.addAll(albumEntityList)
        if (page == 0 && !albumBundle.radio) {
            val selectEntity = Album.instance.initList
            if (selectEntity != null && !selectEntity.isEmpty() && !albumEntityList.isEmpty()) {
                albumScan.mergeEntity(albumEntityList, selectEntity)
                albumAdapter.setMultiplePreviewList(selectEntity)
                selectAlbumEntity = selectEntity
            }
        }
        ++page
    }

    override fun scanFinderSuccess(list: ArrayList<FinderEntity>) {
        finderEntityList.clear()
        finderEntityList.addAll(list)
    }

    override fun getSelectEntity(): ArrayList<AlbumEntity> {
        if (!selectAlbumEntity.isEmpty()) {
            albumAdapter.setMultiplePreviewList(selectAlbumEntity)
        }
        return albumAdapter.getMultiplePreviewList()
    }

    override fun onAlbumNoMore() {
        Album.instance.albumListener?.onAlbumNoMore()
    }

    override fun onAlbumEmpty() {
        emptyView.visibility = View.VISIBLE
        Album.instance.albumListener?.onAlbumEmpty()
    }

    override fun resultSuccess(albumEntity: AlbumEntity?) {
        if (albumEntity == null) {
            Album.instance.albumListener?.onAlbumResultCameraError()
        } else {
            albumAdapter.getAlbumList().add(1, albumEntity)
            albumAdapter.notifyDataSetChanged()
        }
    }

    override fun onItemClick(view: View, position: Int, albumEntity: AlbumEntity) {
        if (position == 0 && TextUtils.equals(albumEntity.path, CAMERA)) {
            if (permissionCamera()) {
                startCamera()
            }
            return
        }
        if (!albumEntity.path.fileExists()) {
            Album.instance.albumListener?.onAlbumFileNotExist()
            return
        }
        if (albumBundle.scanType == VIDEO) {
            try {
                val openVideo = Intent(Intent.ACTION_VIEW)
                openVideo.setDataAndType(Uri.parse(albumEntity.path), "video/*")
                startActivity(openVideo)
            } catch (e: Exception) {
                Album.instance.albumListener?.onVideoPlayError()
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
        albumParentListener?.onAlbumItemClick(albumAdapter.getMultiplePreviewList(), position, bucketId)
    }

    override fun onScanAlbum(bucketId: String, isFinder: Boolean, result: Boolean) {
        if (isFinder) {
            page = 0
            albumAdapter.removeAll()
        }
        this.bucketId = bucketId
        if (permissionStorage()) {
            if (result && !albumAdapter.getAlbumList().isEmpty()) {
                albumScan.resultScan(imagePath.path.orEmpty())
                return
            }
            albumScan.scanAll(bucketId, page)
        }
    }

    override fun onScanStart() {}

    override fun onScanCompleted(type: Int) {
        if (type == TYPE_RESULT_CROP) {
            return
        }
        mActivity.runOnUiThread { onScanAlbum(bucketId, isFinder = false, result = true) }
    }

    override fun disconnectMediaScanner() {
        if (singleMediaScanner != null) {
            singleMediaScanner?.disconnect()
            singleMediaScanner = null
        }
    }

    override fun startCamera() {
        val albumCameraListener = Album.instance.customCameraListener
        if (albumCameraListener != null) {
            albumCameraListener.startCamera(this)
            return
        }
        imagePath = Uri.fromFile(mActivity.getCameraFile(albumBundle.cameraPath, albumBundle.scanType == VIDEO))
        val i = openCamera(imagePath, albumBundle.scanType == VIDEO)
        if (i == OPEN_CAMERA_ERROR) {
            Album.instance.albumListener?.onAlbumOpenCameraError()
        }
    }

    override fun refreshMedia(type: Int, file: File) {
        disconnectMediaScanner()
        singleMediaScanner = AlbumSingleMediaScanner(mActivity, file, this@AlbumFragment, type)
    }

    override fun selectPreview(): ArrayList<AlbumEntity> {
        val albumEntityList = albumAdapter.getMultiplePreviewList()
        if (albumEntityList.isEmpty()) {
            Album.instance.albumListener?.onAlbumPreviewEmpty()
            return ArrayList()
        }
        return albumEntityList
    }

    override fun multipleSelect() {
        val albumEntityList = albumAdapter.getMultiplePreviewList()
        if (albumEntityList.isEmpty()) {
            Album.instance.albumListener?.onAlbumSelectEmpty()
            return
        }
        Album.instance.albumListener?.onAlbumResources(albumEntityList)
        if (albumBundle.selectImageFinish) {
            mActivity.finish()
        }
    }

    override fun openUCrop(path: String) {
        UCrop.of(Uri.fromFile(File(path)), Uri.fromFile(mActivity.getCameraFile(albumBundle.uCropPath, albumBundle.scanType == VIDEO)))
                .withOptions(Album.instance.options ?: UCrop.Options())
                .start(mActivity, this)
    }

    override fun onResultPreview(bundle: Bundle) {
        val previewAlbumEntity = bundle.getParcelableArrayList<AlbumEntity>(TYPE_PREVIEW_KEY)
        val isRefreshUI = bundle.getBoolean(TYPE_PREVIEW_REFRESH_UI, true)
        val isFinish = bundle.getBoolean(TYPE_PREVIEW_FINISH, false)
        if (isFinish) {
            mActivity.finish()
            return
        }
        if (!isRefreshUI || previewAlbumEntity == null || selectAlbumEntity == previewAlbumEntity) {
            return
        }
        selectAlbumEntity = previewAlbumEntity
        albumScan.mergeEntity(albumAdapter.getAlbumList(), previewAlbumEntity)
        albumAdapter.setMultiplePreviewList(previewAlbumEntity)
    }

    override fun onLoadMore() {
        if (permissionStorage()) {
            albumScan.scanAll(bucketId, page)
        }
    }

    override fun permissionsGranted(type: Int) {
        when (type) {
            TYPE_PERMISSIONS_ALBUM -> onScanAlbum(bucketId, isFinder = false, result = false)
            TYPE_PERMISSIONS_CAMERA -> startCamera()
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

    override fun showProgress() {
        if (progressBar.visibility == View.GONE) {
            progressBar.visibility = View.VISIBLE
        }
    }

    override fun hideProgress() {
        if (progressBar.visibility == View.VISIBLE) {
            progressBar.visibility = View.GONE
        }
    }

    override fun getFinderEntity(): List<FinderEntity> = finderEntityList
    override fun getAlbumActivity(): FragmentActivity = mActivity
    override fun getPage(): Int = page
    override val layoutId: Int = R.layout.album_fragment_album
}

