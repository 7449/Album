package com.album.ui.view

/**
 * @author y
 */
interface PrevFragmentToAtyListener {
    fun onChangedCount(currentPos: Int)
    fun onChangedToolbarCount(currentPos: Int, maxPos: Int)
}
