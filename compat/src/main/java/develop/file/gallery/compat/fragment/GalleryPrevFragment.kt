package develop.file.gallery.compat.fragment

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.gallery.compat.R
import develop.file.gallery.compat.extensions.FragmentCompat.galleryCallback
import develop.file.gallery.delegate.IPrevDelegate
import develop.file.gallery.delegate.args.PrevArgs
import develop.file.gallery.delegate.args.PrevArgs.Companion.toBundle
import develop.file.gallery.delegate.impl.PrevDelegateImpl
import develop.file.gallery.entity.ScanEntity

open class GalleryPrevFragment(layoutId: Int = R.layout.gallery_compat_fragment_preview) :
    Fragment(layoutId) {

    companion object {
        fun newInstance(prevArgs: PrevArgs): GalleryPrevFragment {
            val prevFragment = GalleryPrevFragment()
            prevFragment.arguments = prevArgs.toBundle(bundleOf())
            return prevFragment
        }
    }

    private val delegate: IPrevDelegate by lazy { createDelegate() }

    open fun createDelegate(): IPrevDelegate {
        return PrevDelegateImpl(this, galleryCallback(), galleryCallback())
    }

    val rootView: View
        get() = delegate.rootView

    val currentItem: ScanEntity
        get() = delegate.currentItem

    val allItem: ArrayList<ScanEntity>
        get() = delegate.allItem

    val selectItem: ArrayList<ScanEntity>
        get() = delegate.selectItem

    val isSelectEmpty: Boolean
        get() = delegate.isSelectEmpty

    val selectCount: Int
        get() = delegate.selectCount

    val itemCount: Int
        get() = delegate.itemCount

    val currentPosition: Int
        get() = delegate.currentPosition

    fun checkBoxClick(checkBox: View) {
        delegate.selectPictureClick(checkBox)
    }

    fun isCheckBox(position: Int): Boolean {
        return delegate.isSelected(position)
    }

    fun setCurrentItem(position: Int) {
        delegate.setCurrentItem(position)
    }

    fun setCurrentItem(position: Int, smoothScroll: Boolean) {
        delegate.setCurrentItem(position, smoothScroll)
    }

    fun notifyItemChanged(position: Int) {
        delegate.notifyItemChanged(position)
    }

    fun notifyDataSetChanged() {
        delegate.notifyDataSetChanged()
    }

    fun resultBundle(isRefresh: Boolean): Bundle {
        return delegate.resultBundle(isRefresh)
    }

    fun index(id: Long): Int {
        return allItem.indexOfFirst { it.id == id }
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