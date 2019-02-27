package com.album.core

import java.io.File

/**
 * @author y
 * @create 2019/2/27
 */
object AlbumFile {

    /**
     * 文件是否存在
     */
    fun String.fileExists(): Boolean = parentFile() != null

    /**
     * 路径转[File]
     */
    fun String.pathToFile(): File = File(this)

    /**
     * 获取父级路径
     */
    fun String.parentFile(): File? {
        if (this == "") {
            return null
        }
        val file = File(this)
        return if (!file.exists()) {
            null
        } else file.parentFile ?: return null
    }

}