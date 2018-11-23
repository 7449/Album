package com.album

import androidx.recyclerview.widget.LinearLayoutManager
import com.album.util.ScanType

/**
 * by y on 14/08/2017.
 */

class AlbumConfig {

    var scanType: ScanType = ScanType.IMAGE
    var hideCamera: Boolean = false
    var isRadio: Boolean = false
    var isCrop: Boolean = true
    var cameraCrop: Boolean = false
    var isPermissionsDeniedFinish: Boolean = false
    var isFrescoImageLoader: Boolean = false
    var multipleMaxCount = 9
    var cameraPath: String? = null
    var uCropPath: String? = null
    var count = 500
    var sdName = "根目录"
    var filterImg: Boolean = false
    var cropErrorFinish: Boolean = true
    var selectImageFinish: Boolean = true
    var cropFinish: Boolean = true
    var noPreview: Boolean = false
    var spanCount = 3
    var dividerWidth = 10
    var orientation: Int = LinearLayoutManager.VERTICAL
        private set

    var albumContentViewCameraTips = R.string.album_image_camera_tv_tips
    var albumContentViewCameraTipsSize = 18
    var albumContentViewCameraTipsColor = R.color.colorAlbumContentViewTipsColorDay
    var albumContentViewCameraBackgroundColor = R.color.colorAlbumContentViewBackgroundColorColorDay
    var albumContentViewBackground = R.color.colorAlbumContentViewBackgroundDay
    var albumContentViewCameraDrawable = R.drawable.ic_camera_alt_black_24dp
    var albumContentViewCameraDrawableColor = R.color.colorAlbumContentViewCameraDrawableColorDay
    var albumContentItemCheckBoxDrawable = R.drawable.selector_album_item_check
    var albumContentEmptyDrawable = R.drawable.ic_camera_alt_black_24dp
    var albumContentEmptyDrawableColor = R.color.colorAlbumContentEmptyDrawableColorDay


    var previewFinishRefresh: Boolean = false
    var previewBackRefresh: Boolean = false
    //////////////  album toolbar //////
    var albumStatusBarColor = R.color.colorAlbumStatusBarColorDay
    var albumToolbarBackground = R.color.colorAlbumToolbarBackgroundDay
    var albumToolbarIcon = R.drawable.ic_action_back_day
    var albumToolbarIconColor = R.color.colorAlbumToolbarIconColorDay
    var albumToolbarTextColor = R.color.colorAlbumToolbarTextColorDay
    var albumToolbarText = R.string.album_name
    var albumToolbarElevation = 6f
    ////////////// album bottom view //////
    var albumBottomViewBackground = R.color.colorAlbumBottomViewBackgroundDay
    var albumBottomFinderTextSize = 16
    var albumBottomFinderTextBackground = -1
    var albumBottomFinderTextColor = R.color.colorAlbumBottomFinderTextColorDay
    var albumBottomFinderTextCompoundDrawable = R.drawable.ic_action_album_finder_day
    var albumBottomFinderTextDrawableColor = R.color.colorAlbumBottomFinderTextDrawableColorDay
    var albumBottomPreViewText = R.string.album_preview
    var albumBottomPreViewTextSize = 16
    var albumBottomPreViewTextColor = R.color.colorAlbumBottomPreViewTextColorDay
    var albumBottomPreviewTextBackground = -1
    var albumBottomSelectText = R.string.album_select
    var albumBottomSelectTextSize = 16
    var albumBottomSelectTextColor = R.color.colorAlbumBottomSelectTextColorDay
    var albumBottomSelectTextBackground = -1
    ///////////// album list popup window /////
    var albumListPopupWidth = 600
    var albumListPopupHorizontalOffset = 0
    var albumListPopupVerticalOffset = 0
    var albumListPopupItemBackground = R.color.colorAlbumListPopupItemBackgroundDay
    var albumListPopupItemTextColor = R.color.colorAlbumListPopupItemTextColorDay
    //////////// album content view ////////

    /////////// album preview ////////
    var albumPreviewTitle = R.string.preview_title
    var albumPreviewBackground = R.color.colorAlbumPreviewBackgroundDay
    var albumPreviewBottomViewBackground = R.color.colorAlbumPreviewBottomViewBackgroundDay
    var albumPreviewBottomOkText = R.string.preview_select
    var albumPreviewBottomOkTextColor = R.color.colorAlbumPreviewBottomViewOkColorDay
    var albumPreviewBottomCountTextColor = R.color.colorAlbumPreviewBottomViewCountColorDay
    var albumPreviewBottomOkTextSize = 16
    var albumPreviewBottomCountTextSize = 16

    constructor()

    constructor(type: Int) {
        when (type) {
            AlbumConstant.TYPE_NIGHT -> {
                albumStatusBarColor = R.color.colorAlbumStatusBarColorNight
                albumToolbarBackground = R.color.colorAlbumToolbarBackgroundNight
                albumToolbarTextColor = R.color.colorAlbumToolbarTextColorNight
                albumToolbarIconColor = R.color.colorAlbumToolbarIconColorNight

                albumBottomViewBackground = R.color.colorAlbumBottomViewBackgroundNight
                albumBottomFinderTextColor = R.color.colorAlbumBottomFinderTextColorNight
                albumBottomFinderTextDrawableColor = R.color.colorAlbumBottomFinderTextDrawableColorNight
                albumBottomPreViewTextColor = R.color.colorAlbumBottomPreViewTextColorNight
                albumBottomSelectTextColor = R.color.colorAlbumBottomSelectTextColorNight

                albumListPopupItemTextColor = R.color.colorAlbumListPopupItemTextColorNight
                albumListPopupItemBackground = R.color.colorAlbumListPopupItemBackgroundNight

                albumContentViewBackground = R.color.colorAlbumContentViewBackgroundNight
                albumContentViewCameraDrawableColor = R.color.colorAlbumContentViewCameraDrawableColorNight
                albumContentViewCameraTipsColor = R.color.colorAlbumContentViewTipsColorNight
                albumContentViewCameraBackgroundColor = R.color.colorAlbumContentViewBackgroundColorColorNight
                albumContentEmptyDrawableColor = R.color.colorAlbumContentEmptyDrawableColorNight

                albumPreviewBackground = R.color.colorAlbumPreviewBackgroundNight
                albumPreviewBottomViewBackground = R.color.colorAlbumPreviewBottomViewBackgroundNight
                albumPreviewBottomOkTextColor = R.color.colorAlbumPreviewBottomViewOkColorNight
                albumPreviewBottomCountTextColor = R.color.colorAlbumPreviewBottomViewCountColorNight
            }
            else -> throw RuntimeException("type")
        }
    }
}
