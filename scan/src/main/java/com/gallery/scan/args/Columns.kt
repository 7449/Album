package com.gallery.scan.args

import android.provider.MediaStore

/**
 * @author y
 * @create 2019/3/5
 */
object Columns {

    /** 扫描type */
    const val SCAN_TYPE = "scan_type"
    const val SCAN_RESULT = "scan_result"
    const val SORT = "sort"
    const val SORT_FIELD = "sort_field"

    /** id */
    const val ID: String = MediaStore.Files.FileColumns._ID

    /** 大小 */
    const val SIZE: String = MediaStore.Files.FileColumns.SIZE

    /** 父级文件夹 parent */
    const val PARENT: String = MediaStore.Files.FileColumns.PARENT

    /** 文件类型 */
    const val MIME_TYPE: String = MediaStore.Files.FileColumns.MIME_TYPE

    /** 文件名称 */
    const val DISPLAY_NAME: String = MediaStore.Files.FileColumns.DISPLAY_NAME

    /** 文件类型Type */
    const val MEDIA_TYPE: String = MediaStore.Files.FileColumns.MEDIA_TYPE

    /** 宽度 */
    const val WIDTH: String = MediaStore.Files.FileColumns.WIDTH

    /** 高度 */
    const val HEIGHT: String = MediaStore.Files.FileColumns.HEIGHT

    /** 添加文件的时间 */
    const val DATE_ADDED: String = MediaStore.Files.FileColumns.DATE_ADDED

    /** 文件上次修改时间 */
    const val DATE_MODIFIED: String = MediaStore.Files.FileColumns.DATE_MODIFIED

    /** 视频时长 */
    const val DURATION: String = MediaStore.Video.VideoColumns.DURATION

    /** 方向 */
    const val ORIENTATION: String = MediaStore.Images.ImageColumns.ORIENTATION

    /** bucket_id */
    const val BUCKET_ID: String = MediaStore.Images.ImageColumns.BUCKET_ID

    /** 文件夹名称 */
    const val BUCKET_DISPLAY_NAME: String = MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME
}