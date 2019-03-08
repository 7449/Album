package com.album.core.scan

import android.provider.MediaStore

/**
 * @author y
 * @create 2019/3/5
 */
object AlbumColumns {

    const val IMAGE = MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString()

    const val VIDEO = MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()

    /**
     * 页数
     */
    const val PAGE = "page"

    /**
     * 扫描type
     */
    const val SCAN_TYPE = "scan_type"

    /**
     * 个数
     */
    const val COUNT = "_count"
    /**
     * id
     */
    const val ID = "_id"
    /**
     * 路径
     */
    const val DATA = "_data"
    /**
     * 大小
     */
    const val SIZE = "_size"
    /**
     * 视频时长
     */
    const val DURATION = "duration"
    /**
     * 父级文件夹 parent
     */
    const val PARENT = "parent"
    /**
     * 文件类型
     */
    const val MIME_TYPE = "mime_type"
    /**
     * 文件名称
     */
    const val DISPLAY_NAME = "_display_name"
    /**
     * 方向
     */
    const val ORIENTATION = "orientation"
    /**
     * bucket_id 每个文件夹下的 bucket_id 都是相同的
     */
    const val BUCKET_ID = "bucket_id"
    /**
     * 文件夹名称
     */
    const val BUCKET_DISPLAY_NAME = "bucket_display_name"
    /**
     * 文件类型Type
     */
    const val MEDIA_TYPE = "media_type"
    /**
     * 宽度
     */
    const val WIDTH = "width"
    /**
     * 高度
     */
    const val HEIGHT = "height"
    /**
     * 时间
     */
    const val DATA_MODIFIED = "date_modified"
}