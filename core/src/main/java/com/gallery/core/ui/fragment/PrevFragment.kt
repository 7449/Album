package com.gallery.core.ui.fragment

import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.gallery.core.GalleryBundle
import com.gallery.core.R
import com.gallery.core.callback.IGalleryImageLoader
import com.gallery.core.callback.IGalleryPrev
import com.gallery.core.callback.IGalleryPrevCallback
import com.gallery.core.callback.IGalleryPrevInterceptor
import com.gallery.core.ext.externalUri
import com.gallery.core.ui.adapter.PrevAdapter
import com.gallery.core.ui.base.GalleryBaseFragment
import com.gallery.scan.ScanEntity
import com.kotlin.x.PermissionCode
import com.kotlin.x.uriExists
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

    private val galleryPrevInterceptor by lazy {
        when {
            parentFragment is IGalleryPrevInterceptor -> parentFragment as IGalleryPrevInterceptor
            activity is IGalleryPrevInterceptor -> activity as IGalleryPrevInterceptor
            else -> object : IGalleryPrevInterceptor {}
        }
    }
    private val galleryImageLoader by lazy {
        when {
            parentFragment is IGalleryImageLoader -> parentFragment as IGalleryImageLoader
            activity is IGalleryImageLoader -> activity as IGalleryImageLoader
            else -> object : IGalleryImageLoader {}
        }
    }
    private val galleryPrevCallback by lazy {
        when {
            parentFragment is IGalleryPrevCallback -> parentFragment as IGalleryPrevCallback
            activity is IGalleryPrevCallback -> activity as IGalleryPrevCallback
            else -> throw IllegalArgumentException(context.toString() + " must implement IGalleryPrevCallback")
        }
    }
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
                preCheckBox.isChecked = adapter.isCheck(position)
            }
        }
    }
    private val adapter by lazy {
        PrevAdapter { entity, container -> galleryImageLoader.onDisplayGalleryPrev(entity, container) }
    }
    private val galleryBundle by lazy {
        bundle.getParcelable(IGalleryPrev.PREV_START_CONFIG) ?: GalleryBundle()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(IGalleryPrev.PREV_START_POSITION, currentPosition)
        outState.putParcelableArrayList(IGalleryPrev.PREV_START_SELECT, selectEntities)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        preViewPager.registerOnPageChangeCallback(pageChangeCallback)
        preViewPager.adapter = adapter
        preCheckBox.setBackgroundResource(galleryBundle.checkBoxDrawable)
        preCheckBox.setOnClickListener { checkBoxClick() }
        preRootView.setBackgroundColor(galleryBundle.prevPhotoBackgroundColor)
        adapter.cleanAll()
        adapter.addAll(bundle.getParcelableArrayList<ScanEntity>(IGalleryPrev.PREV_START_ALL) as ArrayList<ScanEntity>)
        adapter.addSelectAll((savedInstanceState
                ?: bundle).getParcelableArrayList(IGalleryPrev.PREV_START_SELECT) ?: ArrayList())
        adapter.updateEntity()
        setCurrentItem((savedInstanceState ?: bundle).getInt(IGalleryPrev.PREV_START_POSITION, 0))
        preCheckBox.isChecked = adapter.isCheck(currentPosition)
        galleryPrevCallback.onChangedCreated()
    }

    private fun checkBoxClick() {
        if (!requireActivity().uriExists(currentItem.externalUri())) {
            preCheckBox.isChecked = false
            if (adapter.containsSelect(currentItem)) {
                adapter.removeSelectEntity(currentItem)
            }
            galleryPrevCallback.onClickCheckBoxFileNotExist(requireContext(), currentItem)
            return
        }
        if (!adapter.containsSelect(currentItem) && selectEntities.size >= galleryBundle.multipleMaxCount) {
            preCheckBox.isChecked = false
            galleryPrevCallback.onClickCheckBoxMaxCount(requireContext(), currentItem)
            return
        }
        if (currentItem.isCheck) {
            adapter.removeSelectEntity(currentItem)
            currentItem.isCheck = false
        } else {
            currentItem.isCheck = true
            adapter.addSelectEntity(currentItem)
        }
        galleryPrevCallback.onChangedCheckBox()
    }

    override val currentItem: ScanEntity
        get() = adapter.item(currentPosition)

    override val selectEntities: ArrayList<ScanEntity>
        get() = adapter.currentSelectList

    override val selectEmpty: Boolean
        get() = selectEntities.isEmpty()

    override val selectCount: Int
        get() = selectEntities.size

    override val itemCount: Int
        get() = adapter.itemCount

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

    override fun permissionsGranted(type: PermissionCode) = Unit

    override fun permissionsDenied(type: PermissionCode) = Unit

    override fun onDestroyView() {
        preViewPager.unregisterOnPageChangeCallback(pageChangeCallback)
        super.onDestroyView()
    }
}