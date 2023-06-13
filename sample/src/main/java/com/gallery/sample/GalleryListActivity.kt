package com.gallery.sample

import android.os.Bundle
import android.provider.MediaStore
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import develop.file.gallery.args.GalleryConfigs
import develop.file.gallery.args.GridConfig
import develop.file.gallery.callback.IGalleryImageLoader
import develop.file.gallery.compat.extensions.callbacks.SimpleGalleryCallback
import develop.file.gallery.compat.fragment.GalleryGridFragment
import develop.file.gallery.compat.widget.GalleryImageView
import develop.file.gallery.entity.ScanEntity
import develop.file.media.Types

abstract class GalleryListActivity : AppCompatActivity(), SimpleGalleryCallback,
    IGalleryImageLoader {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val newInstance = GalleryGridFragment.newInstance(
            GalleryConfigs(
                radio = true,
                crop = false,
                hideCamera = true,
                sort = Types.Sort.ASC to MediaStore.Files.FileColumns.DATE_MODIFIED,
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

    override fun onGalleryResource(entity: ScanEntity) {
        AlertDialog
            .Builder(this)
            .setMessage(entity.toString())
            .show()
    }

}