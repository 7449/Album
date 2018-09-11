package com.album.ui.view

import android.app.Activity
import com.album.entity.AlbumEntity
import java.util.*

/**
 * by y on 17/08/2017.
 */

interface PrevView {
    fun getPreViewActivity(): Activity

    fun scanSuccess(albumEntityList: ArrayList<AlbumEntity>)

    fun hideProgress()

    fun showProgress()
}
