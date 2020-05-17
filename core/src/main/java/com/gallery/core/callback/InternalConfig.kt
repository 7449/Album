package com.gallery.core.callback

import android.net.Uri
import android.os.Bundle
import com.gallery.scan.ScanEntity

internal object InternalConfig {
    /**
     * 相机占位的[ScanEntity.parent]
     */
    internal const val CAMERA_PARENT_ID = -11111L

    /**
     * 当前扫描时的parentId
     * 用于横竖屏切换时更新数据
     */
    internal const val PARENT_ID = "galleryStartParentId"

    /**
     * 当前拍照之后的[Uri]
     * 有一种情况为横竖屏切换,横屏点进去,然后竖屏拍照返回之后,切换之后会重走生命周期,
     * 所以这里需要保存下当时拍照时的[Uri]
     */
    internal const val IMAGE_URL = "galleryStartImageUri"

    /**
     * 横竖屏切换时保存选中的数据
     * [ArrayList<ScanEntity>] Bundle key
     * [Bundle.putParcelableArrayList]
     */
    internal const val SELECT = "galleryStartSelectEntities"
}