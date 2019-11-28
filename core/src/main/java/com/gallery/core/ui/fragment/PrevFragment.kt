package com.gallery.core.ui.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.gallery.core.*
import com.gallery.core.action.GalleryPreAction
import com.gallery.core.ext.fileExists
import com.gallery.core.ext.mergeEntity
import com.gallery.core.ui.adapter.PrevAdapter
import com.gallery.core.ui.base.GalleryBaseFragment
import com.gallery.scan.ScanEntity
import kotlinx.android.synthetic.main.gallery_fragment_preview.*

class PrevFragment : GalleryBaseFragment() {

    companion object {

        private fun newInstance(bundle: Bundle): PrevFragment = PrevFragment().apply { arguments = bundle }

        fun newInstance(
                galleryBundle: GalleryBundle,
                position: Int,
                selectList: ArrayList<ScanEntity>,
                allList: ArrayList<ScanEntity>) = newInstance(Bundle().apply {
            putParcelable(GalleryConst.EXTRA_GALLERY_OPTIONS, galleryBundle)
            putInt(GalleryConst.TYPE_PRE_POSITION, position)
            putParcelableArrayList(GalleryConst.TYPE_PRE_SELECT, selectList)
            putParcelableArrayList(GalleryConst.TYPE_PRE_ALL, allList)
        })
    }

    private var galleryPreAction: GalleryPreAction? = null
    private var pageChangeCallback: ViewPager2.OnPageChangeCallback? = null

    private lateinit var adapter: PrevAdapter
    private lateinit var galleryBundle: GalleryBundle
    private var currentPos: Int = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment is GalleryPreAction) {
            galleryPreAction = parentFragment as GalleryPreAction
        } else if (context is GalleryPreAction) {
            galleryPreAction = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        galleryBundle = bundle.getParcelable(GalleryConst.EXTRA_GALLERY_OPTIONS) ?: GalleryBundle()
        currentPos = (savedInstanceState ?: bundle).getInt(GalleryConst.TYPE_PRE_POSITION, 0)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(GalleryConst.TYPE_PRE_POSITION, currentPos)
        outState.putParcelableArrayList(GalleryConst.TYPE_PRE_SELECT, adapter.multipleList)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = PrevAdapter()
        preViewPager.adapter = adapter
        preCheckBox.setBackgroundResource(galleryBundle.checkBoxDrawable)
        preCheckBox.setOnClickListener { checkBoxClick() }
        preRootView.setBackgroundColor(ContextCompat.getColor(mActivity, galleryBundle.prevPhotoBackgroundColor))
        adapter.multipleList = (savedInstanceState
                ?: bundle).getParcelableArrayList(GalleryConst.TYPE_PRE_SELECT) ?: ArrayList()
        adapter.addAll(bundle.getParcelableArrayList<ScanEntity>(GalleryConst.TYPE_PRE_ALL)?.filter { it.path != GalleryInternalConst.CAMERA } as ArrayList<ScanEntity>)
        adapter.galleryList.mergeEntity(adapter.multipleList)
        preViewPager.setCurrentItem(currentPos, false)
        pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (!adapter.galleryList[position].path.fileExists()) {
                    Gallery.instance.galleryListener?.onGalleryPreFileNotExist()
                }
                currentPos = position
                preCheckBox.isChecked = getCurrentItem().isCheck
                galleryPreAction?.onChangedViewPager(position + 1, adapter.galleryList.size)
            }
        }.also { preViewPager.registerOnPageChangeCallback(it) }
        galleryPreAction?.onChangedViewPager(preViewPager.currentItem + 1, adapter.galleryList.size)
        galleryPreAction?.onChangedCheckBoxCount(getSelectEntity().size)
        preCheckBox.isChecked = adapter.galleryList[preViewPager.currentItem].isCheck
    }

    fun isRefreshGalleryUI(isRefresh: Boolean, isFinish: Boolean) {
        val intent = Intent()
        intent.putExtras(resultBundle(isRefresh, isFinish))
        mActivity.setResult(Activity.RESULT_OK, intent)
        mActivity.finish()
    }

    fun isRefreshGalleryUI(isRefresh: Boolean) = resultBundle(isRefresh, false)

    private fun resultBundle(isRefresh: Boolean, isFinish: Boolean): Bundle {
        val bundle = Bundle()
        bundle.putParcelableArrayList(GalleryConst.TYPE_PRE_SELECT, getSelectEntity())
        bundle.putBoolean(GalleryInternalConst.TYPE_PRE_REFRESH_UI, isRefresh)
        bundle.putBoolean(GalleryInternalConst.TYPE_PRE_DONE_FINISH, isFinish)
        return bundle
    }

    private fun checkBoxClick() {
        val galleryEntity = adapter.galleryList[preViewPager.currentItem]
        if (!galleryEntity.path.fileExists()) {
            preCheckBox.isChecked = false
            if (getSelectEntity().contains(galleryEntity)) {
                getSelectEntity().remove(galleryEntity)
            }
            Gallery.instance.galleryListener?.onGalleryCheckFileNotExist()
            return
        }
        if (!getSelectEntity().contains(galleryEntity) && getSelectEntity().size >= galleryBundle.multipleMaxCount) {
            preCheckBox.isChecked = false
            Gallery.instance.galleryListener?.onGalleryMaxCount()
            return
        }
        if (galleryEntity.isCheck) {
            getSelectEntity().remove(galleryEntity)
            galleryEntity.isCheck = false
        } else {
            galleryEntity.isCheck = true
            getSelectEntity().add(galleryEntity)
        }
        Gallery.instance.galleryListener?.onGalleryCheckBox(getSelectEntity().size, galleryBundle.multipleMaxCount)
        galleryPreAction?.onChangedCheckBoxCount(getSelectEntity().size)
    }

    fun getCurrentItem(): ScanEntity = adapter.galleryList[currentPos]

    fun getAllList(): ArrayList<ScanEntity> = adapter.galleryList

    fun getSelectEntity(): ArrayList<ScanEntity> = adapter.multipleList

    fun setCurrentItem(position: Int) = let { preViewPager.setCurrentItem(position, false) }

    override fun permissionsGranted(type: Int) = Unit

    override fun permissionsDenied(type: Int) = Unit

    override val layoutId: Int = R.layout.gallery_fragment_preview

    override fun onDestroyView() {
        super.onDestroyView()
        pageChangeCallback?.let { preViewPager.unregisterOnPageChangeCallback(it) }
    }
}