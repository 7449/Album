package com.gallery.ui.page.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.gallery.core.GalleryBundle
import com.gallery.core.GalleryBundle.Companion.putGalleryArgs
import com.gallery.core.ScanArgs
import com.gallery.core.callback.IGalleryCallback
import com.gallery.core.callback.IGalleryImageLoader
import com.gallery.core.callback.IGalleryInterceptor
import com.gallery.core.crop.ICrop
import com.gallery.core.delegate.IScanDelegate
import com.gallery.core.delegate.entity.ScanEntity
import com.gallery.core.delegate.impl.ScanDelegateImpl
import com.gallery.core.extensions.toScanFileEntity
import com.gallery.scan.types.ScanType
import com.gallery.ui.R

open class ScanFragment(layoutId: Int = R.layout.gallery_fragment_gallery) : Fragment(layoutId) {

    companion object {
        @JvmStatic
        fun newInstance(galleryBundle: GalleryBundle): ScanFragment {
            val scanFragment = ScanFragment()
            scanFragment.arguments = galleryBundle.putGalleryArgs()
            return scanFragment
        }
    }

    private val galleryInterceptor: IGalleryInterceptor
        get() = when {
            parentFragment is IGalleryInterceptor -> parentFragment as IGalleryInterceptor
            activity is IGalleryInterceptor -> activity as IGalleryInterceptor
            else -> object : IGalleryInterceptor {}
        }

    private val galleryCallback: IGalleryCallback
        get() = when {
            parentFragment is IGalleryCallback -> parentFragment as IGalleryCallback
            activity is IGalleryCallback -> activity as IGalleryCallback
            else -> throw IllegalArgumentException(context.toString() + " must implement IGalleryCallback")
        }

    private val galleryImageLoader: IGalleryImageLoader
        get() = when {
            parentFragment is IGalleryImageLoader -> parentFragment as IGalleryImageLoader
            activity is IGalleryImageLoader -> activity as IGalleryImageLoader
            else -> throw IllegalArgumentException(context.toString() + " must implement IGalleryImageLoader")
        }

    private val galleryCrop: ICrop?
        get() = when {
            parentFragment is ICrop -> (parentFragment as ICrop).cropImpl
            activity is ICrop -> (activity as ICrop).cropImpl
            else -> null
        }

    val delegate: IScanDelegate by lazy { createDelegate() }

    open fun createDelegate(): ScanDelegateImpl {
        return ScanDelegateImpl(
                this,
                galleryCrop,
                galleryCallback,
                galleryInterceptor,
                galleryImageLoader)
    }

    fun onCameraResultCanceled() {
        delegate.cameraCanceled()
    }

    fun onCameraResultOk() {
        delegate.cameraSuccess()
    }

    fun onScanGallery(parent: Long = ScanType.SCAN_ALL, isCamera: Boolean = false) {
        delegate.onScanGallery(parent, isCamera)
    }

    fun onUpdateResult(scanArgs: ScanArgs?) {
        delegate.onUpdateResult(scanArgs)
    }

    fun notifyItemChanged(position: Int) {
        delegate.notifyItemChanged(position)
    }

    fun notifyDataSetChanged() {
        delegate.notifyDataSetChanged()
    }

    fun addOnScrollListener(onScrollListener: RecyclerView.OnScrollListener) {
        delegate.addOnScrollListener(onScrollListener)
    }

    fun scrollToPosition(position: Int) {
        delegate.scrollToPosition(position)
    }

    fun scanMultipleSuccess(arrayList: ArrayList<ScanEntity>) {
        delegate.onScanMultipleSuccess(arrayList.toScanFileEntity())
    }

    val currentEntities: ArrayList<ScanEntity>
        get() = delegate.currentEntities

    val selectEntities: ArrayList<ScanEntity>
        get() = delegate.selectEntities

    val selectEmpty: Boolean
        get() = delegate.selectEmpty

    val selectCount: Int
        get() = delegate.selectCount

    var parentId: Long
        get() = delegate.currentParentId
        set(value) {
            delegate.onUpdateParentId(value)
        }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        delegate.onSaveInstanceState(outState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        delegate.onCreate(savedInstanceState)
    }

    override fun onDestroyView() {
        delegate.onDestroy()
        super.onDestroyView()
    }
}