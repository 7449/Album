package com.gallery.sample

import android.content.Context
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gallery.compat.fragment.GalleryCompatFragment
import com.gallery.compat.internal.simple.SimpleGalleryCallback
import com.gallery.compat.widget.GalleryImageView
import com.gallery.core.GalleryConfigs
import com.gallery.core.GridConfig
import com.gallery.core.callback.IGalleryImageLoader
import com.gallery.core.entity.ScanEntity

abstract class GalleryListActivity : AppCompatActivity(), SimpleGalleryCallback,
    IGalleryImageLoader {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val newInstance = GalleryCompatFragment.newInstance(
            GalleryConfigs(
                radio = true, crop = false, hideCamera = true,
                gridConfig = GridConfig(4, RecyclerView.HORIZONTAL),
            )
        )
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.gallery_root_view, newInstance)
            .commitAllowingStateLoss()
    }

    override fun onDisplayHomeGallery(
        width: Int,
        height: Int,
        entity: ScanEntity,
        container: FrameLayout
    ) {
        container.removeAllViews()
        val imageView = GalleryImageView(container.context)
        Glide.with(container.context)
            .load(entity.uri)
            .apply(RequestOptions().centerCrop().override(width, height))
            .into(imageView)
        container.addView(imageView, FrameLayout.LayoutParams(width, height))
    }

    override fun onGalleryResource(context: Context, scanEntity: ScanEntity) {
        AlertDialog
            .Builder(this)
            .setMessage(scanEntity.toString())
            .show()
    }

}