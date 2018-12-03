package com.album.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.viewpager.widget.ViewPager
import com.album.*
import com.album.presenter.PreviewPresenter
import com.album.presenter.impl.PreviewPresenterImpl
import com.album.ui.ExtendedViewPager
import com.album.ui.adapter.PreviewAdapter
import com.album.ui.view.PrevView
import com.album.util.PermissionUtils
import com.album.util.fileExists
import com.album.util.scan.SCAN_ALL

/**
 *  @author y
 */
class PrevFragment : AlbumBaseFragment(), PrevView {

    companion object {
        fun newInstance(bundle: Bundle): PrevFragment {
            return PrevFragment().apply { arguments = bundle }
        }
    }

    lateinit var albumParentListener: AlbumPreviewParentListener
    private lateinit var adapter: PreviewAdapter
    private lateinit var appCompatCheckBox: AppCompatCheckBox
    private lateinit var progressBar: ProgressBar
    private lateinit var viewPager: ExtendedViewPager
    private lateinit var rootView: FrameLayout
    private lateinit var previewPresenter: PreviewPresenter

    private lateinit var albumBundle: AlbumBundle
    /** 全部数据 **/
    private lateinit var albumEntityList: ArrayList<AlbumEntity>
    /** 选中的数据 **/
    private lateinit var selectList: ArrayList<AlbumEntity>

    /** 是否是点击预览按钮进入预览界面 **/
    private var isPreview: Boolean = false
    /** 如果是点击item进入,则为当前的选中页position **/
    private var selectPosition: Int = 0
    /** bucketId,如果是点击全部页进入则为Null,如果是点击按钮进入则为[TYPE_PREVIEW_BUTTON_KEY] **/
    private var bucketId: String = ""

    /**
     * 获取 bucketId 和 isPreview 的值,因为这两个值在初始化之后就是固定的了
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        albumBundle = bundle.getParcelable(EXTRA_ALBUM_OPTIONS) ?: AlbumBundle()
        bucketId = bundle.getString(TYPE_PREVIEW_BUCKET_ID, "")
        isPreview = TextUtils.equals(bucketId, TYPE_PREVIEW_BUTTON_KEY)
    }

    /**
     * 初始化view
     */
    override fun initView(view: View) {
        rootView = view.findViewById(R.id.preview_root_view)
        viewPager = view.findViewById(R.id.preview_viewPager)
        appCompatCheckBox = view.findViewById(R.id.preview_check_box)
        progressBar = view.findViewById(R.id.preview_progress)
        appCompatCheckBox.setOnClickListener { checkBoxClick() }
    }

    /**
     *初始化
     * [albumEntityList]:只有在横竖屏切换并且是预览按钮进入的情况下才会初始化,否则[albumEntityList]会在扫描数据库之后获得，所以
     * 在横竖屏保存数据的时候，非预览按钮的情况下可以存Null
     * [selectList]
     * [selectPosition]
     */
    override fun initActivityCreated(savedInstanceState: Bundle?) {
        appCompatCheckBox.setBackgroundResource(albumBundle.checkBoxDrawable)
        albumEntityList = ArrayList()
        val previewBundle = savedInstanceState ?: bundle
        selectList = previewBundle.getParcelableArrayList<AlbumEntity>(TYPE_PREVIEW_KEY) ?: ArrayList()
        selectPosition = previewBundle.getInt(TYPE_PREVIEW_POSITION_KEY)
        if (savedInstanceState != null && isPreview) {
            albumEntityList = previewBundle.getParcelableArrayList<AlbumEntity>(TYPE_PREVIEW_STATE_SELECT_ALL) ?: ArrayList()
        }
        previewPresenter = PreviewPresenterImpl(this, albumBundle)
        albumParentListener.onChangedCount(selectList.size)
        if (PermissionUtils.storage(this)) {
            initPreview()
        }
    }

    /**
     * 权限被允许
     */
    override fun permissionsGranted(type: Int) {
        when (type) {
            TYPE_PERMISSIONS_ALBUM -> initPreview()
        }
    }

    /**
     * 权限被拒
     */
    override fun permissionsDenied(type: Int) {
        Album.instance.albumListener?.onAlbumPermissionsDenied(type)
        if (albumBundle.permissionsDeniedFinish) {
            mActivity.finish()
        }
    }

    /**
     * 如果不是预览按钮进入则直接扫描全部数据
     * 如果是,获取的[selectList]就相当于扫描之后的[albumEntityList]
     */
    private fun initPreview() {
        if (!isPreview) {
            previewPresenter.startScan(bucketId, -1, SCAN_ALL)
            return
        }
        if (albumEntityList.isEmpty()) {
            albumEntityList.addAll(selectList)
        }
        initViewPager(albumEntityList)
    }

    /**
     * 扫描成功,合并一下扫描到的数据和已选中的数据,然后初始化[ViewPager]
     */
    override fun scanSuccess(albumEntityList: ArrayList<AlbumEntity>) {
        previewPresenter.mergeEntity(albumEntityList, selectList)
        initViewPager(albumEntityList)
    }


    /**
     * 横竖屏切换保存
     * [selectList]
     * [albumEntityList]
     * [selectPosition]
     * 这里需要注意的是,如果是预览界面进入的话，[albumEntityList]不需要保存
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(TYPE_PREVIEW_KEY, selectList)
        outState.putParcelableArrayList(TYPE_PREVIEW_STATE_SELECT_ALL, if (isPreview) albumEntityList else null)
        outState.putInt(TYPE_PREVIEW_POSITION_KEY, selectPosition)
    }


    /**
     * 这里有两个参数
     * [isRefresh]:按下back或者toolbar的finish是否在上级页面刷新数据
     * [isFinish]:选中之后是否销毁上级activity
     */
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
        albumParentListener.onChangedCount(selectList.size)
    }

    private fun initViewPager(albumEntityList: ArrayList<AlbumEntity>) {
        adapter = PreviewAdapter(albumEntityList)
        viewPager.adapter = adapter
        viewPager.currentItem = selectPosition
        viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (!fileExists(adapter.getAlbumPath(position))) {
                    Album.instance.albumListener?.onAlbumPreviewFileNotExist()
                }
                appCompatCheckBox.isChecked = albumEntityList[position].isCheck
                selectPosition = position
                albumParentListener.onChangedToolbarCount(position + 1, albumEntityList.size)
            }
        })
        albumParentListener.onChangedToolbarCount(viewPager.currentItem + 1, albumEntityList.size)
        appCompatCheckBox.isChecked = albumEntityList[viewPager.currentItem].isCheck
    }

    fun getSelectEntity(): ArrayList<AlbumEntity> = selectList

    override fun initCreate(savedInstanceState: Bundle?) {}

    override fun getPreViewActivity(): Activity = mActivity

    override fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    override fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    override fun getLayoutId(): Int = R.layout.album_fragment_preview
}