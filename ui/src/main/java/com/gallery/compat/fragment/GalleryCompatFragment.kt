package com.gallery.compat.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.gallery.core.GalleryBundle
import com.gallery.core.GalleryBundle.Companion.putGalleryArgs
import com.gallery.core.ScanArgs
import com.gallery.core.callback.IGalleryInterceptor
import com.gallery.core.delegate.IScanDelegate
import com.gallery.core.delegate.impl.ScanDelegateImpl
import com.gallery.core.entity.ScanEntity
import com.gallery.core.extensions.toScanFileEntity
import com.gallery.scan.extensions.isScanAllExpand
import com.gallery.scan.types.ScanType
import com.gallery.ui.R

open class GalleryCompatFragment(layoutId: Int = R.layout.gallery_fragment_gallery) : Fragment(layoutId) {

    companion object {
        fun newInstance(galleryBundle: GalleryBundle): GalleryCompatFragment {
            val scanFragment = GalleryCompatFragment()
            scanFragment.arguments = galleryBundle.putGalleryArgs()
            return scanFragment
        }
    }

    private val delegate: IScanDelegate by lazy { createDelegate() }

    open fun createDelegate(): IScanDelegate {
        return ScanDelegateImpl(
                this,
                galleryCallbackOrNull(),
                galleryCallback(),
                galleryCallbackOrNewInstance { object : IGalleryInterceptor {} },
                galleryCallback()
        )
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

    val isScanAll: Boolean
        get() = parentId.isScanAllExpand()

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