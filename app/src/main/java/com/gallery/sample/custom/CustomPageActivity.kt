package com.gallery.sample.custom

import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.android.banner.BannerInfo
import com.android.banner.OnBannerImageLoader
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.gallery.core.callback.IGalleryInterceptor
import com.gallery.core.expand.safeToastExpand
import com.gallery.sample.R
import com.gallery.scan.ScanEntity
import com.gallery.scan.types.externalUriExpand
import com.gallery.ui.page.GalleryActivity
import kotlinx.android.synthetic.main.simple_gallery_layout.*

class CustomPageActivity : GalleryActivity(R.layout.simple_gallery_layout), IGalleryInterceptor {

    override fun onScanSuccess(scanEntities: ArrayList<ScanEntity>) {
        super.onScanSuccess(scanEntities)
        val arrayList = ArrayList<SimpleGallery>()
        scanEntities.forEach { arrayList.add(SimpleGallery(it.externalUriExpand)) }
        banner
                .setOnBannerImageLoader(GlideAppSimpleImageManager())
                .resource(arrayList)
    }

    override fun onGalleryResources(entities: ArrayList<ScanEntity>) {
        entities.toString().safeToastExpand(this)
    }

    class GlideAppSimpleImageManager : OnBannerImageLoader<SimpleGallery> {

        override fun instantiateItem(container: ViewGroup, info: SimpleGallery, position: Int): View {
            val imageView = ImageView(container.context)
            Glide.with(imageView.context)
                    .applyDefaultRequestOptions(RequestOptions().centerCrop())
                    .load(info.bannerUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView)
            return imageView
        }

    }

    class SimpleGallery(image: Uri) : BannerInfo {
        override val bannerUrl = image
        override val bannerTitle = ""
    }
}
