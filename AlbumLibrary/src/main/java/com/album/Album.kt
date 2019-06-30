package com.album

import android.view.View
import com.album.core.scan.AlbumEntity
import com.album.core.ui.AlbumBaseFragment
import com.album.listener.AlbumImageLoader
import com.album.listener.OnAlbumListener
import com.yalantis.ucrop.UCrop


/**
 * 预览页保存已选择信息的Bundle Key
 * 如果是点击图片进入则结果为Null
 */
const val TYPE_PREVIEW_STATE_SELECT_ALL = "state_preview_all"

/**
 * fragment进入到预览页的 request_code
 * 因为需要操作一些数据,依赖于 setResult
 * 例如预览时多选数据变化,按Toolbar返回时是否需要刷新数据
 */
const val TYPE_PREVIEW_REQUEST_CODE = 112

/**
 * 跳转预览页面时携带选中数据的key
 * 数据可能为空
 */
const val TYPE_PREVIEW_KEY = "preview_multiple_list"

/**
 * 是否刷新前一个页面的数据
 * 例如预览时多选数据变化,按Toolbar返回时是否需要刷新数据
 * 具体可见 UI Library
 */
const val TYPE_PREVIEW_REFRESH_UI = "preview_refresh_ui"

/**
 * 预览页确定数据之后是否销毁前一个页面
 */
const val TYPE_PREVIEW_SELECT_OK_FINISH = "preview_select_ok_finish"

/**
 * 进入预览页 parent Bundle Key
 */
const val TYPE_PREVIEW_PARENT = "preview_parent"

/**
 * 扫描图片成功之后需要定位的位置 Key
 */
const val TYPE_PREVIEW_POSITION_KEY = "preview_position"

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