package com.gallery.core.ui.widget

import android.database.ContentObserver
import android.net.Uri
import android.util.Log

class GalleryChangedObserver : ContentObserver(null) {

    override fun onChange(selfChange: Boolean, uri: Uri?) {
        Log.i("onChange", uri.toString())
    }

}