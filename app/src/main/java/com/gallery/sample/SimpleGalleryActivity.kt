package com.gallery.sample

import android.net.Uri
import android.view.ViewGroup
import android.widget.ImageView
import androidx.kotlin.expand.toastExpand
import com.android.banner.BannerInfo
import com.android.banner.ImageLoaderManager
import com.android.banner.imageLoaderManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.gallery.core.callback.IGalleryInterceptor
import com.gallery.core.ext.externalUri
import com.gallery.scan.ScanEntity
import com.gallery.ui.activity.GalleryActivity
import kotlinx.android.synthetic.main.simple_gallery_layout.*

class SimpleGalleryActivity : GalleryActivity(R.layout.simple_gallery_layout), IGalleryInterceptor {

    override fun onScanSuccess(scanEntities: ArrayList<ScanEntity>) {
        val arrayList = ArrayList<SimpleGallery>()
        scanEntities.forEach { arrayList.add(SimpleGallery(it.externalUri())) }
        banner
                .imageLoaderManager { GlideAppSimpleImageManager() }
                .resource(arrayList)
    }

    override fun onUCropResources(uri: Uri) {
        uri.toString().toastExpand(this)
    }

    override fun onGalleryResources(entities: List<ScanEntity>) {
        entities.toString().toastExpand(this)
    }

    override fun onCustomPhotoCrop(uri: Uri): Boolean {
        "custom crop".toastExpand(this)
        return true
    }
}

class GlideAppSimpleImageManager : ImageLoaderManager<SimpleGallery> {

    private val requestOptions: RequestOptions = RequestOptions().centerCrop()

    override fun display(container: ViewGroup, info: SimpleGallery, position: Int): ImageView {
        val imageView = ImageView(container.context)
        Glide.with(imageView.context)
                .applyDefaultRequestOptions(requestOptions)
                .load(info.bannerUrl)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .fallback(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView)
        return imageView
    }

}

class SimpleGallery(image: Uri) : BannerInfo {
    override val bannerUrl = image
    override val bannerTitle = ""
}