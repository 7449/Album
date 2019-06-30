package com.album.core.view

import com.album.core.scan.AlbumEntity

/**
 * @author y
 * @create 2019/2/27
 */
interface AlbumPreView : AlbumBaseView {
    /**
     * 如果不为null,则为点击预览进入多选查看模式,不扫描数据库,直接返回[getSelectEntity]合并之后的数据
     */
    fun getAllEntity(): ArrayList<AlbumEntity>? = null

    /**
     * parent Id
     */
    fun getParentId(): Long
}
