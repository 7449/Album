package com.album

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import com.yalantis.ucrop.UCrop

const val WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 0
const val CAMERA_REQUEST_CODE = 1

const val PREVIEW_LOADER_ID = -111
const val ALBUM_LOADER_ID = -112
const val RESULT_LOADER_ID = -113
const val FINDER_LOADER_ID = -114

const val IMAGE = 0
const val VIDEO = 1
const val MIXING = 2

const val EXTRA_ALBUM_OPTIONS = BuildConfig.APPLICATION_ID + ".Album.Options"
const val EXTRA_ALBUM_UI_OPTIONS = BuildConfig.APPLICATION_ID + ".Album.Ui.Options"

const val FINDER_ALL_DIR_NAME = "全部"
const val CAMERA = "Album:Camera"

const val ITEM_CAMERA = 1111
const val TYPE_PREVIEW_CODE = 112

const val TYPE_ALBUM_STATE_SELECT = "state_select"
const val TYPE_ALBUM_STATE_BUCKET_ID = "state_bucket_id"
const val TYPE_ALBUM_STATE_FINDER_NAME = "state_finder_name"
const val TYPE_ALBUM_STATE_IMAGE_PATH = "state_image_path"

const val TYPE_PREVIEW_STATE_SELECT_ALL = "state_preview_all"
const val TYPE_PREVIEW_KEY = "preview_list"
const val TYPE_PREVIEW_REFRESH_UI = "preview_refresh_ui"
const val TYPE_PREVIEW_FINISH = "preview_finish"
const val TYPE_PREVIEW_BUCKET_ID = "preview_bucket_id"
const val TYPE_PREVIEW_BUTTON_KEY = "preview_button"
const val TYPE_PREVIEW_POSITION_KEY = "preview_position"

const val TYPE_PERMISSIONS_ALBUM = 0
const val TYPE_PERMISSIONS_CAMERA = 1

const val TYPE_RESULT_CAMERA = 0
const val TYPE_RESULT_CROP = 1

const val CUSTOMIZE_CAMERA_RESULT_CODE = 113
const val CUSTOMIZE_CAMERA_RESULT_PATH_KEY = "customize_camera_path"

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
    var albumListener: AlbumListener? = null
    var customCameraListener: AlbumCustomCameraListener? = null
    var emptyClickListener: OnEmptyClickListener? = null
    var initList: ArrayList<AlbumEntity>? = null
}

fun Album.start(context: Context, cls: Class<*>) {
    start(context, AlbumBundle(), cls)
}

fun Album.start(context: Context, albumBundle: AlbumBundle, cls: Class<*>) {
    context.startActivity(Intent(context, cls).apply {
        putExtras(Bundle().apply { putParcelable(EXTRA_ALBUM_OPTIONS, albumBundle) })
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    })
}

fun Album.start(context: Context, albumBundle: AlbumBundle, uiBundle: Parcelable, cls: Class<*>) {
    context.startActivity(Intent(context, cls).apply {
        putExtras(Bundle().apply {
            putParcelable(EXTRA_ALBUM_OPTIONS, albumBundle)
            putParcelable(EXTRA_ALBUM_UI_OPTIONS, uiBundle)
        })
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    })
}