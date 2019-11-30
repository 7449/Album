package com.gallery.scan.args

import android.annotation.SuppressLint
import android.provider.MediaStore

/**
 * @author y
 * @create 2019/3/5
 */
@SuppressLint("InlinedApi")
object Columns {

    const val IMAGE = MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString()

    const val VIDEO = MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()

    /**
     * 扫描type
     */
    const val SCAN_TYPE = "scan_type"

    /**
     * id
     */
    const val ID = MediaStore.Files.FileColumns._ID
    /**
     * 大小
     */
    const val SIZE = MediaStore.Files.FileColumns.SIZE
    /**
     * 父级文件夹 parent
     */
    const val PARENT = MediaStore.Files.FileColumns.PARENT
    /**
     * 文件类型
     */
    const val MIME_TYPE = MediaStore.Files.FileColumns.MIME_TYPE
    /**
     * 文件名称
     */
    const val DISPLAY_NAME = MediaStore.Files.FileColumns.DISPLAY_NAME
    /**
     * 文件类型Type
     */
    const val MEDIA_TYPE = MediaStore.Files.FileColumns.MEDIA_TYPE
    /**
     * 宽度
     */
    const val WIDTH = MediaStore.Files.FileColumns.WIDTH
    /**
     * 高度
     */
    const val HEIGHT = MediaStore.Files.FileColumns.HEIGHT
    /**
     * 时间
     */
    const val DATE_MODIFIED = MediaStore.Files.FileColumns.DATE_MODIFIED
    /**
     * 视频时长
     */
    const val DURATION = MediaStore.Files.FileColumns.DURATION
    /**
     * 方向
     */
    const val ORIENTATION = MediaStore.Files.FileColumns.ORIENTATION
    /**
     * bucket_id
     */
    const val BUCKET_ID = MediaStore.Files.FileColumns.BUCKET_ID
    /**
     * 文件夹名称
     */
    const val BUCKET_DISPLAY_NAME = MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME
}