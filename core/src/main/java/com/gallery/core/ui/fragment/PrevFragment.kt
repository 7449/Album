package com.gallery.core.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.kotlin.expand.app.moveToNextToIdExpand
import androidx.kotlin.expand.os.bundleOrEmptyExpand
import androidx.kotlin.expand.os.getIntExpand
import androidx.kotlin.expand.os.getParcelableArrayListExpand
import androidx.kotlin.expand.os.getParcelableOrDefault
import androidx.viewpager2.widget.ViewPager2
import com.gallery.core.GalleryBundle
import com.gallery.core.R
import com.gallery.core.callback.IGalleryImageLoader
import com.gallery.core.callback.IGalleryPrev
import com.gallery.core.callback.IGalleryPrevCallback
import com.gallery.core.callback.IGalleryPrevInterceptor
import com.gallery.core.ext.externalUri
import com.gallery.core.ext.galleryImageLoaderExpand
import com.gallery.core.ext.galleryPrevCallbackExpand
import com.gallery.core.ext.galleryPrevInterceptorExpand
import com.gallery.core.ui.adapter.PrevAdapter
import com.gallery.core.ui.base.GalleryBaseFragment
import com.gallery.scan.ScanEntity
import kotlinx.android.synthetic.main.gallery_fragment_preview.*

class PrevFragment : GalleryBaseFragment(R.layout.gallery_fragment_preview), IGalleryPrev {

    companion object {
        fun newInstance(allList: ArrayList<ScanEntity>,
                        selectList: ArrayList<ScanEntity> = ArrayList(),
                        config: GalleryBundle = GalleryBundle(),
                        position: Int = 0): PrevFragment {
            val prevFragment = PrevFragment()
            val bundle = Bundle()
            bundle.putParcelable(IGalleryPrev.PREV_START_CONFIG, config)
            bundle.putParcelableArrayList(IGalleryPrev.PREV_START_SELECT, selectList)
            bundle.putParcelableArrayList(IGalleryPrev.PREV_START_ALL, allList)
            bundle.putInt(IGalleryPrev.PREV_START_POSITION, position)
            prevFragment.arguments = bundle
            return prevFragment
        }
    }

    private val galleryPrevInterceptor: IGalleryPrevInterceptor by lazy { galleryPrevInterceptorExpand }
    private val galleryImageLoader: IGalleryImageLoader by lazy { galleryImageLoaderExpand }
    private val galleryPrevCallback: IGalleryPrevCallback by lazy { galleryPrevCallbackExpand }
    private val pageChangeCallback by lazy {
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
                    preCheckBox.isSelected = prevAdapter.isCheck(position)
                }
            }
        }
    }
    private val prevAdapter: PrevAdapter by lazy { PrevAdapter { entity, container -> galleryImageLoader.onDisplayGalleryPrev(entity, container) } }
    private val galleryBundle by lazy { getParcelableOrDefault<GalleryBundle>(IGalleryPrev.PREV_START_CONFIG, GalleryBundle()) }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(IGalleryPrev.PREV_START_POSITION, currentPosition)
        outState.putParcelableArrayList(IGalleryPrev.PREV_START_SELECT, selectEntities)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prevAdapter.cleanAll()
        prevAdapter.addAll(getParcelableArrayListExpand(IGalleryPrev.PREV_START_ALL))
        prevAdapter.addSelectAll((savedInstanceState ?: bundleOrEmptyExpand()).getParcelableArrayListExpand(IGalleryPrev.PREV_START_SELECT))
        prevAdapter.updateEntity()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        preViewPager.registerOnPageChangeCallback(pageChangeCallback)
        preViewPager.adapter = prevAdapter
        preCheckBox.setBackgroundResource(galleryBundle.checkBoxDrawable)
        preCheckBox.setOnClickListener { checkBoxClick(preCheckBox) }
        preRootView.setBackgroundColor(galleryBundle.prevPhotoBackgroundColor)
        setCurrentItem((savedInstanceState ?: bundleOrEmptyExpand()).getIntExpand(IGalleryPrev.PREV_START_POSITION))
        preCheckBox.isSelected = prevAdapter.isCheck(currentPosition)
        galleryPrevCallback.onChangedCreated()
        preCheckBox.visibility = if (galleryPrevInterceptor.hideCheckBox) View.GONE else View.VISIBLE
    }

    fun checkBoxClick(preCheckBox: View) {
        if (!moveToNextToIdExpand(currentItem.externalUri())) {
            preCheckBox.isSelected = false
            if (prevAdapter.containsSelect(currentItem)) {
                prevAdapter.removeSelectEntity(currentItem)
            }
            galleryPrevCallback.onClickCheckBoxFileNotExist(requireContext(), galleryBundle, currentItem)
            return
        }
        if (!prevAdapter.containsSelect(currentItem) && selectEntities.size >= galleryBundle.multipleMaxCount) {
            galleryPrevCallback.onClickCheckBoxMaxCount(requireContext(), galleryBundle, currentItem)
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

    override fun setCurrentItem(position: Int) {
        setCurrentItem(position, false)
    }

    override fun setCurrentItem(position: Int, flag: Boolean) {
        preViewPager.setCurrentItem(position, flag)
    }

    override fun resultBundle(isRefresh: Boolean): Bundle {
        val bundle = Bundle()
        bundle.putParcelableArrayList(IGalleryPrev.PREV_RESULT_SELECT, selectEntities)
        bundle.putBoolean(IGalleryPrev.PREV_RESULT_REFRESH, isRefresh)
        return bundle
    }

    override fun onDestroyView() {
        preViewPager.unregisterOnPageChangeCallback(pageChangeCallback)
        super.onDestroyView()
    }
}