package com.gallery.sample

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gallery.compat.extensions.galleryFragment
import com.gallery.compat.fragment.GalleryCompatFragment
import com.gallery.compat.fragment.addFragment
import com.gallery.compat.fragment.showFragment
import com.gallery.compat.internal.simple.SimpleGalleryCallback
import com.gallery.compat.widget.GalleryImageView
import com.gallery.core.GalleryConfigs
import com.gallery.core.GridConfig
import com.gallery.core.callback.IGalleryImageLoader
import com.gallery.core.delegate.IScanDelegate
import com.gallery.core.entity.ScanEntity

abstract class GalleryListActivity : AppCompatActivity(), SimpleGalleryCallback,
    IGalleryImageLoader {

    private fun showDialog(msg: String) {
        AlertDialog
            .Builder(this)
            .setMessage(msg)
            .show()
    }

    abstract fun getGalleryListRootViewId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        galleryFragment?.let {
            showFragment(fragment = it)
        } ?: addFragment(
            getGalleryListRootViewId(), fragment = GalleryCompatFragment.newInstance(
                GalleryConfigs(
                    radio = true,
                    crop = false,
                    hideCamera = true,
                    gridConfig = GridConfig(3, LinearLayoutManager.HORIZONTAL),
                )
            )
        )
    }

    override fun onDisplayGallery(
        width: Int,
        height: Int,
        scanEntity: ScanEntity,
        container: FrameLayout,
        checkBox: TextView
    ) {
        container.removeAllViews()
        val imageView = GalleryImageView(container.context)
        Glide.with(container.context)
            .load(scanEntity.uri)
            .apply(RequestOptions().centerCrop().override(width, height))
            .into(imageView)
        container.addView(imageView, FrameLayout.LayoutParams(width, height))
    }

    override fun onGalleryCreated(
        delegate: IScanDelegate,
        bundle: GalleryConfigs,
        savedInstanceState: Bundle?
    ) {
        delegate.rootView.setBackgroundColor(Color.BLACK)
    }

    override fun onGalleryResource(context: Context, scanEntity: ScanEntity) {
        showDialog(scanEntity.toString())
    }

}