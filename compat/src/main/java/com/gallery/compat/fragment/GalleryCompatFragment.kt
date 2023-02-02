package com.gallery.compat.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.gallery.compat.R
import com.gallery.core.args.GalleryConfigs
import com.gallery.core.args.GalleryConfigs.Companion.toBundle
import com.gallery.core.callback.IGalleryInterceptor
import com.gallery.core.crop.ICrop
import com.gallery.core.delegate.IScanDelegate
import com.gallery.core.delegate.args.ScanArgs
import com.gallery.core.delegate.impl.ScanDelegateImpl
import com.gallery.core.entity.ScanEntity
import com.gallery.core.extensions.toFileEntity
import com.gallery.scan.Types
import com.gallery.scan.extensions.isScanAllMedia

open class GalleryCompatFragment(layoutId: Int = R.layout.gallery_compat_fragment_gallery) :
    Fragment(layoutId) {

    companion object {
        fun newInstance(configs: GalleryConfigs): GalleryCompatFragment {
            val scanFragment = GalleryCompatFragment()
            scanFragment.arguments = configs.toBundle()
            return scanFragment
        }
    }

    private val delegate: IScanDelegate by lazy { createDelegate() }

    open fun createDelegate(): IScanDelegate {
        return ScanDelegateImpl(
            this,
            galleryCallbackOrNull<ICrop>()?.cropImpl,
            galleryCallback(),
            galleryCallbackOrNewInstance<IGalleryInterceptor> { object : IGalleryInterceptor {} },
            galleryCallback()
        )
    }

    open fun onCameraResultCanceled() {
        delegate.takePictureCanceled()
    }

    open fun onCameraResultOk() {
        delegate.takePictureSuccess()
    }

    open fun onScanGallery(parent: Long = Types.Id.ALL, isCamera: Boolean = false) {
        delegate.onScanGallery(parent, isCamera)
    }

    open fun onUpdateResult(scanArgs: ScanArgs) {
        delegate.onUpdateResult(scanArgs)
    }

    open fun notifyItemChanged(position: Int) {
        delegate.notifyItemChanged(position)
    }

    open fun notifyDataSetChanged() {
        delegate.notifyDataSetChanged()
    }

    open fun addOnScrollListener(onScrollListener: OnScrollListener) {
        delegate.addOnScrollListener(onScrollListener)
    }

    open fun removeOnScrollListener(onScrollListener: OnScrollListener) {
        delegate.removeOnScrollListener(onScrollListener)
    }

    open fun scrollToPosition(position: Int) {
        delegate.scrollToPosition(position)
    }

    open fun scanMultipleSuccess(arrayList: ArrayList<ScanEntity>) {
        delegate.onScanMultipleSuccess(arrayList.toFileEntity())
    }

    open val rootView: View
        get() = delegate.rootView

    open val allItem: ArrayList<ScanEntity>
        get() = delegate.allItem

    open val selectItem: ArrayList<ScanEntity>
        get() = delegate.selectItem

    open val isSelectEmpty: Boolean
        get() = delegate.isSelectEmpty

    open val selectCount: Int
        get() = delegate.selectCount

    open var parentId: Long
        get() = delegate.currentParentId
        set(value) {
            delegate.updateParentId(value)
        }

    open val isScanAll: Boolean
        get() = parentId.isScanAllMedia

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