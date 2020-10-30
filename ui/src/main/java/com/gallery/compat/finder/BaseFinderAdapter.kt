package com.gallery.compat.finder

import android.view.View
import com.gallery.compat.GalleryUiBundle
import com.gallery.compat.activity.GalleryCompatActivity

abstract class BaseFinderAdapter : GalleryFinderAdapter {

    protected val activity: GalleryCompatActivity by lazy { activityPrivate }
    protected val uiBundle: GalleryUiBundle by lazy { uiBundlePrivate }
    protected val viewAnchor: View by lazy { anchorViewPrivate }
    protected val listener: GalleryFinderAdapter.AdapterFinderListener by lazy { listenerPrivate }

    private lateinit var activityPrivate: GalleryCompatActivity
    private lateinit var uiBundlePrivate: GalleryUiBundle
    private lateinit var anchorViewPrivate: View
    private lateinit var listenerPrivate: GalleryFinderAdapter.AdapterFinderListener

    override fun adapterInit(activity: GalleryCompatActivity, uiBundle: GalleryUiBundle, anchorView: View?) {
        this.activityPrivate = activity
        this.uiBundlePrivate = uiBundle
        anchorView?.let { this.anchorViewPrivate = it }
    }

    override fun setOnAdapterFinderListener(listener: GalleryFinderAdapter.AdapterFinderListener) {
        this.listenerPrivate = listener
    }
}