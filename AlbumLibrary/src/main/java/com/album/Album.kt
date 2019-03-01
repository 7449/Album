package com.album

import com.album.core.scan.AlbumEntity
import com.album.listener.AlbumCustomCameraListener
import com.album.listener.AlbumImageLoader
import com.album.listener.OnAlbumListener
import com.album.listener.OnEmptyClickListener
import com.yalantis.ucrop.UCrop

/**
 * 扫描全部图片时显示的文件夹名称
 */
internal const val FINDER_ALL_DIR_NAME = "全部"

/**
 * 相机Type
 */
internal const val CAMERA = "Album:Camera"

/**
 *  [AlbumBundle] 主要数据的Key
 */
const val EXTRA_ALBUM_OPTIONS = BuildConfig.APPLICATION_ID + ".Album.Options"

/**
 * 使用内置的UI框架时的数据Key
 */
const val EXTRA_ALBUM_UI_OPTIONS = BuildConfig.APPLICATION_ID + ".Album.Ui.Options"

/**
 * 保存已选择信息的Bundle Key
 */
const val TYPE_ALBUM_STATE_SELECT = "album:state_select"

/**
 * 保存当前bucketId的Bundle Key
 */
const val TYPE_ALBUM_STATE_BUCKET_ID = "album:state_bucket_id"

/**
 * 保存当前文件夹名称的Bundle Key
 */
const val TYPE_ALBUM_STATE_FINDER_NAME = "album:state_finder_name"

/**
 * 保存保存的图片路径的Bundle Key
 */
const val TYPE_ALBUM_STATE_IMAGE_PATH = "album:state_image_path"

/**
 * 预览页保存已选择信息的Bundle Key
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
 * 进入预览页 bucketId Bundle Key
 * 如果是[PREVIEW_BUTTON_KEY]则是只查看已选中数据跳转预览页
 * 和[TYPE_PREVIEW_KEY]相呼应,如果是只查看选中数据则缺一不可
 */
const val TYPE_PREVIEW_BUCKET_ID = "preview_bucket_id"

/**
 * 只查看选中数据的 "bucketId"
 */
const val PREVIEW_BUTTON_KEY = "preview_button"

/**
 * 扫描图片成功之后需要定位的位置 Key
 */
const val TYPE_PREVIEW_POSITION_KEY = "preview_position"

/**
 * 刷新图库的Type,为拍照成功之后刷新的Type
 */
const val TYPE_RESULT_CAMERA = 0

/**
 * 刷新图库的Type,为裁剪成功之后刷新的Type
 */
const val TYPE_RESULT_CROP = 1

class Album {
    companion object {
        val instance by lazy { Album() }
        fun destroy() {
            instance.apply {
                options = null
                albumListener = null
                customCameraListener = null
                emptyClickListener = null
                albumImageLoader = null
                initList = null
            }
        }
    }

    var options: UCrop.Options? = null
    var albumImageLoader: AlbumImageLoader? = null
    var albumListener: OnAlbumListener? = null
    var customCameraListener: AlbumCustomCameraListener? = null
    var emptyClickListener: OnEmptyClickListener? = null
    var initList: ArrayList<AlbumEntity>? = null
}

