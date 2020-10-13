package com.gallery.core.extension.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.kotlin.expand.os.bundleOrEmptyExpand
import com.gallery.core.PrevArgs
import com.gallery.core.PrevArgs.Companion.prevArgsOrDefault
import com.gallery.core.PrevArgs.Companion.putPrevArgs
import com.gallery.core.callback.IGalleryImageLoader
import com.gallery.core.callback.IGalleryPrevCallback
import com.gallery.core.callback.IGalleryPrevInterceptor
import com.gallery.core.delegate.IPrevDelegate
import com.gallery.core.delegate.PrevDelegateImpl
import com.gallery.core.delegate.entity.ScanEntity
import com.gallery.core.extension.R
import kotlinx.android.synthetic.main.gallery_fragment_preview.*

open class PrevFragment(layoutId: Int = R.layout.gallery_fragment_preview) : Fragment(layoutId) {

    companion object {
        fun newInstance(prevArgs: PrevArgs): PrevFragment {
            val prevFragment = PrevFragment()
            prevFragment.arguments = prevArgs.putPrevArgs()
            return prevFragment
        }
    }

    private val galleryPrevInterceptor: IGalleryPrevInterceptor
        get() = when {
            parentFragment is IGalleryPrevInterceptor -> parentFragment as IGalleryPrevInterceptor
            activity is IGalleryPrevInterceptor -> activity as IGalleryPrevInterceptor
            else -> object : IGalleryPrevInterceptor {}
        }

    private val galleryPrevCallback: IGalleryPrevCallback
        get() = when {
            parentFragment is IGalleryPrevCallback -> parentFragment as IGalleryPrevCallback
            activity is IGalleryPrevCallback -> activity as IGalleryPrevCallback
            else -> throw IllegalArgumentException(context.toString() + " must implement IGalleryPrevCallback")
        }

    private val galleryImageLoader: IGalleryImageLoader
        get() = when {
            parentFragment is IGalleryImageLoader -> parentFragment as IGalleryImageLoader
            activity is IGalleryImageLoader -> activity as IGalleryImageLoader
            else -> throw IllegalArgumentException(context.toString() + " must implement IGalleryImageLoader")
        }

    private val prevArgs: PrevArgs get() = bundleOrEmptyExpand().prevArgsOrDefault

    val delegate: IPrevDelegate by lazy { createDelegate() }

    open fun createDelegate(): PrevDelegateImpl {
        return PrevDelegateImpl(
                this,
                preViewPager,
                preCheckBox,
                galleryPrevCallback,
                galleryPrevInterceptor,
                galleryImageLoader,
                prevArgs
        )
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