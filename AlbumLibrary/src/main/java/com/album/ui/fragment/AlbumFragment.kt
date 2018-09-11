package com.album.ui.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.GridLayoutManager
import android.text.TextUtils
import android.view.View
import android.widget.ProgressBar
import com.album.Album
import com.album.AlbumConstant
import com.album.R
import com.album.annotation.AlbumResultType
import com.album.entity.AlbumEntity
import com.album.entity.FinderEntity
import com.album.presenter.impl.AlbumPresenterImpl
import com.album.ui.activity.PreviewActivity
import com.album.ui.adapter.AlbumAdapter
import com.album.ui.view.AlbumMethodFragmentView
import com.album.ui.view.AlbumView
import com.album.ui.widget.LoadMoreRecyclerView
import com.album.ui.widget.SimpleGridDivider
import com.album.util.AlbumTool
import com.album.util.FileUtils
import com.album.util.PermissionUtils
import com.album.util.scanner.SingleMediaScanner
import com.album.util.scanner.SingleScannerListener
import com.yalantis.ucrop.UCrop
import java.io.File

/**
 * by y on 14/08/2017.
 */

class AlbumFragment : AlbumBaseFragment(),
        AlbumView,
        AlbumMethodFragmentView,
        AlbumAdapter.OnItemClickListener,
        SingleScannerListener,
        LoadMoreRecyclerView.LoadMoreListener {

    companion object {
        fun newInstance(): AlbumFragment {
            return AlbumFragment()
        }
    }

    private lateinit var recyclerView: LoadMoreRecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var albumAdapter: AlbumAdapter
    private lateinit var emptyView: AppCompatImageView
    private lateinit var albumPresenter: AlbumPresenterImpl

    private lateinit var finderEntityList: ArrayList<FinderEntity>
    private lateinit var uCropImagePath: Uri
    private lateinit var imagePath: Uri

    private lateinit var multipleAlbumEntity: ArrayList<AlbumEntity>
    private var singleMediaScanner: SingleMediaScanner? = null
    private var bucketId: String = ""
    var finderName: String = ""
    private var page = 0

    /**
     * 横竖屏切换获取数据
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            return
        }
        bucketId = savedInstanceState.getString(AlbumConstant.TYPE_ALBUM_STATE_BUCKET_ID)
        multipleAlbumEntity = savedInstanceState.getParcelableArrayList(AlbumConstant.TYPE_ALBUM_STATE_SELECT)
        finderName = savedInstanceState.getString(AlbumConstant.TYPE_ALBUM_STATE_FINDER_NAME)
    }

    override fun initCreate(savedInstanceState: Bundle?) {
        uCropImagePath = Uri.fromFile(FileUtils.getCameraFile(mActivity, albumConfig.uCropPath, albumConfig.isVideo))
        imagePath = Uri.fromFile(FileUtils.getCameraFile(mActivity, albumConfig.cameraPath, albumConfig.isVideo))
    }

    override fun initView(view: View) {
        view.findViewById<View>(R.id.album_content_view).setBackgroundColor(ContextCompat.getColor(view.context, albumConfig.albumContentViewBackground))
        recyclerView = view.findViewById(R.id.album_recyclerView)
        progressBar = view.findViewById(R.id.album_progress)
        emptyView = view.findViewById(R.id.album_empty)
        albumPresenter = AlbumPresenterImpl(this, albumConfig.isVideo)
        val drawable = ContextCompat.getDrawable(mActivity, albumConfig.albumContentEmptyDrawable)
        drawable?.setColorFilter(ContextCompat.getColor(mActivity, albumConfig.albumContentEmptyDrawableColor), PorterDuff.Mode.SRC_ATOP)
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
        finderEntityList = ArrayList()
        multipleAlbumEntity = ArrayList()
        initRecyclerView()
        onScanAlbum(bucketId, false, false)
    }

    override fun initRecyclerView() {
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(mActivity, albumConfig.spanCount)
        recyclerView.setLoadingListener(this)
        recyclerView.addItemDecoration(SimpleGridDivider(albumConfig.dividerWidth))
        albumAdapter = AlbumAdapter(ArrayList(), AlbumTool.getImageViewWidth(mActivity, albumConfig.spanCount))
        albumAdapter.setOnItemClickListener(this)
        recyclerView.adapter = albumAdapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_CANCELED -> when (requestCode) {
                AlbumConstant.TYPE_PREVIEW_CODE -> onResultPreview(data!!.extras)
                UCrop.REQUEST_CROP -> Album.instance.albumListener.onAlbumFragmentCropCanceled()
                AlbumConstant.ITEM_CAMERA -> Album.instance.albumListener.onAlbumFragmentCameraCanceled()
            }
            UCrop.RESULT_ERROR -> {
                Album.instance.albumListener.onAlbumFragmentUCropError(UCrop.getError(data!!))
                mActivity.finish()
            }
            Activity.RESULT_OK -> when (requestCode) {
                AlbumConstant.CUSTOMIZE_CAMERA_RESULT_CODE -> {
                    val extras = data!!.extras
                    if (extras != null) {
                        val customizePath = extras.getString(AlbumConstant.CUSTOMIZE_CAMERA_RESULT_PATH_KEY)
                        if (!TextUtils.isEmpty(customizePath)) {
                            imagePath = Uri.fromFile(File(customizePath))
                            refreshMedia(AlbumConstant.TYPE_RESULT_CAMERA)
                            if (albumConfig.cameraCrop) {
                                openUCrop(imagePath.path, uCropImagePath)
                            }
                        }
                    }
                }
                AlbumConstant.ITEM_CAMERA -> {
                    refreshMedia(AlbumConstant.TYPE_RESULT_CAMERA)
                    if (albumConfig.cameraCrop) {
                        openUCrop(imagePath.path, uCropImagePath)
                    }
                }
                UCrop.REQUEST_CROP -> {
                    Album.instance.albumListener.onAlbumUCropResources(FileUtils.getScannerFile(uCropImagePath.path))
                    refreshMedia(AlbumConstant.TYPE_RESULT_CROP)
                    mActivity.finish()
                }
                AlbumConstant.TYPE_PREVIEW_CODE -> onResultPreview(data!!.extras)
            }
        }
    }


    override fun scanSuccess(albumEntityList: ArrayList<AlbumEntity>) {
        if (emptyView.visibility == View.VISIBLE) {
            emptyView.visibility = View.GONE
        }
        if (TextUtils.isEmpty(bucketId) && !albumConfig.hideCamera && page == 0 && !albumEntityList.isEmpty()) {
            albumEntityList.add(0, AlbumEntity("", "", AlbumConstant.CAMERA, 0, false))
        }
        albumAdapter.addAll(albumEntityList)
        if (page == 0 && !albumConfig.isRadio) {
            val selectEntity = Album.instance.albumEntityList
            if (selectEntity != null && !selectEntity.isEmpty() && !albumEntityList.isEmpty()) {
                albumPresenter.firstMergeEntity(albumEntityList, selectEntity)
                albumAdapter.setMultiplePreviewList(selectEntity)
            }
        }
        ++page
    }

    /**
     * 刷新目录
     */
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

    /**
     * 没有更多数据
     */
    override fun onAlbumNoMore() {
        if (page == 0) {
            emptyView.visibility = View.VISIBLE
        }
        Album.instance.albumListener.onAlbumNoMore()
    }

    /**
     * 拍照之后刷新成功,添加该图片
     */
    override fun resultSuccess(albumEntity: AlbumEntity) {
        albumAdapter.getAlbumList().add(1, albumEntity)
        albumAdapter.notifyDataSetChanged()
    }

    override fun onItemClick(view: View, position: Int, albumEntity: AlbumEntity) {
        if (position == 0 && TextUtils.equals(albumEntity.path, AlbumConstant.CAMERA)) {
            if (PermissionUtils.camera(this)) {
                openCamera()
            }
            return
        }
        if (!FileUtils.isFile(albumEntity.path)) {
            Album.instance.albumListener.onAlbumFragmentFileNull()
            return
        }
        if (albumConfig.isVideo) {
            try {
                val openVideo = Intent(Intent.ACTION_VIEW)
                openVideo.setDataAndType(Uri.parse(albumEntity.path), AlbumConstant.VIDEO_PLAY_TYPE)
                startActivity(openVideo)
            } catch (e: Exception) {
                Album.instance.albumListener.onVideoPlayError()
            }
            return
        }
        if (albumConfig.isRadio) {
            if (albumConfig.isCrop) {
                openUCrop(albumEntity.path, uCropImagePath)
            } else {
                val list = ArrayList<AlbumEntity>()
                list.add(albumEntity)
                Album.instance.albumListener.onAlbumResources(list)
                mActivity.finish()
            }
            return
        }
        val bundle = Bundle()
        val multiplePreviewList = albumAdapter.getMultiplePreviewList()
        bundle.putParcelableArrayList(AlbumConstant.PREVIEW_KEY, multiplePreviewList)
        bundle.putInt(AlbumConstant.PREVIEW_POSITION_KEY, position)
        bundle.putString(AlbumConstant.PREVIEW_BUCKET_ID, bucketId)
        startActivityForResult(Intent(mActivity, if (Album.instance.previewClass == null) PreviewActivity::class.java else Album.instance.previewClass)
                .putExtras(bundle), AlbumConstant.TYPE_PREVIEW_CODE)
    }


    /**
     * [result]:是否是拍照之后扫描数据库,如果是只需要刷新当前信息即可,没有必要重新刷新全部数据
     * [isFinder]:如果是点击finder刷新数据,则重置recyclerView
     */
    override fun onScanAlbum(bucketId: String, isFinder: Boolean, result: Boolean) {
        if (isFinder) {
            page = 0
            albumAdapter.removeAll()
        }
        this.bucketId = bucketId
        if (PermissionUtils.storage(this)) {
            if (result && !albumAdapter.getAlbumList().isEmpty()) {
                albumPresenter.resultScan(imagePath.path)
                return
            }
            albumPresenter.scan(bucketId, page, albumConfig.count)
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
            singleMediaScanner!!.disconnect()
            singleMediaScanner = null
        }
    }

    /**
     * 打开相机,在这里可拦截然后自定义相机进行拍照或者摄像
     */
    override fun openCamera() {
        if (albumConfig.isVideo && Album.instance.albumVideoListener != null) {
            Album.instance.albumVideoListener?.startVideo(this)
            return
        }
        val albumCameraListener = Album.instance.albumCameraListener
        if (albumCameraListener != null) {
            albumCameraListener.startCamera(this)
            return
        }
        imagePath = Uri.fromFile(FileUtils.getCameraFile(mActivity, albumConfig.cameraPath, albumConfig.isVideo))
        val i = AlbumTool.openCamera(this, imagePath, albumConfig.isVideo)
        if (i == 1) {
            Album.instance.albumListener.onAlbumOpenCameraError()
        }
    }

    /**
     * 进入裁剪
     */
    override fun openUCrop(path: String, uri: Uri) {
        UCrop.of(Uri.fromFile(File(path)), uri)
                .withOptions(Album.instance.options)
                .start(mActivity, this)
    }


    /**
     * 在裁剪或者拍照之后刷新图库信息
     */
    override fun refreshMedia(@AlbumResultType type: Int) {
        disconnectMediaScanner()
        singleMediaScanner = SingleMediaScanner(mActivity,
                FileUtils.getScannerFile(if (type == AlbumConstant.TYPE_RESULT_CAMERA)
                    imagePath.path
                else
                    uCropImagePath.path),
                this@AlbumFragment, type)
    }

    /**
     * 点击预览按钮进入预览页
     */
    override fun multiplePreview() {
        val albumEntityList = albumAdapter.getMultiplePreviewList()
        if (albumEntityList.isEmpty()) {
            Album.instance.albumListener.onAlbumBottomPreviewNull()
            return
        }
        val bundle = Bundle()
        bundle.putParcelableArrayList(AlbumConstant.PREVIEW_KEY, albumEntityList)
        bundle.putString(AlbumConstant.PREVIEW_BUCKET_ID, AlbumConstant.PREVIEW_BUTTON_KEY)
        startActivityForResult(Intent(mActivity, if (Album.instance.previewClass == null) PreviewActivity::class.java else Album.instance.previewClass).putExtras(bundle),
                AlbumConstant.TYPE_PREVIEW_CODE)
    }

    /**
     * 获取多选的数据
     */
    override fun multipleSelect() {
        val albumEntityList = albumAdapter.getMultiplePreviewList()
        if (albumEntityList.isEmpty()) {
            Album.instance.albumListener.onAlbumBottomSelectNull()
            return
        }
        Album.instance.albumListener.onAlbumResources(albumEntityList)
        mActivity.finish()
    }

    /**
     * 预览页之后的回调
     * 更新数据或者finish掉当前页
     */
    override fun onResultPreview(bundle: Bundle) {
        val previewAlbumEntity = bundle.getParcelableArrayList<AlbumEntity>(AlbumConstant.PREVIEW_KEY)
        val isRefreshUI = bundle.getBoolean(AlbumConstant.PREVIEW_REFRESH_UI, true)
        val isFinish = bundle.getBoolean(AlbumConstant.PREVIEW_FINISH, false)
        if (isFinish) {
            mActivity.finish()
            return
        }
        if (!isRefreshUI) {
            return
        }
        if (previewAlbumEntity == null) {
            return
        }
        multipleAlbumEntity = previewAlbumEntity
        albumPresenter.mergeSelectEntity(albumAdapter.getAlbumList(), previewAlbumEntity)
        albumAdapter.setMultiplePreviewList(previewAlbumEntity)
    }

    /**
     * 横竖屏保存数据
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(AlbumConstant.TYPE_ALBUM_STATE_SELECT, albumAdapter.getMultiplePreviewList())
        outState.putString(AlbumConstant.TYPE_ALBUM_STATE_BUCKET_ID, bucketId)
        outState.putString(AlbumConstant.TYPE_ALBUM_STATE_FINDER_NAME, finderName)
    }


    /**
     * recyclerView上拉刷新
     */
    override fun onLoadMore() {
        if (PermissionUtils.storage(this) && !albumPresenter.getScanLoading()) {
            albumPresenter.scan(bucketId, page, albumConfig.count)
        }
    }


    /**
     * 权限被允许
     */
    override fun permissionsGranted(type: Int) {
        when (type) {
            AlbumConstant.TYPE_PERMISSIONS_ALBUM -> onScanAlbum(bucketId, false, false)
            AlbumConstant.TYPE_PERMISSIONS_CAMERA -> openCamera()
        }
    }

    /**
     * 权限被拒
     */
    override fun permissionsDenied(type: Int) {
        Album.instance.albumListener.onAlbumPermissionsDenied(type)
        if (albumConfig.isPermissionsDeniedFinish) {
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
    override fun getLayoutId(): Int = R.layout.album_fragment_album
}
