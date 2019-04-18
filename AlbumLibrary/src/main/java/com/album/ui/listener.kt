package com.album.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.album.core.scan.AlbumEntity

/**
 * @author y
 * @create 2019-04-18
 */
interface OnAlbumPrevItemClickListener {

    fun onItemCheckBoxClick(view: View, currentMaxCount: Int, albumEntity: AlbumEntity)

    fun onItemClick(view: View, position: Int, albumEntity: AlbumEntity)

}

interface OnAlbumItemClickListener {

    fun onCameraItemClick(view: View, position: Int, albumEntity: AlbumEntity)

    fun onPhotoItemClick(view: View, position: Int, albumEntity: AlbumEntity)

}


internal interface AlbumMethodFragmentViewListener {
    /**
     * 断掉MediaScanner
     */
    fun disconnectMediaScanner()

    /**
     * 扫描设备
     * [parent] 文件夹唯一
     * [isFinder] 是否点击文件夹扫描
     * [result] 是否是拍照之后的扫描
     */
    fun onScanAlbum(parent: Long, isFinder: Boolean, result: Boolean)

    /**
     * 扫描裁剪之后的信息
     */
    fun onScanCropAlbum(path: String)

    /**
     * 打开相机
     */
    fun startCamera()

    /**
     * 裁剪
     */
    fun openUCrop(path: String)

    /**
     * 刷新图库
     */
    fun refreshMedia(type: Int, path: String)

    /**
     * 选择选中的数据
     */
    fun selectPreview(): ArrayList<AlbumEntity>

    /**
     * 确定数据
     */
    fun multipleSelect()

    /**
     * 刷新[FragmentActivity.onActivityResult]数据
     */
    fun onResultPreview(bundle: Bundle)

    /**
     * 刷新数据
     */
    fun onDialogResultPreview(bundle: Bundle)
}