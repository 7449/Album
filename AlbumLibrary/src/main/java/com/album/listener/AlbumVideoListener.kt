package com.album.listener

import com.album.ui.fragment.AlbumBaseFragment

/**
 * by y on 28/08/2017.
 *
 *
 * customize video ui
 */

interface AlbumVideoListener {
    /**
     * 自定义相机
     *
     * @param fragment [com.album.ui.fragment.AlbumFragment]
     */
    fun startVideo(fragment: AlbumBaseFragment)
}
