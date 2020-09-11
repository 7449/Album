package com.gallery.core.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.gallery.core.GalleryBundle
import com.gallery.core.GalleryBundle.Companion.putGalleryArgs
import com.gallery.core.R
import com.gallery.core.ScanArgs
import com.gallery.core.delegate.ScanDelegate
import com.gallery.scan.args.ScanMinimumEntity
import com.gallery.scan.types.SCAN_ALL
import kotlinx.android.synthetic.main.gallery_fragment_gallery.*

open class ScanFragment(layoutId: Int = R.layout.gallery_fragment_gallery) : Fragment(layoutId) {

    companion object {
        fun newInstance(galleryBundle: GalleryBundle): ScanFragment {
            val scanFragment = ScanFragment()
            scanFragment.arguments = galleryBundle.putGalleryArgs()
            return scanFragment
        }
    }

    val delegate: ScanDelegate by lazy { createDelegate() }

    open fun createDelegate(): ScanDelegate {
        return ScanDelegate(this, galleryRecyclerView, galleryEmpty)
    }

    fun onCameraResultCanceled() {
        delegate.cameraCanceled()
    }

    fun onCameraResultOk() {
        delegate.cameraSuccess()
    }

    fun onScanGallery(parent: Long = SCAN_ALL, isCamera: Boolean = false) {
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

    fun scanSuccess(arrayList: ArrayList<ScanMinimumEntity>) {
        delegate.scanSuccess(arrayList)
    }

    val currentEntities: ArrayList<ScanMinimumEntity>
        get() = delegate.currentEntities

    val selectEntities: ArrayList<ScanMinimumEntity>
        get() = delegate.selectEntities

    val selectEmpty: Boolean
        get() = delegate.selectEmpty

    val selectCount: Int
        get() = delegate.selectCount

    var parentId: Long
        get() = delegate.parentId
        set(value) {
            delegate.parentId = value
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