package com.album.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager
import com.album.*
import com.album.core.scan.AlbumEntity
import com.album.core.AlbumFile.fileExists
import com.album.core.AlbumPermission.TYPE_PERMISSIONS_ALBUM
import com.album.core.AlbumPermission.permissionStorage
import com.album.core.scan.AlbumPreviewScanImpl
import com.album.core.ui.AlbumBaseFragment
import com.album.core.view.AlbumPreViewView
import com.album.listener.AlbumPreviewParentListener
import com.album.ui.adapter.PreviewAdapter
import com.album.widget.ExtendedViewPager

/**
 *  @author y
 */
class PrevFragment : AlbumBaseFragment(), AlbumPreViewView {

    companion object {
        fun newInstance(bundle: Bundle): PrevFragment {
            return PrevFragment().apply { arguments = bundle }
        }
    }

    var albumParentListener: AlbumPreviewParentListener? = null

    private lateinit var adapter: PreviewAdapter
    private lateinit var appCompatCheckBox: AppCompatCheckBox
    private lateinit var progressBar: ProgressBar
    private lateinit var viewPager: ExtendedViewPager
    private lateinit var rootView: FrameLayout
    private lateinit var previewPresenter: AlbumPreviewScanImpl

    private lateinit var albumBundle: AlbumBundle

    private lateinit var albumList: ArrayList<AlbumEntity>
    private lateinit var selectList: ArrayList<AlbumEntity>

    private var preview: Boolean = false
    private var selectPosition: Int = 0
    private var bucketId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        albumBundle = bundle.getParcelable(EXTRA_ALBUM_OPTIONS) ?: AlbumBundle()
        bucketId = bundle.getString(TYPE_PREVIEW_BUCKET_ID, "")
        preview = TextUtils.equals(bucketId, TYPE_PREVIEW_BUTTON_KEY)
    }

    override fun initView(view: View) {
        rootView = view.findViewById(R.id.preview_root_view)
        viewPager = view.findViewById(R.id.preview_viewPager)
        appCompatCheckBox = view.findViewById(R.id.preview_check_box)
        progressBar = view.findViewById(R.id.preview_progress)
        appCompatCheckBox.setOnClickListener { checkBoxClick() }
    }

    override fun initActivityCreated(savedInstanceState: Bundle?) {
        albumList = ArrayList()
        appCompatCheckBox.setBackgroundResource(albumBundle.checkBoxDrawable)
        val previewBundle = savedInstanceState ?: bundle

        selectList = previewBundle.getParcelableArrayList<AlbumEntity>(TYPE_PREVIEW_KEY)
                ?: ArrayList()
        selectPosition = previewBundle.getInt(TYPE_PREVIEW_POSITION_KEY)

        if (savedInstanceState != null && preview) {
            albumList = previewBundle.getParcelableArrayList<AlbumEntity>(TYPE_PREVIEW_STATE_SELECT_ALL)
                    ?: ArrayList()
        }

        albumParentListener?.onChangedCount(selectList.size)

        if (permissionStorage()) {
            initPreview()
        }
    }

    override fun permissionsGranted(type: Int) {
        when (type) {
            TYPE_PERMISSIONS_ALBUM -> initPreview()
        }
    }

    override fun permissionsDenied(type: Int) {
        Album.instance.albumListener?.onAlbumPermissionsDenied(type)
        if (albumBundle.permissionsDeniedFinish) {
            mActivity.finish()
        }
    }

    private fun initPreview() {
        if (!preview) {
            previewPresenter = AlbumPreviewScanImpl.newInstance(this, albumBundle.filterImg, selectList, bucketId)
            return
        }
        if (albumList.isEmpty()) {
            albumList.addAll(selectList)
        }
        initViewPager(albumList)
    }

    override fun scanSuccess(entityList: ArrayList<AlbumEntity>) {
        initViewPager(entityList)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(TYPE_PREVIEW_KEY, selectList)
        outState.putParcelableArrayList(TYPE_PREVIEW_STATE_SELECT_ALL, if (preview) albumList else null)
        outState.putInt(TYPE_PREVIEW_POSITION_KEY, selectPosition)
    }

    fun isRefreshAlbumUI(isRefresh: Boolean, isFinish: Boolean) {
        val bundle = Bundle()
        bundle.putParcelableArrayList(TYPE_PREVIEW_KEY, selectList)
        bundle.putBoolean(TYPE_PREVIEW_REFRESH_UI, isRefresh)
        bundle.putBoolean(TYPE_PREVIEW_FINISH, isFinish)
        val intent = Intent()
        intent.putExtras(bundle)
        mActivity.setResult(Activity.RESULT_OK, intent)
        mActivity.finish()
    }

    private fun checkBoxClick() {
        val albumEntity = adapter.getAlbumEntity(viewPager.currentItem)
        if (!selectList.contains(albumEntity) && selectList.size >= albumBundle.multipleMaxCount) {
            appCompatCheckBox.isChecked = false
            Album.instance.albumListener?.onAlbumMaxCount()
            return
        }
        if (albumEntity.isCheck) {
            selectList.remove(albumEntity)
            albumEntity.isCheck = false
        } else {
            albumEntity.isCheck = true
            selectList.add(albumEntity)
        }
        Album.instance.albumListener?.onCheckBoxAlbum(selectList.size, albumBundle.multipleMaxCount)
        albumParentListener?.onChangedCount(selectList.size)
    }

    private fun initViewPager(albumEntityList: ArrayList<AlbumEntity>) {
        adapter = PreviewAdapter(albumEntityList)
        viewPager.adapter = adapter
        viewPager.currentItem = selectPosition
        viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (!adapter.getAlbumPath(position).fileExists()) {
                    Album.instance.albumListener?.onAlbumPreviewFileNotExist()
                }
                appCompatCheckBox.isChecked = albumEntityList[position].isCheck
                selectPosition = position
                albumParentListener?.onChangedToolbarCount(position + 1, albumEntityList.size)
            }
        })
        albumParentListener?.onChangedToolbarCount(viewPager.currentItem + 1, albumEntityList.size)
        appCompatCheckBox.isChecked = albumEntityList[viewPager.currentItem].isCheck
    }

    fun getSelectEntity(): ArrayList<AlbumEntity> = selectList

    override fun initCreate(savedInstanceState: Bundle?) {}

    override fun getPrevContext(): FragmentActivity = mActivity

    override val layoutId: Int = R.layout.album_fragment_preview

    override fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    override fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }
}