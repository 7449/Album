package com.album.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager
import com.album.*
import com.album.core.AlbumFile.fileExists
import com.album.core.AlbumPermission.TYPE_PERMISSIONS_ALBUM
import com.album.core.AlbumPermission.permissionStorage
import com.album.core.AlbumView.hide
import com.album.core.AlbumView.show
import com.album.core.scan.AlbumEntity
import com.album.core.scan.AlbumScanPreviewImpl
import com.album.core.ui.AlbumBaseFragment
import com.album.core.view.AlbumPreViewView
import com.album.listener.AlbumPreviewParentListener
import com.album.ui.adapter.PreviewAdapter
import kotlinx.android.synthetic.main.album_fragment_preview.*

/**
 *  @author y
 */
class PrevFragment : AlbumBaseFragment(), AlbumPreViewView {

    companion object {
        /**
         * 获取预览的fragment,参数里面应该包含
         * [EXTRA_ALBUM_OPTIONS] AlbumBundle
         * [TYPE_PREVIEW_BUCKET_ID] 预览扫描的bucketId,如果为空则扫描整个相册
         * [TYPE_PREVIEW_POSITION_KEY] 扫描图片成功之后需要定位的位置
         * [TYPE_PREVIEW_KEY] 跳转预览页面时携带选中数据的key
         * 可参考自定义UI
         */
        fun newInstance(bundle: Bundle): PrevFragment {
            return PrevFragment().apply { arguments = bundle }
        }
    }

    /**
     * UI使用回调,可能会用到
     */
    var albumParentListener: AlbumPreviewParentListener? = null

    private lateinit var adapter: PreviewAdapter
    private lateinit var presenterPreview: AlbumScanPreviewImpl

    private lateinit var albumBundle: AlbumBundle

    /**
     * 如果是点击预览或者其他按钮进入的则为已选中的数据
     * 如果是点击Itm进入的则是按照bucketId扫描得来的数据
     */
    private var albumList: ArrayList<AlbumEntity> = ArrayList()

    /**
     * 选中的数据
     * 如果是点击预览或者其他按钮进入的则为全部
     * 如果是点击Itm进入的则需要合并
     */
    var selectList: ArrayList<AlbumEntity> = ArrayList()

    /**
     * 是否是点击预览进入的页面
     */
    private var preview: Boolean = false

    /**
     * 当前位置的position
     */
    private var currentPosition: Int = 0

    /**
     * 点击的图片的bucketId,为空的话则是扫描整个数据,如果是[PREVIEW_BUTTON_KEY]则不扫描直接显示
     */
    private var bucketId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        albumBundle = bundle.getParcelable(EXTRA_ALBUM_OPTIONS)
                ?: throw KotlinNullPointerException()
        bucketId = bundle.getString(TYPE_PREVIEW_BUCKET_ID, "")
        preview = TextUtils.equals(bucketId, PREVIEW_BUTTON_KEY)

        // albumBundle bucketId preview 这个几个变量直到销毁也不会变,所以先赋值
        //如果 savedInstanceState == null 则返回携带的Bundle，如果不等于null ，则是横竖屏切换了，要重新赋值一些保存的数据

        // selectList 是当前页选择的数据,如果是点击预览进入则为已选择的所有数据，如果不是,则有可能为空,但不会为Null
        // currentPosition 当前定位的position
        // albumList 全部数据,如果是点击预览页进入则初始化赋值为 selectList , 如果不是则是根据 bucketId 扫描到的所有数据，赋值是在 initPreview()
        // 所以如果 savedInstanceState == null 或者 不是预览进入 则直接跳过横竖屏导致的数据变化赋值
        // 如果是预览,则会在横竖屏切换时保存 albumList ， 就是保存起来上一个页面所有的选择数据，相当于刚进来没有变化的 selectList,如果不是,则不用保存,横竖屏切换直接重新扫描

        val previewBundle = savedInstanceState ?: bundle

        selectList = previewBundle.getParcelableArrayList<AlbumEntity>(TYPE_PREVIEW_KEY)
                ?: ArrayList()
        currentPosition = previewBundle.getInt(TYPE_PREVIEW_POSITION_KEY)

        savedInstanceState?.let {
            if (preview) {
                albumList = savedInstanceState.getParcelableArrayList<AlbumEntity>(TYPE_PREVIEW_STATE_SELECT_ALL)
                        ?: ArrayList()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        preview_check_box.setBackgroundResource(albumBundle.checkBoxDrawable)
        preview_check_box.setOnClickListener { checkBoxClick() }
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

    /**
     * 如果不是预览则直接扫描图库 [scanSuccess]
     * 这里 albumList 加个判空处理，是因为横竖屏切换时,已经重新赋值了 albumList 而且 selectList 有可能会变化
     */
    private fun initPreview() {
        if (!preview) {
            presenterPreview = AlbumScanPreviewImpl.newInstance(this, albumBundle.filterImg, selectList, bucketId)
            return
        }
        if (albumList.isEmpty()) {
            albumList.addAll(selectList)
        }
        scanSuccess(albumList)
    }

    override fun scanSuccess(entityList: ArrayList<AlbumEntity>) {
        initViewPager(entityList)
    }

    fun isRefreshAlbumUI(isRefresh: Boolean, isFinish: Boolean) {
        val bundle = Bundle()
        bundle.putParcelableArrayList(TYPE_PREVIEW_KEY, selectList)
        bundle.putBoolean(TYPE_PREVIEW_REFRESH_UI, isRefresh)
        bundle.putBoolean(TYPE_PREVIEW_SELECT_OK_FINISH, isFinish)
        val intent = Intent()
        intent.putExtras(bundle)
        mActivity.setResult(Activity.RESULT_OK, intent)
        mActivity.finish()
    }

    private fun checkBoxClick() {
        val albumEntity = adapter.getAlbumEntity(preview_viewPager.currentItem)
        if (!selectList.contains(albumEntity) && selectList.size >= albumBundle.multipleMaxCount) {
            preview_check_box.isChecked = false
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
        preview_viewPager.adapter = adapter
        preview_viewPager.currentItem = currentPosition
        preview_viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (!adapter.getAlbumPath(position).fileExists()) {
                    Album.instance.albumListener?.onAlbumPreviewFileNotExist()
                }
                preview_check_box.isChecked = albumEntityList[position].isCheck
                currentPosition = position
                albumParentListener?.onChangedToolbarCount(position + 1, albumEntityList.size)
            }
        })
        albumParentListener?.onChangedToolbarCount(preview_viewPager.currentItem + 1, albumEntityList.size)
        preview_check_box.isChecked = albumEntityList[preview_viewPager.currentItem].isCheck
        albumParentListener?.onChangedCount(selectList.size)
    }

    /**
     * 横竖屏切换时保存需要的数据
     * [TYPE_PREVIEW_KEY] 选择数据
     * [TYPE_PREVIEW_STATE_SELECT_ALL] 全部数据,只在 preview true 的时候有效
     * [TYPE_PREVIEW_POSITION_KEY] 当前 position
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(TYPE_PREVIEW_KEY, selectList)
        outState.putParcelableArrayList(TYPE_PREVIEW_STATE_SELECT_ALL, if (preview) albumList else null)
        outState.putInt(TYPE_PREVIEW_POSITION_KEY, currentPosition)
    }

    override fun getPrevContext(): FragmentActivity = mActivity

    override val layoutId: Int = R.layout.album_fragment_preview

    override fun hideProgress() = preview_progress.hide()

    override fun showProgress() = preview_progress.show()
}