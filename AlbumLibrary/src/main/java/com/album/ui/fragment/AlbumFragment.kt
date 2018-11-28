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
import androidx.recyclerview.widget.GridLayoutManager
import com.album.*
import com.album.presenter.impl.AlbumPresenterImpl
import com.album.ui.LoadMoreRecyclerView
import com.album.ui.SimpleGridDivider
import com.album.ui.adapter.AlbumAdapter
import com.album.ui.view.AlbumMethodFragmentView
import com.album.ui.view.AlbumView
import com.album.util.*
import com.yalantis.ucrop.UCrop
import java.io.File

/**
 * by y on 14/08/2017.
 */

class AlbumFragment : AlbumBaseFragment(), AlbumView, AlbumMethodFragmentView, AlbumAdapter.OnItemClickListener, SingleScannerListener, LoadMoreRecyclerView.LoadMoreListener {

    companion object {
        fun newInstance(albumBundle: AlbumBundle) = AlbumFragment().apply {
            arguments = Bundle().apply { putParcelable(EXTRA_ALBUM_OPTIONS, albumBundle) }
        }
    }

    private lateinit var recyclerView: LoadMoreRecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var albumAdapter: AlbumAdapter
    private lateinit var emptyView: AppCompatImageView
    private lateinit var albumPresenter: AlbumPresenterImpl

    lateinit var albumParentListener: AlbumParentListener
    private lateinit var finderEntityList: ArrayList<FinderEntity>
    private lateinit var imagePath: Uri

    private lateinit var albumBundle: AlbumBundle
    private lateinit var multipleAlbumEntity: ArrayList<AlbumEntity>
    private var singleMediaScanner: SingleMediaScanner? = null
    private var bucketId: String = ""
    var finderName: String = ""
    private var page = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        albumBundle = bundle.getParcelable(EXTRA_ALBUM_OPTIONS) ?: AlbumBundle()
        if (savedInstanceState == null) {
            imagePath = Uri.fromFile(FileUtils.getCameraFile(mActivity, albumBundle.cameraPath, albumBundle.scanType == VIDEO))
            return
        }
        bucketId = savedInstanceState.getString(AlbumConstant.TYPE_ALBUM_STATE_BUCKET_ID, "")
        multipleAlbumEntity = savedInstanceState.getParcelableArrayList(AlbumConstant.TYPE_ALBUM_STATE_SELECT) ?: ArrayList()
        finderName = savedInstanceState.getString(AlbumConstant.TYPE_ALBUM_STATE_FINDER_NAME, "")
        imagePath = savedInstanceState.getParcelable(AlbumConstant.TYPE_ALBUM_STATE_IMAGE_PATH) ?: Uri.EMPTY
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(AlbumConstant.TYPE_ALBUM_STATE_SELECT, albumAdapter.getMultiplePreviewList())
        outState.putString(AlbumConstant.TYPE_ALBUM_STATE_BUCKET_ID, bucketId)
        outState.putString(AlbumConstant.TYPE_ALBUM_STATE_FINDER_NAME, finderName)
        outState.putParcelable(AlbumConstant.TYPE_ALBUM_STATE_IMAGE_PATH, imagePath)
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
                    openCamera()
                }
            }
        }
    }

    override fun initActivityCreated(savedInstanceState: Bundle?) {
        albumPresenter = AlbumPresenterImpl(this, albumBundle)
        finderEntityList = ArrayList()
        multipleAlbumEntity = ArrayList()
        recyclerView.setHasFixedSize(true)
        val gridLayoutManager = GridLayoutManager(mActivity, albumBundle.spanCount)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setLoadingListener(this)
        recyclerView.addItemDecoration(SimpleGridDivider(albumBundle.dividerWidth))
        albumAdapter = AlbumAdapter(ArrayList(), getImageViewWidth(mActivity, albumBundle.spanCount), albumBundle)
        albumAdapter.setOnItemClickListener(this)
        recyclerView.adapter = albumAdapter
        onScanAlbum(bucketId, false, false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_CANCELED -> when (requestCode) {
                AlbumConstant.TYPE_PREVIEW_CODE -> onResultPreview(checkNotBundleNull(data?.extras))
                UCrop.REQUEST_CROP -> Album.instance.albumListener.onAlbumCropCanceled()
                AlbumConstant.ITEM_CAMERA -> Album.instance.albumListener.onAlbumCameraCanceled()
            }
            UCrop.RESULT_ERROR -> {
                Album.instance.albumListener.onAlbumUCropError(UCrop.getError(checkNotIntentNull(data)))
                if (albumBundle.cropErrorFinish) {
                    mActivity.finish()
                }
            }
            Activity.RESULT_OK -> when (requestCode) {
                AlbumConstant.CUSTOMIZE_CAMERA_RESULT_CODE -> {
                    val extras = data?.extras
                    if (extras != null) {
                        val customizePath = extras.getString(AlbumConstant.CUSTOMIZE_CAMERA_RESULT_PATH_KEY)
                        if (!TextUtils.isEmpty(customizePath)) {
                            imagePath = Uri.fromFile(File(customizePath))
                            refreshMedia(AlbumConstant.TYPE_RESULT_CAMERA, FileUtils.getScannerFile(checkNotStringNull(imagePath.path)))
                            if (albumBundle.cameraCrop) {
                                openUCrop(checkNotStringNull(imagePath.path))
                            }
                        }
                    }
                }
                AlbumConstant.ITEM_CAMERA -> {
                    refreshMedia(AlbumConstant.TYPE_RESULT_CAMERA, FileUtils.getScannerFile(checkNotStringNull(imagePath.path)))
                    if (albumBundle.cameraCrop) {
                        openUCrop(checkNotStringNull(imagePath.path))
                    }
                }
                UCrop.REQUEST_CROP -> {
                    if (data == null) {
                        Album.instance.albumListener.onAlbumUCropError(null)
                    } else {
                        val path = checkNotStringNull(data.extras?.getParcelable<Uri>(UCrop.EXTRA_OUTPUT_URI)?.path)
                        Album.instance.albumListener.onAlbumUCropResources(FileUtils.getScannerFile(path))
                        refreshMedia(AlbumConstant.TYPE_RESULT_CROP, FileUtils.getScannerFile(path))
                    }
                    if (albumBundle.cropFinish) {
                        mActivity.finish()
                    }
                }
                AlbumConstant.TYPE_PREVIEW_CODE -> onResultPreview(checkNotBundleNull(data?.extras))
            }
        }
    }


    override fun scanSuccess(albumEntityList: ArrayList<AlbumEntity>) {
        if (emptyView.visibility == View.VISIBLE) {
            emptyView.visibility = View.GONE
        }
        if (TextUtils.isEmpty(bucketId) && !albumBundle.hideCamera && page == 0 && !albumEntityList.isEmpty()) {
            albumEntityList.add(0, AlbumEntity("", "", AlbumConstant.CAMERA, 0, false))
        }
        albumAdapter.addAll(albumEntityList)
        if (page == 0 && !albumBundle.radio) {
            val selectEntity = Album.instance.albumEntityList
            if (selectEntity != null && !selectEntity.isEmpty() && !albumEntityList.isEmpty()) {
                albumPresenter.firstMergeEntity(albumEntityList, selectEntity)
                albumAdapter.setMultiplePreviewList(selectEntity)
            }
        }
        ++page
    }

    override fun scanFinder(list: ArrayList<FinderEntity>) {
        finderEntityList.clear()
        finderEntityList.addAll(list)
    }

    override fun getSelectEntity(): ArrayList<AlbumEntity> {
        if (!multipleAlbumEntity.isEmpty()) {
            albumAdapter.setMultiplePreviewList(multipleAlbumEntity)
        }
        return albumAdapter.getMultiplePreviewList()
    }

    override fun onAlbumNoMore() {
        Album.instance.albumListener.onAlbumNoMore()
    }

    override fun onAlbumEmpty() {
        emptyView.visibility = View.VISIBLE
        Album.instance.albumListener.onAlbumEmpty()
    }

    override fun resultSuccess(albumEntity: AlbumEntity?) {
        if (albumEntity == null) {
            Album.instance.albumListener.onAlbumResultCameraError()
        } else {
            albumAdapter.getAlbumList().add(1, albumEntity)
            albumAdapter.notifyDataSetChanged()
        }
    }

    override fun onItemClick(view: View, position: Int, albumEntity: AlbumEntity) {
        if (position == 0 && TextUtils.equals(albumEntity.path, AlbumConstant.CAMERA)) {
            if (PermissionUtils.camera(this)) {
                openCamera()
            }
            return
        }
        if (!FileUtils.isFile(albumEntity.path)) {
            Album.instance.albumListener.onAlbumFileNotExist()
            return
        }
        if (albumBundle.scanType == VIDEO) {
            try {
                val openVideo = Intent(Intent.ACTION_VIEW)
                openVideo.setDataAndType(Uri.parse(albumEntity.path), AlbumConstant.VIDEO_PLAY_TYPE)
                startActivity(openVideo)
            } catch (e: Exception) {
                Album.instance.albumListener.onVideoPlayError()
            }
            return
        }
        if (albumBundle.radio) {
            if (albumBundle.crop) {
                openUCrop(albumEntity.path)
            } else {
                val list = ArrayList<AlbumEntity>()
                list.add(albumEntity)
                Album.instance.albumListener.onAlbumResources(list)
                if (albumBundle.selectImageFinish) {
                    mActivity.finish()
                }
            }
            return
        }
        if (albumBundle.noPreview) {
            return
        }
        albumParentListener.onAlbumItemClick(albumAdapter.getMultiplePreviewList(), position, bucketId)
    }

    override fun onScanAlbum(bucketId: String, isFinder: Boolean, result: Boolean) {
        if (isFinder) {
            page = 0
            albumAdapter.removeAll()
        }
        this.bucketId = bucketId
        if (PermissionUtils.storage(this)) {
            if (result && !albumAdapter.getAlbumList().isEmpty()) {
                albumPresenter.resultScan(checkNotStringNull(imagePath.path))
                return
            }
            albumPresenter.scan(bucketId, page, albumBundle.scanCount)
        }
    }

    override fun onScanStart() {}

    override fun onScanCompleted(@AlbumResultType type: Int) {
        if (type == AlbumConstant.TYPE_RESULT_CROP) {
            return
        }
        onScanAlbum(bucketId, false, true)
    }

    override fun disconnectMediaScanner() {
        if (singleMediaScanner != null) {
            singleMediaScanner?.disconnect()
            singleMediaScanner = null
        }
    }

    override fun openCamera() {
        val albumCameraListener = Album.instance.albumCustomListener
        if (albumCameraListener != null) {
            albumCameraListener.startCamera(this)
            return
        }
        imagePath = Uri.fromFile(FileUtils.getCameraFile(mActivity, albumBundle.cameraPath, albumBundle.scanType == VIDEO))
        val i = openCamera(this, imagePath, albumBundle.scanType == VIDEO)
        if (i == 1) {
            Album.instance.albumListener.onAlbumOpenCameraError()
        }
    }

    override fun refreshMedia(@AlbumResultType type: Int, file: File) {
        disconnectMediaScanner()
        singleMediaScanner = SingleMediaScanner(mActivity, file, this@AlbumFragment, type)
    }

    override fun multiplePreview(): ArrayList<AlbumEntity>? {
        val albumEntityList = albumAdapter.getMultiplePreviewList()
        if (albumEntityList.isEmpty()) {
            Album.instance.albumListener.onAlbumPreviewEmpty()
            return null
        }
        return albumEntityList
    }

    override fun multipleSelect() {
        val albumEntityList = albumAdapter.getMultiplePreviewList()
        if (albumEntityList.isEmpty()) {
            Album.instance.albumListener.onAlbumSelectEmpty()
            return
        }
        Album.instance.albumListener.onAlbumResources(albumEntityList)
        if (albumBundle.selectImageFinish) {
            mActivity.finish()
        }
    }

    override fun openUCrop(path: String) {
        UCrop.of(Uri.fromFile(File(path)), Uri.fromFile(FileUtils.getCameraFile(mActivity, albumBundle.uCropPath, albumBundle.scanType == VIDEO)))
                .withOptions(Album.instance.options)
                .start(mActivity, this)
    }

    override fun onResultPreview(bundle: Bundle) {
        val previewAlbumEntity = bundle.getParcelableArrayList<AlbumEntity>(AlbumConstant.PREVIEW_KEY)
        val isRefreshUI = bundle.getBoolean(AlbumConstant.PREVIEW_REFRESH_UI, true)
        val isFinish = bundle.getBoolean(AlbumConstant.PREVIEW_FINISH, false)
        if (isFinish) {
            mActivity.finish()
            return
        }
        if (!isRefreshUI || previewAlbumEntity == null || multipleAlbumEntity == previewAlbumEntity) {
            return
        }
        multipleAlbumEntity = previewAlbumEntity
        albumPresenter.mergeSelectEntity(albumAdapter.getAlbumList(), previewAlbumEntity)
        albumAdapter.setMultiplePreviewList(previewAlbumEntity)
    }

    override fun onLoadMore() {
        if (PermissionUtils.storage(this) && !albumPresenter.getScanLoading()) {
            albumPresenter.scan(bucketId, page, albumBundle.scanCount)
        }
    }

    override fun permissionsGranted(type: Int) {
        when (type) {
            AlbumConstant.TYPE_PERMISSIONS_ALBUM -> onScanAlbum(bucketId, false, false)
            AlbumConstant.TYPE_PERMISSIONS_CAMERA -> openCamera()
        }
    }

    override fun permissionsDenied(type: Int) {
        Album.instance.albumListener.onAlbumPermissionsDenied(type)
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
    override fun getAlbumActivity(): Activity = mActivity
    override fun getPage(): Int = page
    override fun getLayoutId(): Int = R.layout.album_fragment_album
}
