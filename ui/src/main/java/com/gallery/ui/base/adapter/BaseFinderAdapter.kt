package com.gallery.ui.base.adapter

import android.view.View
import com.gallery.ui.GalleryUiBundle
import com.gallery.ui.base.activity.GalleryBaseActivity

abstract class BaseFinderAdapter : GalleryFinderAdapter {

    protected val activity: GalleryBaseActivity by lazy { activityPrivate }
    protected val uiBundle: GalleryUiBundle by lazy { uiBundlePrivate }
    protected val viewAnchor: View by lazy { anchorViewPrivate }
    protected val listener: GalleryFinderAdapter.AdapterFinderListener by lazy { listenerPrivate }

    private lateinit var activityPrivate: GalleryBaseActivity
    private lateinit var uiBundlePrivate: GalleryUiBundle
    private lateinit var anchorViewPrivate: View
    private lateinit var listenerPrivate: GalleryFinderAdapter.AdapterFinderListener

    override fun adapterInit(activity: GalleryBaseActivity, uiBundle: GalleryUiBundle, anchorView: View?) {
        this.activityPrivate = activity
        this.uiBundlePrivate = uiBundle
        anchorView?.let { this.anchorViewPrivate = it }
    }

    override fun setOnAdapterFinderListener(listener: GalleryFinderAdapter.AdapterFinderListener) {
        this.listenerPrivate = listener
    }
}