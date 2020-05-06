package com.gallery.core.callback

import android.net.Uri
import android.os.Bundle
import com.gallery.core.GalleryBundle
import com.gallery.core.ResultType
import com.gallery.scan.SCAN_ALL
import com.gallery.scan.ScanEntity

/**
 *
 */
interface IGallery {

    companion object {

        /**
         * 相机占位的[ScanEntity.parent]
         */
        internal const val CAMERA_PARENT_ID = -11111L

        /**
         * 当前扫描时的parentId
         * 用于横竖屏切换时更新数据
         */
        internal const val GALLERY_START_PARENT_ID = "galleryStartParentId"

        /**
         * 当前拍照之后的[Uri]
         * 有一种情况为横竖屏切换,横屏点进去,然后竖屏拍照返回之后,切换之后会重走生命周期,
         * 所以这里需要保存下当时拍照时的[Uri]
         */
        internal const val GALLERY_START_IMAGE_URL = "galleryStartImageUri"

        /**
         * 横竖屏切换时保存选中的数据
         * [ArrayList<ScanEntity>] Bundle key
         * [Bundle.putParcelableArrayList]
         */
        internal const val GALLERY_START_SELECT = "galleryStartSelectEntities"

        /**
         * [GalleryBundle] Bundle key
         * [Bundle.putParcelable]
         */
        const val GALLERY_START_CONFIG = "galleryStartConfig"

        /**
         * 启动携带的其他数据
         * default [Bundle.EMPTY]
         */
        const val GALLERY_START_BUNDLE = "galleryStartBundle"
    }

    /**
     * 当前扫描的数据
     * 数据已经过滤了[CAMERA_PARENT_ID]
     */
    val currentEntities: ArrayList<ScanEntity>

    /**
     * 当前选中的数据,单选无效
     */
    val selectEntities: ArrayList<ScanEntity>

    /**
     * 当前选中的数据是否为空
     * true 空
     */
    val selectEmpty: Boolean

    /**
     * 当前选中的数据个数
     */
    val selectCount: Int

    /**
     * 当前扫描的数据个数
     */
    val itemCount: Int

    /**
     * 打开相机
     */
    fun startCamera()

    /**
     * 打开裁剪
     */
    fun openCrop(uri: Uri)

    /**
     * 刷新图库
     * [ResultType.CROP]
     * [ResultType.CAMERA]
     */
    fun scanFile(type: ResultType, path: String)

    /**
     * 扫描设备
     * [result] 是否是拍照之后的扫描
     */
    fun onScanGallery(parent: Long = SCAN_ALL, result: Boolean = false)

    /**
     * 刷新预览之后的数据
     * [Bundle.getParcelableArrayList] 选中的数据
     * [Bundle.getBoolean] 是否刷新数据(合并选中的数据)
     */
    fun onUpdatePrevResult(bundle: Bundle)

    /**
     * 用于刷新单个Item
     */
    fun notifyItemChanged(position: Int)

    /**
     * 刷新全部数据
     */
    fun notifyDataSetChanged()

}