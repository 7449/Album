package com.album.sample.imageloader

import android.net.Uri
import android.view.View
import android.widget.FrameLayout
import com.album.AlbumEntity
import com.album.AlbumImageLoader
import com.album.FinderEntity
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.drawable.ScalingUtils
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.common.RotationOptions
import com.facebook.imagepipeline.request.ImageRequest
import com.facebook.imagepipeline.request.ImageRequestBuilder

/**
 * by y on 20/08/2017.
 */

class SimpleFrescoAlbumImageLoader : AlbumImageLoader {

    override fun displayAlbum(width: Int, height: Int, albumEntity: AlbumEntity, container: FrameLayout): View {
        val simpleDraweeView = SimpleDraweeView(container.context)
        val uri = Uri.parse("file://" + albumEntity.path)
        val request = ImageRequestBuilder
                .newBuilderWithSource(uri)
                .setRotationOptions(RotationOptions.autoRotate())
                .setResizeOptions(ResizeOptions(width, height))
                .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)
                .build()
        val controller = Fresco.newDraweeControllerBuilder()
                .setTapToRetryEnabled(true)
                .setImageRequest(request)
                .setAutoPlayAnimations(true)
                .setOldController(simpleDraweeView.controller)
                .build()
        val hierarchy = simpleDraweeView.hierarchy
        //        hierarchy.setPlaceholderImage(R.drawable.ic_launcher);
        hierarchy.actualImageScaleType = ScalingUtils.ScaleType.CENTER_CROP
        simpleDraweeView.controller = controller
        return simpleDraweeView
    }

    override fun displayAlbumThumbnails(finderEntity: FinderEntity, container: FrameLayout): View {
        val simpleDraweeView = SimpleDraweeView(container.context)
        val uri = Uri.parse("file://" + finderEntity.thumbnailsPath)
        val request = ImageRequestBuilder
                .newBuilderWithSource(uri)
                .setRotationOptions(RotationOptions.autoRotate())
                .setResizeOptions(ResizeOptions(50, 50))
                .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)
                .build()
        val controller = Fresco.newDraweeControllerBuilder()
                .setTapToRetryEnabled(true)
                .setImageRequest(request)
                .setOldController(simpleDraweeView.controller)
                .build()
        val hierarchy = simpleDraweeView.hierarchy
        hierarchy.actualImageScaleType = ScalingUtils.ScaleType.CENTER_CROP
        simpleDraweeView.controller = controller
        return simpleDraweeView
    }

    override fun displayPreview(albumEntity: AlbumEntity, container: FrameLayout): View {
        val simpleDraweeView = SimpleDraweeView(container.context)
        val uri = Uri.parse("file://" + albumEntity.path)
        val request = ImageRequestBuilder
                .newBuilderWithSource(uri)
                .setRotationOptions(RotationOptions.autoRotate())
                .setResizeOptions(ResizeOptions(400, 350))
                .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)
                .build()
        val controller = Fresco.newDraweeControllerBuilder()
                .setTapToRetryEnabled(true)
                .setImageRequest(request)
                .setOldController(simpleDraweeView.controller)
                .build()
        val hierarchy = simpleDraweeView.hierarchy
        hierarchy.actualImageScaleType = ScalingUtils.ScaleType.CENTER_CROP
        simpleDraweeView.controller = controller
        return simpleDraweeView
    }
}