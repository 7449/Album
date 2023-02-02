package com.gallery.core.delegate.impl

import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.gallery.core.GalleryConfigs
import com.gallery.core.R
import com.gallery.core.callback.IGalleryImageLoader
import com.gallery.core.callback.IGalleryPrevCallback
import com.gallery.core.delegate.IPrevDelegate
import com.gallery.core.delegate.adapter.PrevAdapter
import com.gallery.core.delegate.args.PrevArgs
import com.gallery.core.delegate.args.PrevArgs.Companion.configOrDefault
import com.gallery.core.delegate.args.PrevArgs.Companion.prevArgs
import com.gallery.core.delegate.args.PrevArgs.Companion.prevArgsOrDefault
import com.gallery.core.delegate.args.PrevArgs.Companion.toBundle
import com.gallery.core.delegate.args.ScanArgs
import com.gallery.core.delegate.args.ScanArgs.Companion.toBundle
import com.gallery.core.entity.ScanEntity
import com.gallery.core.extensions.fileExists
import com.gallery.core.extensions.orEmpty
import com.gallery.core.extensions.toScanEntity
import com.gallery.scan.Types.Id.ALL
import com.gallery.scan.Types.Id.NONE
import com.gallery.scan.extensions.fileScan
import com.gallery.scan.extensions.isScanNoNeMedia
import com.gallery.scan.impl.MediaScanImpl
import com.gallery.scan.impl.file.FileScanArgs

/**
 * 预览代理
 * 需要在[Fragment]的[Fragment.onViewCreated]之后初始化
 */
class PrevDelegateImpl(
    /**
     * [Fragment]
     * 承载容器
     * 使用容器获取需要的[ViewPager2]
     * [Fragment]中必须存在 [R.id.gallery_prev_viewpager2]和[R.id.gallery_prev_checkbox] 两个id的View
     */
    private val fragment: Fragment,
    /**
     * [IGalleryPrevCallback]
     * 预览回调
     */
    private val galleryPrevCallback: IGalleryPrevCallback,
    /**
     * [IGalleryImageLoader]
     * 图片加载框架
     */
    private val galleryImageLoader: IGalleryImageLoader,
) : IPrevDelegate {

    private val pageChangeCallback: ViewPager2.OnPageChangeCallback =
        object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                galleryPrevCallback.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageScrollStateChanged(state: Int) {
                galleryPrevCallback.onPageScrollStateChanged(state)
            }

            override fun onPageSelected(position: Int) {
                galleryPrevCallback.onPageSelected(position)
                checkBox.isSelected = isSelected(position)
            }
        }

    private val viewPager2: ViewPager2 = rootView.findViewById(R.id.gallery_prev_viewpager2)
    private val checkBox: View = rootView.findViewById(R.id.gallery_prev_checkbox)
    private val prevAdapter: PrevAdapter = PrevAdapter { entity, container ->
        galleryImageLoader.onDisplayPrevGallery(entity, container)
    }
    private val args = fragment.arguments.orEmpty().prevArgsOrDefault
    private val configs = args.configOrDefault

    override val rootView: View get() = fragment.requireView()
    override val allItem: ArrayList<ScanEntity> get() = prevAdapter.allItem
    override val selectItem: ArrayList<ScanEntity> get() = prevAdapter.currentSelectList
    override val currentPosition: Int get() = viewPager2.currentItem

    override fun onSaveInstanceState(outState: Bundle) {
        PrevArgs.onSaveInstanceState(currentPosition, selectItem).toBundle(outState)
    }

    /**
     * 如果parentId是[NONE]的话，就是不扫描，直接把传入的 selectList
     * 作为全部数据展示
     * 否则从数据库获取数据，从数据库获取数据时会判断 singleType 是否是 [MediaStore.Files.FileColumns.MEDIA_TYPE_NONE]
     * 如果是，则使用 [GalleryConfigs.type]作为参数，否则使用 singleType
     * 如果预览页想扫描专门的类型，则使用 singleType，这个时候传入[ALL]即可
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        //https://github.com/7449/Album/issues/4
        //新增对单独扫描的支持，获取singleType和parentId
        val singleType = args.scanSingleType
        val parentId: Long = args.parentId
        if (parentId.isScanNoNeMedia) {
            updateEntity(savedInstanceState, args.selectList)
        } else {
            //https://issuetracker.google.com/issues/127692541
            //这个问题已经在ViewPager2上修复
            val scanFileArgs = FileScanArgs(
                if (singleType == MediaStore.Files.FileColumns.MEDIA_TYPE_NONE)
                    configs.type
                else
                    arrayOf(singleType.toString()),
                configs.sort.second,
                configs.sort.first
            )
            MediaScanImpl(fragment.fileScan(scanFileArgs)) {
                updateEntity(savedInstanceState, multipleValue.toScanEntity())
            }.multiple(parentId)
        }
    }

    override fun updateEntity(savedInstanceState: Bundle?, arrayList: ArrayList<ScanEntity>) {
        val prevArgs: PrevArgs = savedInstanceState?.prevArgs ?: args
        prevAdapter.addAll(arrayList)
        prevAdapter.addSelectAll(prevArgs.selectList)
        prevAdapter.updateEntity()
        viewPager2.adapter = prevAdapter
        viewPager2.registerOnPageChangeCallback(pageChangeCallback)
        setCurrentItem(prevArgs.position)
        checkBox.setBackgroundResource(configs.cameraConfig.checkBoxIcon)
        checkBox.setOnClickListener { selectPictureClick(checkBox) }
        checkBox.isSelected = isSelected(currentPosition)
        galleryPrevCallback.onPrevCreated(this, configs, savedInstanceState)
    }

    override fun selectPictureClick(box: View) {
        val activity = fragment.requireActivity()
        if (!currentItem.uri.fileExists(activity)) {
            if (prevAdapter.containsSelect(currentItem)) {
                prevAdapter.removeSelect(currentItem)
            }
            box.isSelected = false
            currentItem.isSelected = false
            galleryPrevCallback.onSelectMultipleFileNotExist(currentItem)
            return
        }
        if (!prevAdapter.containsSelect(currentItem) && selectItem.size >= configs.maxCount) {
            galleryPrevCallback.onSelectMultipleMaxCount()
            return
        }
        if (currentItem.isSelected) {
            prevAdapter.removeSelect(currentItem)
            currentItem.isSelected = false
            box.isSelected = false
        } else {
            prevAdapter.addSelect(currentItem)
            currentItem.isSelected = true
            box.isSelected = true
        }
        galleryPrevCallback.onSelectMultipleFileChanged(currentPosition, currentItem)
    }

    override fun isSelected(position: Int): Boolean {
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
        return ScanArgs.newResultInstance(selectItem, isRefresh).toBundle(bundleOf())
    }

    override fun onDestroy() {
        viewPager2.unregisterOnPageChangeCallback(pageChangeCallback)
    }

}