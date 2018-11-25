package com.album

import android.content.Context
import android.content.Intent
import com.album.ui.activity.AlbumActivity
import com.album.ui.activity.PreviewActivity
import com.yalantis.ucrop.UCrop

/**
 * by y on 14/08/2017.
 */
private const val EXTRA_ALBUM_CONFIG = BuildConfig.APPLICATION_ID + ".Config"

const val EXTRA_ALBUM_CROP_PATH = "$EXTRA_ALBUM_CONFIG.CropPath"
const val EXTRA_ALBUM_CAMERA_PATH = "$EXTRA_ALBUM_CONFIG.CameraPath"
const val EXTRA_ALBUM_CAMERA_TEXT = "$EXTRA_ALBUM_CONFIG.CameraText"
const val EXTRA_ALBUM_CAMERA_TEXT_SIZE = "$EXTRA_ALBUM_CONFIG.CameraTextSize"
const val EXTRA_ALBUM_CAMERA_TEXT_COLOR = "$EXTRA_ALBUM_CONFIG.CameraTextColor"
const val EXTRA_ALBUM_CAMERA_BACKGROUND_COLOR = "$EXTRA_ALBUM_CONFIG.CameraBackgroundColor"
const val EXTRA_ALBUM_CAMERA_DRAWABLE = "$EXTRA_ALBUM_CONFIG.CameraDrawable"
const val EXTRA_ALBUM_CAMERA_DRAWABLE_COLOR = "$EXTRA_ALBUM_CONFIG.CameraDrawableColor"

const val EXTRA_ALBUM_ROOT_VIEW_COLOR = "$EXTRA_ALBUM_CONFIG.RootViewColor"
const val EXTRA_ALBUM_CHECKOUT_BOX_SELECTOR = "$EXTRA_ALBUM_CONFIG.CheckoutBoxSelector"
const val EXTRA_ALBUM_PHOTO_EMPTY_DRAWABLE = "$EXTRA_ALBUM_CONFIG.PhotoEmptyDrawable"
const val EXTRA_ALBUM_PHOTO_EMPTY_DRAWABLE_COLOR = "$EXTRA_ALBUM_CONFIG.PhotoEmptyDrawableColor"

const val EXTRA_ALBUM_HIDE_CAMERA = "$EXTRA_ALBUM_CONFIG.HideCamera"
const val EXTRA_ALBUM_RADIO = "$EXTRA_ALBUM_CONFIG.Radio"
const val EXTRA_ALBUM_CROP = "$EXTRA_ALBUM_CONFIG.Crop"
const val EXTRA_ALBUM_MULTIPLE_MAX_COUNT = "$EXTRA_ALBUM_CONFIG.MultipleMaxCount"
const val EXTRA_ALBUM_SCAN_MAX_COUNT = "$EXTRA_ALBUM_CONFIG.ScanMaxCount"
const val EXTRA_ALBUM_SD_NAME = "$EXTRA_ALBUM_CONFIG.SdName"
const val EXTRA_ALBUM_FILTER_IMAGE = "$EXTRA_ALBUM_CONFIG.FilterImage"
const val EXTRA_ALBUM_PREVIEW = "$EXTRA_ALBUM_CONFIG.Preview"
const val EXTRA_ALBUM_SPAN_COUNT = "$EXTRA_ALBUM_CONFIG.SpanCount"
const val EXTRA_ALBUM_DIVIDER_WIDTH = "$EXTRA_ALBUM_CONFIG.DividerWidth"
const val EXTRA_ALBUM_ORIENTATION = "$EXTRA_ALBUM_CONFIG.Orientation"

const val EXTRA_ALBUM_PERMISSIONS_DENIED_FINISH = "$EXTRA_ALBUM_CONFIG.PermissionsDeniedFinish"
const val EXTRA_ALBUM_CROP_ERROR_FINISH = "$EXTRA_ALBUM_CONFIG.CropErrorFinish"
const val EXTRA_ALBUM_SELECT_IMAGE_FINISH = "$EXTRA_ALBUM_CONFIG.SelectImageFinish"
const val EXTRA_ALBUM_CROP_FINISH = "$EXTRA_ALBUM_CONFIG.CropFinish"


class Album {

    companion object {
        val instance by lazy { Album() }
        fun reset() {
            instance.apply {
                config = AlbumConfig()
                albumImageLoader = SimpleGlideAlbumImageLoader()
                options = UCrop.Options()
                albumListener = SimpleAlbumListener()
                albumCameraListener = null
                albumVideoListener = null
                emptyClickListener = null
                albumEntityList = null
                albumClass = AlbumActivity::class.java
                previewClass = PreviewActivity::class.java
            }
        }
    }

    var config = AlbumConfig()
    var albumImageLoader: AlbumImageLoader = SimpleGlideAlbumImageLoader()
    var options = UCrop.Options()
    var albumListener: AlbumListener = SimpleAlbumListener()
    var albumCameraListener: AlbumCameraListener? = null
    var albumVideoListener: AlbumVideoListener? = null
    var emptyClickListener: OnEmptyClickListener? = null
    var albumEntityList: ArrayList<AlbumEntity>? = null
    var albumClass: Class<*> = AlbumActivity::class.java
    var previewClass: Class<*> = PreviewActivity::class.java

    fun start(context: Context) {
        val intent = Intent(context, albumClass)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
}
