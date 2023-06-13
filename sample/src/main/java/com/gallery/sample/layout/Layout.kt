package com.gallery.sample.layout

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.banner.BannerLayout
import androidx.banner.listener.BannerItem
import androidx.banner.listener.OnBannerImageLoader
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gallery.sample.R
import develop.file.gallery.ui.material.activity.MaterialGalleryActivity

private const val IMAGE_URL_1 =
    "https://p1.img.cctvpic.com/photoworkspace/2018/05/18/2018051814594647287.jpg"
private const val IMAGE_URL_2 =
    "https://p1.img.cctvpic.com/photoworkspace/2018/05/18/2018051814220084352.jpg"
private const val IMAGE_URL_3 =
    "https://p1.img.cctvpic.com/photoworkspace/2018/05/18/2018051814245872100.jpg"
private const val IMAGE_URL_4 =
    "https://p1.img.cctvpic.com/photoworkspace/2018/05/18/2018051814175817985.jpg"

private fun newModel(): ArrayList<SimpleBannerItem> {
    val modules = ArrayList<SimpleBannerItem>()
    modules.add(SimpleBannerItem(IMAGE_URL_1))
    modules.add(SimpleBannerItem(IMAGE_URL_2))
    modules.add(SimpleBannerItem(IMAGE_URL_3))
    modules.add(SimpleBannerItem(IMAGE_URL_4))
    return modules
}

private class SimpleBannerItem(image: Any) : BannerItem {
    override val bannerUrl: String = image.toString()
}

private class GlideImageLoader : OnBannerImageLoader<SimpleBannerItem> {
    override fun instantiateItem(container: ViewGroup, item: SimpleBannerItem): View {
        return ImageView(container.context).apply {
            Glide.with(container.context)
                .applyDefaultRequestOptions(RequestOptions().centerCrop())
                .load(item.bannerUrl)
                .into(this)
        }
    }
}

class LayoutActivity : MaterialGalleryActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val adViewRoot = View.inflate(this, R.layout.simple_layout_ad_view, null)
        val viewGroup = findViewById<ViewGroup>(com.gallery.ui.material.R.id.galleryRootView)
        viewGroup.addView(adViewRoot, 1)
        viewGroup.findViewById<BannerLayout>(R.id.banner)
            .setOnBannerImageLoader(GlideImageLoader())
            .resource(newModel())
    }

}