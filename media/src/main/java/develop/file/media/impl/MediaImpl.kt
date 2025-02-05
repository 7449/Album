package develop.file.media.impl

import android.provider.MediaStore
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.loader.app.LoaderManager
import develop.file.media.MediaContext
import develop.file.media.args.MediaResult
import develop.file.media.callbacks.MediaLoaderTaskCallbacks

class MediaImpl<E>(
    mediaContext: MediaContext,
    private val action: MediaResult<E>.() -> Unit
) : LifecycleEventObserver {

    companion object {
        private const val ID = 1
    }

    private val lifecycleOwner: LifecycleOwner = mediaContext.context()
    private val loaderManager = LoaderManager.getInstance(mediaContext.context())
    private val factory = mediaContext.factory
    private val loaderArgs = mediaContext.args
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
            MediaLoaderTaskCallbacks(context, factory, loaderArgs) {
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
            MediaLoaderTaskCallbacks<E>(context, factory, loaderArgs) {
                action.invoke(MediaResult.Single(it.firstOrNull()))
                destroy()
            })
    }

    private fun destroy() {
        loaderManager.destroyLoader(ID)
    }

}