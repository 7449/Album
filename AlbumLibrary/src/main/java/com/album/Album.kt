package com.album

import android.view.View
import com.album.core.scan.AlbumEntity
import com.album.core.ui.AlbumBaseFragment
import com.album.listener.AlbumImageLoader
import com.album.listener.OnAlbumListener
import com.yalantis.ucrop.UCrop

/**
 * fragment进入到预览页的 request_code
 * 因为需要操作一些数据,依赖于 setResult
 * 例如预览时多选数据变化,按Toolbar返回时是否需要刷新数据
 */
const val TYPE_PREVIEW_REQUEST_CODE = 112


class Album {

    companion object {
        @JvmStatic
        val instance by lazy { Album() }

        @JvmStatic
        fun destroy() = instance.apply {
            options = null
            initList = null
            imageLoaderDestroy()
            listenerDestroy()
        }

        @JvmStatic
        fun listenerDestroy() = instance.apply {
            albumListener = null
            customCameraListener = null
            albumEmptyClickListener = null
        }

        @JvmStatic
        fun removeInitList() = instance.initList?.clear()

        @JvmStatic
        fun imageLoaderDestroy() = instance.apply {
            albumImageLoader = null
        }
    }

    var options: UCrop.Options? = null
    var albumImageLoader: AlbumImageLoader? = null
    var albumListener: OnAlbumListener? = null
    var customCameraListener: ((fragment: AlbumBaseFragment) -> Unit)? = null
    var albumEmptyClickListener: ((view: View) -> Boolean)? = null
    var initList: ArrayList<AlbumEntity>? = null
}