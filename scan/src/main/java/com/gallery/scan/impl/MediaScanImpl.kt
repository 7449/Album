package com.gallery.scan.impl

import android.provider.MediaStore
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.loader.app.LoaderManager
import com.gallery.scan.MediaScanContext
import com.gallery.scan.args.MediaResult
import com.gallery.scan.callbacks.MediaScanTaskCallbacks

class MediaScanImpl<E>(
    mediaScanContext: MediaScanContext,
    private val action: MediaResult<E>.() -> Unit
) : LifecycleEventObserver {

    companion object {
        private const val ID = 1
    }

    private val lifecycleOwner: LifecycleOwner = mediaScanContext.context()
    private val loaderManager = LoaderManager.getInstance(mediaScanContext.context())
    private val factory = mediaScanContext.factory
    private val loaderArgs = mediaScanContext.loaderArgs
    private val context by lazy {
        when (lifecycleOwner) {
            is Fragment -> lifecycleOwner.requireContext().applicationContext
            is FragmentActivity -> lifecycleOwner.applicationContext
            else -> throw KotlinNullPointerException("context == null")
        }
    }

    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (source.lifecycle.currentState == Lifecycle.State.DESTROYED) {
            lifecycleOwner.lifecycle.removeObserver(this)
            destroy()
        }
    }

    /*
    *  文件夹扫描
    *  根据文件夹的parentId获取相对应的实体类集合
    *  */
    fun multiple(parent: Long) {
        if (loaderManager.hasRunningLoaders()) return
        loaderManager.restartLoader(ID,
            bundleOf(MediaStore.Files.FileColumns.PARENT to parent),
            MediaScanTaskCallbacks(context, factory, loaderArgs) {
                action.invoke(MediaResult.Multiple(it))
                destroy()
            })
    }

    /*
    *  用于拍照之后的扫描
    *  根据文件Id获取相对应的实体类
    *  */
    fun single(id: Long) {
        if (loaderManager.hasRunningLoaders()) return
        loaderManager.restartLoader(ID,
            bundleOf(MediaStore.Files.FileColumns._ID to id),
            MediaScanTaskCallbacks<E>(context, factory, loaderArgs) {
                action.invoke(MediaResult.Single(it.firstOrNull()))
                destroy()
            })
    }

    private fun destroy() {
        loaderManager.destroyLoader(ID)
    }

}