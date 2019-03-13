package com.album

/**
 * @author y
 * @create 2019/3/13
 */

fun Album.destroy() = Album.instance.apply {
    options = null
    albumListener = null
    customCameraListener = null
    albumEmptyClickListener = null
    albumImageLoader = null
    initList = null
}

fun Album.listenerDestroy() = Album.instance.apply {
    albumListener = null
    customCameraListener = null
    albumEmptyClickListener = null
}

fun Album.removeAllInitList() = Album.instance.initList?.clear()

fun Album.imageLoaderDestroy() = Album.instance.apply {
    albumImageLoader = null
}