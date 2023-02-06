package develop.file.gallery.compat.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.gallery.compat.R
import develop.file.gallery.args.GalleryConfigs
import develop.file.gallery.args.GalleryConfigs.Companion.toBundle
import develop.file.gallery.callback.IGalleryInterceptor
import develop.file.gallery.compat.extensions.FragmentCompat.galleryCallback
import develop.file.gallery.compat.extensions.FragmentCompat.galleryCallbackOrNewInstance
import develop.file.gallery.compat.extensions.FragmentCompat.galleryCallbackOrNull
import develop.file.gallery.crop.ICrop
import develop.file.gallery.delegate.IScanDelegate
import develop.file.gallery.delegate.args.ScanArgs
import develop.file.gallery.delegate.impl.ScanDelegateImpl
import develop.file.gallery.entity.ScanEntity
import develop.file.gallery.extensions.ResultCompat.toFileMediaEntity
import develop.file.media.Types
import develop.file.media.extensions.isAllMediaParent

open class GalleryGridFragment(layoutId: Int = R.layout.gallery_compat_fragment_gallery) :
    Fragment(layoutId) {

    companion object {
        fun newInstance(configs: GalleryConfigs): GalleryGridFragment {
            val scanFragment = GalleryGridFragment()
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

    fun onCameraResultCanceled() {
        delegate.takePictureCanceled()
    }

    fun onCameraResultOk() {
        delegate.takePictureSuccess()
    }

    fun onScanGallery(parent: Long = Types.Id.ALL, isCamera: Boolean = false) {
        delegate.onScanGallery(parent, isCamera)
    }

    fun onUpdateResult(scanArgs: ScanArgs) {
        delegate.onUpdateResult(scanArgs)
    }

    fun notifyItemChanged(position: Int) {
        delegate.notifyItemChanged(position)
    }

    fun notifyDataSetChanged() {
        delegate.notifyDataSetChanged()
    }

    fun addOnScrollListener(onScrollListener: OnScrollListener) {
        delegate.addOnScrollListener(onScrollListener)
    }

    fun removeOnScrollListener(onScrollListener: OnScrollListener) {
        delegate.removeOnScrollListener(onScrollListener)
    }

    fun scrollToPosition(position: Int) {
        delegate.scrollToPosition(position)
    }

    fun scanMultipleSuccess(arrayList: ArrayList<ScanEntity>) {
        delegate.onScanMultipleSuccess(arrayList.toFileMediaEntity())
    }

    val rootView: View
        get() = delegate.rootView

    val allItem: ArrayList<ScanEntity>
        get() = delegate.allItem

    val selectItem: ArrayList<ScanEntity>
        get() = delegate.selectItem

    val isSelectEmpty: Boolean
        get() = delegate.isSelectEmpty

    val selectCount: Int
        get() = delegate.selectCount

    var parentId: Long
        get() = delegate.currentParentId
        set(value) {
            delegate.updateParentId(value)
        }

    val isScanAll: Boolean
        get() = parentId.isAllMediaParent

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