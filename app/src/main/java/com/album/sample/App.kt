package com.album.sample

import android.app.Application

import com.facebook.drawee.backends.pipeline.Fresco
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.QueueProcessingType

/**
 * by y on 15/08/2017.
 */

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)
        val config = ImageLoaderConfiguration.Builder(this)
        config.threadPriority(Thread.NORM_PRIORITY - 2)
        config.denyCacheImageMultipleSizesInMemory()
        config.diskCacheFileNameGenerator(Md5FileNameGenerator())
        config.diskCacheSize(50 * 1024 * 1024)
        config.tasksProcessingOrder(QueueProcessingType.LIFO)
        ImageLoader.getInstance().init(config.build())
    }
}
