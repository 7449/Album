package com.gallery.core.delegate

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.kotlin.expand.app.moveToNextToIdExpand
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.gallery.core.GalleryBundle
import com.gallery.core.GalleryConfig
import com.gallery.core.PrevArgs
import com.gallery.core.PrevArgs.Companion.configOrDefault
import com.gallery.core.PrevArgs.Companion.prevArgs
import com.gallery.core.PrevArgs.Companion.putArgs
import com.gallery.core.ScanArgs
import com.gallery.core.ScanArgs.Companion.putArgs
import com.gallery.core.callback.IGalleryPrevCallback
import com.gallery.core.callback.IGalleryPrevInterceptor
import com.gallery.core.expand.externalUri
import com.gallery.core.ui.adapter.PrevAdapter
import com.gallery.scan.ScanEntity
import com.gallery.scan.ScanImpl
import com.gallery.scan.ScanViewModelFactory

/**
 * 预览代理
 */
class PrevDelegate(
        private val fragment: Fragment,
        private val viewPager2: ViewPager2,
        private val checkBox: View
) : IPrevDelegate {

    private val pageChangeCallback: ViewPager2.OnPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            galleryPrevCallback.onPageScrolled(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageScrollStateChanged(state: Int) {
            galleryPrevCallback.onPageScrollStateChanged(state)
        }

        override fun onPageSelected(position: Int) {
            galleryPrevCallback.onPageSelected(position)
            checkBox.isSelected = isCheckBox(position)
        }
    }
    private val prevAdapter: PrevAdapter by lazy { PrevAdapter { entity, container -> fragment.galleryImageLoader.onDisplayGalleryPrev(entity, container) } }
    private val prevArgs: PrevArgs by lazy { fragment.prevArgs }
    private val galleryBundle: GalleryBundle by lazy { prevArgs.configOrDefault }
    private val galleryPrevCallback: IGalleryPrevCallback by lazy { fragment.galleryPrevCallback }
    private val galleryPrevInterceptor: IGalleryPrevInterceptor by lazy { fragment.galleryPrevInterceptor }

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
        get() = viewPager2.currentItem

    /**
     * 保存当前position和选中的文件数据
     */
    override fun onSaveInstanceState(outState: Bundle) {
        PrevArgs.newSaveInstance(currentPosition, selectEntities).putArgs(outState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        //https://github.com/7449/Album/issues/4
        //新增对单独扫描的支持，获取scanAlone和parentId
        val scanAlone = prevArgs.aloneScan
        val parentId: Long = prevArgs.parentId
        if (parentId == GalleryConfig.DEFAULT_PARENT_ID) {
            updateEntity(savedInstanceState, prevArgs.selectList)
        } else {
            //https://issuetracker.google.com/issues/127692541
            //这个问题已经在ViewPager2上修复
            val scanViewModel = ViewModelProvider(fragment.requireActivity(), ScanViewModelFactory(fragment.requireActivity(),
                    if (scanAlone == GalleryConfig.DEFAULT_SCAN_ALONE_TYPE) galleryBundle.scanType else scanAlone,
                    galleryBundle.scanSort,
                    galleryBundle.scanSortField))
                    .get(ScanImpl::class.java)
            scanViewModel.scanLiveData.observe(fragment.requireActivity(), { updateEntity(savedInstanceState, it.entities) })
            scanViewModel.scanParent(parentId)
        }
    }

    override fun updateEntity(savedInstanceState: Bundle?, arrayList: ArrayList<ScanEntity>) {
        val prevArgs: PrevArgs = savedInstanceState?.prevArgs ?: prevArgs
        prevAdapter.addAll(arrayList)
        prevAdapter.addSelectAll(prevArgs.selectList)
        prevAdapter.updateEntity()
        galleryPrevCallback.onPrevViewCreated(savedInstanceState)
        viewPager2.adapter = prevAdapter
        viewPager2.registerOnPageChangeCallback(pageChangeCallback)
        fragment.view?.setBackgroundColor(galleryBundle.prevPhotoBackgroundColor)
        setCurrentItem(prevArgs.position)
        checkBox.setBackgroundResource(galleryBundle.checkBoxDrawable)
        checkBox.setOnClickListener { checkBoxClick(checkBox) }
        checkBox.isSelected = isCheckBox(currentPosition)
    }

    override fun checkBoxClick(checkBox: View) {
        if (!fragment.moveToNextToIdExpand(currentItem.externalUri)) {
            if (prevAdapter.containsSelect(currentItem)) {
                prevAdapter.removeSelectEntity(currentItem)
            }
            checkBox.isSelected = false
            currentItem.isSelected = false
            galleryPrevCallback.onClickCheckBoxFileNotExist(fragment.requireActivity(), galleryBundle, currentItem)
            return
        }
        if (!prevAdapter.containsSelect(currentItem) && selectEntities.size >= galleryBundle.multipleMaxCount) {
            galleryPrevCallback.onClickCheckBoxMaxCount(fragment.requireActivity(), galleryBundle, currentItem)
            return
        }
        if (currentItem.isSelected) {
            prevAdapter.removeSelectEntity(currentItem)
            currentItem.isSelected = false
            checkBox.isSelected = false
        } else {
            prevAdapter.addSelectEntity(currentItem)
            currentItem.isSelected = true
            checkBox.isSelected = true
        }
        galleryPrevCallback.onChangedCheckBox()
    }

    override fun isCheckBox(position: Int): Boolean {
        return prevAdapter.isCheck(position)
    }

    override fun setCurrentItem(position: Int) {
        setCurrentItem(position, false)
    }

    override fun setCurrentItem(position: Int, smoothScroll: Boolean) {
        viewPager2.setCurrentItem(position, smoothScroll)
    }

    override fun notifyItemChanged(position: Int) {
        prevAdapter.notifyItemChanged(position)
    }

    override fun notifyDataSetChanged() {
        prevAdapter.notifyDataSetChanged()
    }

    override fun resultBundle(isRefresh: Boolean): Bundle {
        return ScanArgs.newResultInstance(selectEntities, isRefresh).putArgs()
    }

    override fun onDestroy() {
        viewPager2.unregisterOnPageChangeCallback(pageChangeCallback)
    }
}