package com.gallery.core.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.kotlin.expand.app.moveToNextToIdExpand
import androidx.kotlin.expand.os.*
import androidx.viewpager2.widget.ViewPager2
import com.gallery.core.GalleryBundle
import com.gallery.core.GalleryConfig
import com.gallery.core.callback.IGalleryImageLoader
import com.gallery.core.callback.IGalleryPrev
import com.gallery.core.callback.IGalleryPrevCallback
import com.gallery.core.callback.IGalleryPrevInterceptor
import com.gallery.core.expand.externalUri
import com.gallery.core.expand.galleryImageLoaderExpand
import com.gallery.core.expand.galleryPrevCallbackExpand
import com.gallery.core.expand.galleryPrevInterceptorExpand
import com.gallery.core.ui.adapter.PrevAdapter
import com.gallery.core.ui.base.GalleryBaseFragment
import com.gallery.scan.ScanEntity
import com.gallery.scan.ScanImpl
import com.gallery.scan.ScanType
import com.gallery.scan.ScanView
import kotlinx.android.synthetic.main.gallery_fragment_preview.*

class PrevFragment : GalleryBaseFragment(com.gallery.core.R.layout.gallery_fragment_preview), IGalleryPrev {

    companion object {
        fun newInstance(parentId: Long,
                        selectList: ArrayList<ScanEntity> = ArrayList(),
                        config: GalleryBundle = GalleryBundle(),
                        position: Int = 0): PrevFragment {
            val prevFragment = PrevFragment()
            val bundle = Bundle()
            bundle.putParcelable(GalleryConfig.PREV_CONFIG, config)
            bundle.putParcelableArrayList(GalleryConfig.PREV_START_SELECT, selectList)
            bundle.putLong(GalleryConfig.PREV_PARENT_ID, parentId)
            bundle.putInt(GalleryConfig.PREV_START_POSITION, position)
            prevFragment.arguments = bundle
            return prevFragment
        }
    }

    private val galleryPrevInterceptor: IGalleryPrevInterceptor by lazy { galleryPrevInterceptorExpand }
    private val galleryImageLoader: IGalleryImageLoader by lazy { galleryImageLoaderExpand }
    private val galleryPrevCallback: IGalleryPrevCallback by lazy { galleryPrevCallbackExpand }
    private val pageChangeCallback: ViewPager2.OnPageChangeCallback by lazy {
        object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                galleryPrevCallback.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageScrollStateChanged(state: Int) {
                galleryPrevCallback.onPageScrollStateChanged(state)
            }

            override fun onPageSelected(position: Int) {
                galleryPrevCallback.onPageSelected(position)
                if (!galleryPrevInterceptor.hideCheckBox) {
                    preCheckBox.isSelected = isCheckBox(position)
                }
            }
        }
    }
    private val prevAdapter: PrevAdapter by lazy { PrevAdapter { entity, container -> galleryImageLoader.onDisplayGalleryPrev(entity, container) } }
    private val galleryBundle by lazy { getParcelableOrDefault<GalleryBundle>(GalleryConfig.PREV_CONFIG, GalleryBundle()) }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(GalleryConfig.PREV_START_POSITION, currentPosition)
        outState.putParcelableArrayList(GalleryConfig.PREV_START_SELECT, selectEntities)
    }

    private fun updateEntity(arrayList: ArrayList<ScanEntity>, savedInstanceState: Bundle?) {
        prevAdapter.addAll(arrayList)
        prevAdapter.addSelectAll((savedInstanceState
                ?: bundleOrEmptyExpand()).getParcelableArrayListExpand(GalleryConfig.PREV_START_SELECT))
        prevAdapter.updateEntity()

        galleryPrevCallback.onPrevViewCreated(savedInstanceState)
        preViewPager.adapter = prevAdapter
        preViewPager.registerOnPageChangeCallback(pageChangeCallback)
        preRootView.setBackgroundColor(galleryBundle.prevPhotoBackgroundColor)
        setCurrentItem((savedInstanceState
                ?: bundleOrEmptyExpand()).getIntExpand(GalleryConfig.PREV_START_POSITION))

        if (!galleryPrevInterceptor.hideCheckBox) {
            preCheckBox.setBackgroundResource(galleryBundle.checkBoxDrawable)
            preCheckBox.setOnClickListener { checkBoxClick(preCheckBox) }
            preCheckBox.isSelected = isCheckBox(currentPosition)
            preCheckBox.visibility = View.VISIBLE
        } else {
            preCheckBox.visibility = View.GONE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val parentId: Long = getLongExpand(GalleryConfig.PREV_PARENT_ID)
        if (parentId == GalleryConfig.PREV_SELECT_PARENT_ID) {
            //如果是点击预览这里认为未选中数据和选中数据应该一致
            updateEntity(getParcelableArrayListExpand(GalleryConfig.PREV_START_SELECT), savedInstanceState)
        } else {
            //https://issuetracker.google.com/issues/127692541
            //这个问题已经在ViewPager2上修复
            ScanImpl(object : ScanView {
                override val currentScanType: ScanType
                    get() = galleryBundle.scanType
                override val scanContext: FragmentActivity
                    get() = requireActivity()

                override fun resultSuccess(scanEntity: ScanEntity?) {
                    TODO("Not yet implemented")
                }

                override fun scanSuccess(arrayList: ArrayList<ScanEntity>) {
                    updateEntity(arrayList, savedInstanceState)
                }
            }).scanParent(parentId)
        }
    }

    fun checkBoxClick(preCheckBox: View) {
        if (!moveToNextToIdExpand(currentItem.externalUri())) {
            if (prevAdapter.containsSelect(currentItem)) {
                prevAdapter.removeSelectEntity(currentItem)
            }
            preCheckBox.isSelected = false
            currentItem.isCheck = false
            galleryPrevCallback.onClickCheckBoxFileNotExist(context, galleryBundle, currentItem)
            return
        }
        if (!prevAdapter.containsSelect(currentItem) && selectEntities.size >= galleryBundle.multipleMaxCount) {
            galleryPrevCallback.onClickCheckBoxMaxCount(context, galleryBundle, currentItem)
            return
        }
        if (currentItem.isCheck) {
            prevAdapter.removeSelectEntity(currentItem)
            currentItem.isCheck = false
            preCheckBox.isSelected = false
        } else {
            prevAdapter.addSelectEntity(currentItem)
            currentItem.isCheck = true
            preCheckBox.isSelected = true
        }
        galleryPrevCallback.onChangedCheckBox()
    }

    override val currentItem: ScanEntity
        get() = prevAdapter.item(currentPosition)

    override val allItem: ArrayList<ScanEntity>
        get() = prevAdapter.allItem

    override val selectEntities: ArrayList<ScanEntity>
        get() = prevAdapter.currentSelectList

    override val selectEmpty: Boolean
        get() = selectEntities.isEmpty()

    override val selectCount: Int
        get() = selectEntities.size

    override val itemCount: Int
        get() = prevAdapter.itemCount

    override val currentPosition: Int
        get() = preViewPager.currentItem

    override fun isCheckBox(position: Int): Boolean {
        return prevAdapter.isCheck(position)
    }

    override fun setCurrentItem(position: Int) {
        setCurrentItem(position, false)
    }

    override fun setCurrentItem(position: Int, smoothScroll: Boolean) {
        preViewPager.setCurrentItem(position, smoothScroll)
    }

    override fun resultBundle(isRefresh: Boolean): Bundle {
        val bundle = Bundle()
        bundle.putParcelableArrayList(GalleryConfig.PREV_RESULT_SELECT, selectEntities)
        bundle.putBoolean(GalleryConfig.PREV_RESULT_REFRESH, isRefresh)
        return bundle
    }

    override fun onDestroyView() {
        preViewPager.unregisterOnPageChangeCallback(pageChangeCallback)
        super.onDestroyView()
    }
}