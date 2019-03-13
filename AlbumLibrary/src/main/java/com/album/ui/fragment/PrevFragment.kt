package com.album.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.album.*
import com.album.core.fileExists
import com.album.core.hide
import com.album.core.permissionStorage
import com.album.core.scan.AlbumEntity
import com.album.core.scan.AlbumScan
import com.album.core.scan.AlbumScanPreviewImpl
import com.album.core.show
import com.album.core.ui.AlbumBaseFragment
import com.album.core.view.AlbumPreViewView
import com.album.listener.AlbumPreviewParentListener
import com.album.ui.adapter.AlbumPrevAdapter
import kotlinx.android.synthetic.main.album_fragment_preview.*

/**
 *  @author y
 */
class PrevFragment : AlbumBaseFragment(), AlbumPreViewView, AlbumPrevAdapter.OnAlbumPrevItemClickListener {

    companion object {
        /**
         * 获取预览的fragment,参数里面应该包含
         * [EXTRA_ALBUM_OPTIONS] AlbumBundle
         * [TYPE_PREVIEW_PARENT] 预览扫描的parent,如果为 ALL_PARENT 是预览而不是点击进入
         * [TYPE_PREVIEW_POSITION_KEY] 扫描图片成功之后需要定位的位置
         * [TYPE_PREVIEW_KEY] 跳转预览页面时携带选中数据的key
         * 可参考自定义UI
         */
        fun newInstance(bundle: Bundle): PrevFragment = PrevFragment().apply { arguments = bundle }
    }

    /**
     * UI使用回调,可能会用到
     */
    var albumParentListener: AlbumPreviewParentListener? = null

    private lateinit var adapter: AlbumPrevAdapter
    private lateinit var presenterPreview: AlbumScanPreviewImpl
    private var prevOnPageChangeCallback: ViewPager2.OnPageChangeCallback? = null

    private lateinit var albumBundle: AlbumBundle

    /**
     * 当前位置的position
     */
    private var currentPosition: Int = 0

    /**
     * 文件夹的parent,如果为 PREV_PARENT 则为预览
     */
    private var parent: Long = AlbumScan.ALL_PARENT

    /**
     *  albumBundle parent 这个几个变量直到销毁也不会变,所以先赋值
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        albumBundle = bundle.getParcelable(EXTRA_ALBUM_OPTIONS) ?: AlbumBundle()
        parent = bundle.getLong(TYPE_PREVIEW_PARENT, AlbumScan.ALL_PARENT)
        val previewBundle = savedInstanceState ?: bundle
        currentPosition = previewBundle.getInt(TYPE_PREVIEW_POSITION_KEY, 0)
    }

    /**
     * 横竖屏切换时保存需要的数据
     * [TYPE_PREVIEW_STATE_SELECT_ALL] 当前选择的全部数据
     * [TYPE_PREVIEW_POSITION_KEY] 当前 position
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(TYPE_PREVIEW_STATE_SELECT_ALL, getSelectEntity())
        outState.putParcelableArrayList(TYPE_PREVIEW_KEY, adapter.albumList)
        outState.putInt(TYPE_PREVIEW_POSITION_KEY, currentPosition)
    }

    /**
     * [savedInstanceState]优先级高于[bundle]
     * 如果是点击预览进入, albumList 一直都用 [TYPE_PREVIEW_KEY]获取，
     * 初始化时 selectList == albumList，使用[TYPE_PREVIEW_KEY]获取，横竖屏时 selectList 使用 [TYPE_PREVIEW_STATE_SELECT_ALL]获取
     * 反之
     * 初始化时只赋值 selectList,albumList数据库自己扫描, selectList 使用[TYPE_PREVIEW_KEY] 获取，
     * 横竖屏 selectList 使用 [TYPE_PREVIEW_STATE_SELECT_ALL]获取, albumList 数据库扫描
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = AlbumPrevAdapter(albumBundle, this)
        preview_viewPager.adapter = adapter

        val selectList: ArrayList<AlbumEntity> = ArrayList()

        selectList.addAll(savedInstanceState?.getParcelableArrayList(TYPE_PREVIEW_STATE_SELECT_ALL)
                ?: bundle.getParcelableArrayList(TYPE_PREVIEW_KEY) ?: ArrayList())

        if (!selectList.isEmpty()) {
            adapter.multipleList = selectList
        }

        if (parent == AlbumScan.PREV_PARENT) {
            adapter.addAll(bundle.getParcelableArrayList<AlbumEntity>(TYPE_PREVIEW_KEY)
                    ?: ArrayList())
        } else if (savedInstanceState != null) {
            adapter.addAll(savedInstanceState.getParcelableArrayList<AlbumEntity>(TYPE_PREVIEW_KEY)
                    ?: ArrayList())
        }

        preview_check_box.setBackgroundResource(albumBundle.checkBoxDrawable)
        preview_check_box.setOnClickListener { checkBoxClick() }

        if (permissionStorage()) {
            initPreview()
        }
    }

    override fun permissionsGranted(type: Int) {
        initPreview()
    }

    override fun permissionsDenied(type: Int) {
        Album.instance.albumListener?.onAlbumPermissionsDenied(type)
        if (albumBundle.permissionsDeniedFinish) {
            mActivity.finish()
        }
    }

    private fun initPreview() {
        presenterPreview = AlbumScanPreviewImpl.newInstance(this, adapter.multipleList, if (parent == AlbumScan.PREV_PARENT || !adapter.albumList.isEmpty()) adapter.albumList else null, parent, albumBundle.scanType)
    }

    override fun scanSuccess(albumEntityList: ArrayList<AlbumEntity>) {
        adapter.removeAll()
        adapter.addAll(albumEntityList)
        preview_viewPager.setCurrentItem(currentPosition, false)
        prevOnPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (!adapter.albumList[position].path.fileExists()) {
                    Album.instance.albumListener?.onAlbumPreviewFileNotExist()
                }
                currentPosition = position
                preview_check_box.isChecked = adapter.albumList[position].isCheck
                albumParentListener?.onChangedViewPager(position + 1, adapter.albumList.size)
            }
        }
        prevOnPageChangeCallback?.let { preview_viewPager.registerOnPageChangeCallback(it) }
        albumParentListener?.onChangedViewPager(preview_viewPager.currentItem + 1, adapter.albumList.size)
        albumParentListener?.onChangedCheckBoxCount(getSelectEntity().size)
        preview_check_box.isChecked = adapter.albumList[preview_viewPager.currentItem].isCheck
    }

    /**
     * 不适用于Dialog
     */
    fun isRefreshAlbumUI(isRefresh: Boolean, isFinish: Boolean) {
        val intent = Intent()
        intent.putExtras(resultBundle(isRefresh, isFinish))
        mActivity.setResult(Activity.RESULT_OK, intent)
        mActivity.finish()
    }

    /**
     * 适用于Dialog
     */
    fun isDialogRefreshAlbumUI(isRefresh: Boolean) = resultBundle(isRefresh, false)

    private fun resultBundle(isRefresh: Boolean, isFinish: Boolean): Bundle {
        val bundle = Bundle()
        bundle.putParcelableArrayList(TYPE_PREVIEW_KEY, getSelectEntity())
        bundle.putBoolean(TYPE_PREVIEW_REFRESH_UI, isRefresh)
        bundle.putBoolean(TYPE_PREVIEW_SELECT_OK_FINISH, isFinish)
        return bundle
    }

    private fun checkBoxClick() {
        val albumEntity = adapter.albumList[preview_viewPager.currentItem]

        if (!albumEntity.path.fileExists()) {
            preview_check_box.isChecked = false
            Album.instance.albumListener?.onAlbumCheckFileNotExist()
        }

        if (!getSelectEntity().contains(albumEntity) && getSelectEntity().size >= albumBundle.multipleMaxCount) {
            preview_check_box.isChecked = false
            Album.instance.albumListener?.onAlbumMaxCount()
            return
        }
        if (albumEntity.isCheck) {
            getSelectEntity().remove(albumEntity)
            albumEntity.isCheck = false
        } else {
            albumEntity.isCheck = true
            getSelectEntity().add(albumEntity)
        }
        Album.instance.albumListener?.onAlbumCheckBox(getSelectEntity().size, albumBundle.multipleMaxCount)
        albumParentListener?.onChangedCheckBoxCount(getSelectEntity().size)
    }

    override fun onItemCheckBoxClick(view: View, currentMaxCount: Int, albumEntity: AlbumEntity) {
        albumParentListener?.onChangedCheckBoxCount(currentMaxCount)
    }

    override fun onItemClick(view: View, position: Int, albumEntity: AlbumEntity) {
    }

    fun getSelectEntity(): ArrayList<AlbumEntity> = adapter.multipleList

    fun getCurrentEntity(): AlbumEntity = adapter.albumList[currentPosition]

    fun getAlbumList(): ArrayList<AlbumEntity> = adapter.albumList

    fun setCurrentItem(position: Int) {
        preview_viewPager.setCurrentItem(position, false)
    }

    override fun getAlbumContext(): FragmentActivity = mActivity

    override val layoutId: Int = R.layout.album_fragment_preview

    override fun showProgress() {
        if (albumBundle.showProgress) {
            preview_progress.show()
        }
    }

    override fun hideProgress() {
        if (albumBundle.showProgress) {
            preview_progress.hide()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        prevOnPageChangeCallback?.let { preview_viewPager.unregisterOnPageChangeCallback(it) }
    }
}