package com.gallery.core.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.gallery.core.PrevArgs
import com.gallery.core.PrevArgs.Companion.putPrevArgs
import com.gallery.core.R
import com.gallery.core.delegate.PrevDelegate
import com.gallery.core.delegate.ScanEntity
import kotlinx.android.synthetic.main.gallery_fragment_preview.*

open class PrevFragment(layoutId: Int = R.layout.gallery_fragment_preview) : Fragment(layoutId) {

    companion object {
        fun newInstance(prevArgs: PrevArgs): PrevFragment {
            val prevFragment = PrevFragment()
            prevFragment.arguments = prevArgs.putPrevArgs()
            return prevFragment
        }
    }

    val delegate: PrevDelegate by lazy { createDelegate() }

    open fun createDelegate(): PrevDelegate {
        return PrevDelegate(this, preViewPager, preCheckBox)
    }

    val currentItem: ScanEntity
        get() = delegate.currentItem

    val allItem: ArrayList<ScanEntity>
        get() = delegate.allItem

    val selectEntities: ArrayList<ScanEntity>
        get() = delegate.selectEntities

    val selectEmpty: Boolean
        get() = delegate.selectEmpty

    val selectCount: Int
        get() = delegate.selectCount

    val itemCount: Int
        get() = delegate.itemCount

    val currentPosition: Int
        get() = delegate.currentPosition

    fun checkBoxClick(checkBox: View) {
        delegate.checkBoxClick(checkBox)
    }

    fun isCheckBox(position: Int): Boolean {
        return delegate.isCheckBox(position)
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