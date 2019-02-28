package com.album.listener

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.album.core.scan.AlbumEntity
import com.album.core.scan.FinderEntity
import java.io.File

/**
 * @author y
 * @create 2019/2/28
 */
internal interface AlbumMethodFragmentViewListener {
    /**
     * 获取文件夹list
     */
    fun getFinderEntity(): List<FinderEntity>

    /**
     * 断掉MediaScanner
     */
    fun disconnectMediaScanner()

    /**
     * 扫描设备
     * [bucketId] 文件夹唯一
     * [isFinder] 是否点击文件夹扫描
     * [result] 是否是拍照之后的扫描
     */
    fun onScanAlbum(bucketId: String, isFinder: Boolean, result: Boolean)

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
    fun refreshMedia(type: Int, file: File)

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
}