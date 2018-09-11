package com.album.presenter


import com.album.entity.AlbumEntity

import java.util.ArrayList

/**
 * by y on 17/08/2017.
 */

interface PreviewPresenter {
    fun scan(bucketId: String, page: Int, count: Int)

    fun mergeSelectEntity(albumEntityList: List<AlbumEntity>, selectAlbumEntityList: ArrayList<AlbumEntity>)
}
