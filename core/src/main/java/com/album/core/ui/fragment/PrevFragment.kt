package com.album.core.ui.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.album.core.*
import com.album.core.action.AlbumPreAction
import com.album.core.ext.fileExists
import com.album.core.ext.mergeEntity
import com.album.core.ui.adapter.PrevAdapter
import com.album.core.ui.base.AlbumBaseFragment
import com.gallery.scan.ScanEntity
import kotlinx.android.synthetic.main.album_fragment_preview.*

class PrevFragment : AlbumBaseFragment() {

    companion object {

        private fun newInstance(bundle: Bundle): PrevFragment = PrevFragment().apply { arguments = bundle }

        fun newInstance(
                albumBundle: AlbumBundle,
                position: Int,
                selectList: ArrayList<ScanEntity>,
                allList: ArrayList<ScanEntity>) = newInstance(Bundle().apply {
            putParcelable(AlbumConst.EXTRA_ALBUM_OPTIONS, albumBundle)
            putInt(AlbumConst.TYPE_PRE_POSITION, position)
            putParcelableArrayList(AlbumConst.TYPE_PRE_SELECT, selectList)
            putParcelableArrayList(AlbumConst.TYPE_PRE_ALL, allList)
        })
    }

    private var albumPreAction: AlbumPreAction? = null
    private var pageChangeCallback: ViewPager2.OnPageChangeCallback? = null

    private lateinit var adapter: PrevAdapter
    private lateinit var albumBundle: AlbumBundle
    private var currentPos: Int = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment is AlbumPreAction) {
            albumPreAction = parentFragment as AlbumPreAction
        } else if (context is AlbumPreAction) {
            albumPreAction = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        albumBundle = bundle.getParcelable(AlbumConst.EXTRA_ALBUM_OPTIONS) ?: AlbumBundle()
        currentPos = (savedInstanceState ?: bundle).getInt(AlbumConst.TYPE_PRE_POSITION, 0)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(AlbumConst.TYPE_PRE_POSITION, currentPos)
        outState.putParcelableArrayList(AlbumConst.TYPE_PRE_SELECT, adapter.multipleList)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = PrevAdapter()
        preViewPager.adapter = adapter
        preCheckBox.setBackgroundResource(albumBundle.checkBoxDrawable)
        preCheckBox.setOnClickListener { checkBoxClick() }
        preRootView.setBackgroundColor(ContextCompat.getColor(mActivity, albumBundle.prevPhotoBackgroundColor))
        adapter.multipleList = (savedInstanceState
                ?: bundle).getParcelableArrayList(AlbumConst.TYPE_PRE_SELECT) ?: ArrayList()
        adapter.addAll(bundle.getParcelableArrayList<ScanEntity>(AlbumConst.TYPE_PRE_ALL)?.filter { it.path != AlbumInternalConst.CAMERA } as ArrayList<ScanEntity>)
        adapter.albumList.mergeEntity(adapter.multipleList)
        preViewPager.setCurrentItem(currentPos, false)
        pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (!adapter.albumList[position].path.fileExists()) {
                    Album.instance.albumListener?.onAlbumPreFileNotExist()
                }
                currentPos = position
                preCheckBox.isChecked = getCurrentItem().isCheck
                albumPreAction?.onChangedViewPager(position + 1, adapter.albumList.size)
            }
        }.also { preViewPager.registerOnPageChangeCallback(it) }
        albumPreAction?.onChangedViewPager(preViewPager.currentItem + 1, adapter.albumList.size)
        albumPreAction?.onChangedCheckBoxCount(getSelectEntity().size)
        preCheckBox.isChecked = adapter.albumList[preViewPager.currentItem].isCheck
    }

    fun isRefreshAlbumUI(isRefresh: Boolean, isFinish: Boolean) {
        val intent = Intent()
        intent.putExtras(resultBundle(isRefresh, isFinish))
        mActivity.setResult(Activity.RESULT_OK, intent)
        mActivity.finish()
    }

    fun isRefreshAlbumUI(isRefresh: Boolean) = resultBundle(isRefresh, false)

    private fun resultBundle(isRefresh: Boolean, isFinish: Boolean): Bundle {
        val bundle = Bundle()
        bundle.putParcelableArrayList(AlbumConst.TYPE_PRE_SELECT, getSelectEntity())
        bundle.putBoolean(AlbumInternalConst.TYPE_PRE_REFRESH_UI, isRefresh)
        bundle.putBoolean(AlbumInternalConst.TYPE_PRE_DONE_FINISH, isFinish)
        return bundle
    }

    private fun checkBoxClick() {
        val albumEntity = adapter.albumList[preViewPager.currentItem]
        if (!albumEntity.path.fileExists()) {
            preCheckBox.isChecked = false
            if (getSelectEntity().contains(albumEntity)) {
                getSelectEntity().remove(albumEntity)
            }
            Album.instance.albumListener?.onAlbumCheckFileNotExist()
            return
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
        albumPreAction?.onChangedCheckBoxCount(getSelectEntity().size)
    }

    fun getCurrentItem(): ScanEntity = adapter.albumList[currentPos]

    fun getAllList(): ArrayList<ScanEntity> = adapter.albumList

    fun getSelectEntity(): ArrayList<ScanEntity> = adapter.multipleList

    fun setCurrentItem(position: Int) = let { preViewPager.setCurrentItem(position, false) }

    override fun permissionsGranted(type: Int) = Unit

    override fun permissionsDenied(type: Int) = Unit

    override val layoutId: Int = R.layout.album_fragment_preview

    override fun onDestroyView() {
        super.onDestroyView()
        pageChangeCallback?.let { preViewPager.unregisterOnPageChangeCallback(it) }
    }
}