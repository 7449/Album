package com.album.listener

import android.support.v4.app.Fragment

/**
 * by y on 28/08/2017.
 *
 *
 * customize camera ui
 */

interface AlbumCameraListener {

    /**
     * 自定义相机
     *
     * @param fragment [com.album.ui.fragment.AlbumFragment]
     */
    fun startCamera(fragment: Fragment)
}
