package com.album.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.album.*
import com.album.core.AlbumScan
import com.album.core.fileExists
import com.album.core.permissionStorage
import com.album.core.scan.AlbumEntity
import com.album.core.scan.AlbumScanPreviewImpl
import com.album.core.ui.AlbumBaseFragment
import com.album.core.view.AlbumPreView
import com.album.listener.AlbumPreParentListener
import com.album.listener.OnAlbumPrevItemClickListener
import com.album.ui.adapter.AlbumPrevAdapter
import kotlinx.android.synthetic.main.album_fragment_preview.*

/**
 *  @author y
 */
class PrevFragment : AlbumBaseFragment(), AlbumPreView, OnAlbumPrevItemClickListener {

    companion object {

        @JvmStatic
        fun newInstance(bundle: Bundle): PrevFragment = PrevFragment().apply { arguments = bundle }

        /**
         * 获取预览的fragment,参数里面应该包含
         * [AlbumConst.EXTRA_ALBUM_OPTIONS] AlbumBundle
         * [TYPE_PREVIEW_PARENT] 预览扫描的parent,如果为 ALL_PARENT 是预览而不是点击进入
         * [TYPE_PREVIEW_POSITION_KEY] 扫描图片成功之后需要定位的位置
         * [TYPE_PREVIEW_KEY] 跳转预览页面时携带选中数据的key
         * 可参考自定义UI
         */
        @JvmStatic
        fun newInstance(albumBundle: AlbumBundle, position: Int, parent: Long, previewList: ArrayList<AlbumEntity>) = newInstance(Bundle().apply {
            putParcelableArrayList(TYPE_PREVIEW_KEY, previewList)
            putInt(TYPE_PREVIEW_POSITION_KEY, position)
            putLong(TYPE_PREVIEW_PARENT, parent)
            putParcelable(AlbumConst.EXTRA_ALBUM_OPTIONS, albumBundle)
        })

    }

    /**
     * UI使用回调,可能会用到
     */
    var albumParentListener: AlbumPreParentListener? = null

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
        albumBundle = bundle.getParcelable(AlbumConst.EXTRA_ALBUM_OPTIONS) ?: AlbumBundle()
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
        preViewPager.adapter = adapter

        val selectList: ArrayList<AlbumEntity> = ArrayList()

        selectList.addAll(savedInstanceState?.getParcelableArrayList(TYPE_PREVIEW_STATE_SELECT_ALL)
                ?: bundle.getParcelableArrayList(TYPE_PREVIEW_KEY) ?: ArrayList())

        if (selectList.isNotEmpty()) {
            adapter.multipleList = selectList
        }

        if (parent == AlbumScan.PREV_PARENT) {
            adapter.addAll(bundle.getParcelableArrayList<AlbumEntity>(TYPE_PREVIEW_KEY)
                    ?: ArrayList())
        } else if (savedInstanceState != null) {
            adapter.addAll(savedInstanceState.getParcelableArrayList<AlbumEntity>(TYPE_PREVIEW_KEY)
                    ?: ArrayList())
        }

        preCheckBox.setBackgroundResource(albumBundle.checkBoxDrawable)
        preCheckBox.setOnClickListener { checkBoxClick() }

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
        presenterPreview = AlbumScanPreviewImpl.newInstance(this)
    }

    override fun scanSuccess(arrayList: ArrayList<AlbumEntity>) {
        adapter.removeAll()
        adapter.addAll(arrayList)
        preViewPager.setCurrentItem(currentPosition, false)
        prevOnPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (!adapter.albumList[position].path.fileExists()) {
                    Album.instance.albumListener?.onAlbumPreviewFileNotExist()
                }
                currentPosition = position
                preCheckBox.isChecked = adapter.albumList[position].isCheck
                albumParentListener?.onChangedViewPager(position + 1, adapter.albumList.size)
            }
        }
        prevOnPageChangeCallback?.let { preViewPager.registerOnPageChangeCallback(it) }
        albumParentListener?.onChangedViewPager(preViewPager.currentItem + 1, adapter.albumList.size)
        albumParentListener?.onChangedCheckBoxCount(getSelectEntity().size)
        preCheckBox.isChecked = adapter.albumList[preViewPager.currentItem].isCheck
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
        val albumEntity = adapter.albumList[preViewPager.currentItem]

        if (!albumEntity.path.fileExists()) {
            preCheckBox.isChecked = false
            Album.instance.albumListener?.onAlbumCheckFileNotExist()
        }

        if (!getSelectEntity().contains(albumEntity) && getSelectEntity().size >= albumBundle.multipleMaxCount) {
            preCheckBox.isChecked = false
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

    override fun getSelectEntity(): ArrayList<AlbumEntity> = adapter.multipleList

    fun getCurrentEntity(): AlbumEntity = adapter.albumList[currentPosition]

    override fun getParentId(): Long = parent

    override fun currentScanType(): Int = albumBundle.scanType

    fun getAlbumList(): ArrayList<AlbumEntity> = adapter.albumList

    fun setCurrentItem(position: Int) {
        preViewPager.setCurrentItem(position, false)
    }

    override fun getAllEntity(): ArrayList<AlbumEntity>? = if (parent == AlbumScan.PREV_PARENT || adapter.albumList.isNotEmpty()) adapter.albumList else null

    override fun getAlbumContext(): FragmentActivity = mActivity

    override val layoutId: Int = R.layout.album_fragment_preview

    override fun onDestroyView() {
        super.onDestroyView()
        prevOnPageChangeCallback?.let { preViewPager.unregisterOnPageChangeCallback(it) }
    }
}