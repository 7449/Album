package com.album.sample

import android.content.Context
import android.net.Uri
import android.widget.ImageView

import com.album.AlbumConstant
import com.album.listener.AlbumImageLoader
import com.album.entity.AlbumEntity
import com.album.entity.FinderEntity
import com.album.annotation.FrescoType
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.drawable.ScalingUtils
import com.facebook.drawee.generic.GenericDraweeHierarchy
import com.facebook.drawee.interfaces.DraweeController
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.common.RotationOptions
import com.facebook.imagepipeline.request.ImageRequest
import com.facebook.imagepipeline.request.ImageRequestBuilder

/**
 * by y on 20/08/2017.
 */

class SimpleFrescoAlbumImageLoader : AlbumImageLoader {

    override fun displayAlbum(view: ImageView, width: Int, height: Int, albumEntity: AlbumEntity) {
        val simpleDraweeView = view as SimpleDraweeView
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
    }

    override fun displayAlbumThumbnails(view: ImageView, finderEntity: FinderEntity) {
        val simpleDraweeView = view as SimpleDraweeView
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
    }

    override fun displayPreview(view: ImageView, albumEntity: AlbumEntity) {
        val simpleDraweeView = view as SimpleDraweeView
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
    }

    override fun frescoView(context: Context, @FrescoType type: Int): ImageView? {
        when (type) {
            AlbumConstant.TYPE_FRESCO_ALBUM -> return SimpleDraweeView(context)
            AlbumConstant.TYPE_FRESCO_PREVIEW -> return SimpleDraweeView(context)
        }
        return null
    }
}
