package com.gallery.compat.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.gallery.compat.R
import com.gallery.core.delegate.IPrevDelegate
import com.gallery.core.delegate.args.PrevArgs
import com.gallery.core.delegate.args.PrevArgs.Companion.putPrevArgs
import com.gallery.core.delegate.impl.PrevDelegateImpl
import com.gallery.core.entity.ScanEntity

open class PrevCompatFragment(layoutId: Int = R.layout.gallery_compat_fragment_preview) :
    Fragment(layoutId) {

    companion object {
        fun newInstance(prevArgs: PrevArgs): PrevCompatFragment {
            val prevFragment = PrevCompatFragment()
            prevFragment.arguments = prevArgs.putPrevArgs()
            return prevFragment
        }
    }

    private val delegate: IPrevDelegate by lazy { createDelegate() }

    open fun createDelegate(): IPrevDelegate {
        return PrevDelegateImpl(this, galleryCallback(), galleryCallback())
    }

    open val rootView: View
        get() = delegate.rootView

    open val currentItem: ScanEntity
        get() = delegate.currentItem

    open val allItem: ArrayList<ScanEntity>
        get() = delegate.allItem

    open val selectItem: ArrayList<ScanEntity>
        get() = delegate.selectItem

    open val isSelectEmpty: Boolean
        get() = delegate.isSelectEmpty

    open val selectCount: Int
        get() = delegate.selectCount

    open val itemCount: Int
        get() = delegate.itemCount

    open val currentPosition: Int
        get() = delegate.currentPosition

    open fun checkBoxClick(checkBox: View) {
        delegate.itemViewClick(checkBox)
    }

    open fun isCheckBox(position: Int): Boolean {
        return delegate.isSelected(position)
    }

    open fun setCurrentItem(position: Int) {
        delegate.setCurrentItem(position)
    }

    open fun setCurrentItem(position: Int, smoothScroll: Boolean) {
        delegate.setCurrentItem(position, smoothScroll)
    }

    open fun notifyItemChanged(position: Int) {
        delegate.notifyItemChanged(position)
    }

    open fun notifyDataSetChanged() {
        delegate.notifyDataSetChanged()
    }

    open fun resultBundle(isRefresh: Boolean): Bundle {
        return delegate.resultBundle(isRefresh)
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