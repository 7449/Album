package com.gallery.core.delegate

import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.fragment.app.Fragment
import androidx.kotlin.expand.net.isFileExistsExpand
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.gallery.core.GalleryBundle
import com.gallery.core.PrevArgs
import com.gallery.core.PrevArgs.Companion.configOrDefault
import com.gallery.core.PrevArgs.Companion.prevArgs
import com.gallery.core.PrevArgs.Companion.putPrevArgs
import com.gallery.core.ScanArgs
import com.gallery.core.ScanArgs.Companion.putScanArgs
import com.gallery.core.callback.IGalleryPrevCallback
import com.gallery.core.callback.IGalleryPrevInterceptor
import com.gallery.core.ui.adapter.PrevAdapter
import com.gallery.scan.args.ScanEntityFactory
import com.gallery.scan.extensions.*
import com.gallery.scan.extensions.ScanFileArgs
import com.gallery.scan.types.ScanType.SCAN_ALL
import com.gallery.scan.types.ScanType.SCAN_NONE

/**
 * 预览代理
 */
class PrevDelegate(
        private val fragment: Fragment,
        private val viewPager2: ViewPager2,
        private val checkBox: View,
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

    override val allItem: ArrayList<ScanEntity>
        get() = prevAdapter.allItem
    override val selectEntities: ArrayList<ScanEntity>
        get() = prevAdapter.currentSelectList
    override val currentPosition: Int
        get() = viewPager2.currentItem

    /**
     * 保存当前position和选中的文件数据
     */
    override fun onSaveInstanceState(outState: Bundle) {
        PrevArgs.newSaveInstance(currentPosition, selectEntities).putPrevArgs(outState)
    }

    /**
     * 如果parentId是[SCAN_NONE]的话，就是不扫描，直接把传入的 selectList
     * 作为全部数据展示
     * 否则从数据库获取数据，从数据库获取数据时会判断 scanAlone 是否是 [MediaStore.Files.FileColumns.MEDIA_TYPE_NONE]
     * 如果是，则使用 [GalleryBundle.scanType]作为参数，否则使用 scanAlone
     * 如果预览页想扫描专门的类型，则使用 scanAlone 即可，这个时候传入[SCAN_ALL]即可
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        //https://github.com/7449/Album/issues/4
        //新增对单独扫描的支持，获取scanAlone和parentId
        val scanAlone = prevArgs.scanAlone
        val parentId: Long = prevArgs.parentId
        if (parentId.isScanNoNeExpand()) {
            updateEntity(savedInstanceState, prevArgs.selectList)
        } else {
            //https://issuetracker.google.com/issues/127692541
            //这个问题已经在ViewPager2上修复
            val scanFileArgs = ScanFileArgs(
                    if (scanAlone == MediaStore.Files.FileColumns.MEDIA_TYPE_NONE) galleryBundle.scanType.map { it.toString() }.toTypedArray() else arrayOf(scanAlone.toString()),
                    galleryBundle.scanSortField,
                    galleryBundle.scanSort
            )
            ViewModelProvider(fragment,
                    ScanViewModelFactory(
                            ownerFragment = fragment,
                            factory = ScanEntityFactory.fileExpand(),
                            args = scanFileArgs
                    )
            ).scanFileImpl().registerLiveData(fragment) { result ->
                updateEntity(savedInstanceState, result.multipleValue.toScanEntity())
            }.scanMultiple(parentId.multipleScanExpand())
        }
    }

    override fun updateEntity(savedInstanceState: Bundle?, arrayList: ArrayList<ScanEntity>) {
        val prevArgs: PrevArgs = savedInstanceState?.prevArgs ?: prevArgs
        prevAdapter.addAll(arrayList)
        prevAdapter.addSelectAll(prevArgs.selectList)
        prevAdapter.updateEntity()
        galleryPrevCallback.onPrevCreated()
        viewPager2.adapter = prevAdapter
        viewPager2.registerOnPageChangeCallback(pageChangeCallback)
        fragment.view?.setBackgroundColor(galleryBundle.prevPhotoBackgroundColor)
        setCurrentItem(prevArgs.position)
        checkBox.setBackgroundResource(galleryBundle.checkBoxDrawable)
        checkBox.setOnClickListener { checkBoxClick(checkBox) }
        checkBox.isSelected = isCheckBox(currentPosition)
    }

    override fun checkBoxClick(checkBox: View) {
        if (!currentItem.uri.isFileExistsExpand(fragment.requireActivity())) {
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
        return ScanArgs.newResultInstance(selectEntities, isRefresh).putScanArgs()
    }

    override fun onDestroy() {
        viewPager2.unregisterOnPageChangeCallback(pageChangeCallback)
    }
}