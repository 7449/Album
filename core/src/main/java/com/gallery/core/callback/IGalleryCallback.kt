package com.gallery.core.callback

import com.gallery.core.CameraStatus
import com.gallery.core.GalleryBundle
import com.gallery.core.PermissionCode
import com.gallery.core.ui.adapter.vh.PhotoViewHolder
import com.gallery.core.ui.fragment.ScanFragment
import com.gallery.scan.ScanEntity

/**
 *
 */
interface IGalleryCallback {

    /**
     * 单选状态下,点击Item返回的那条数据
     * [ScanFragment.onPhotoItemClick]
     */
    fun onGalleryResource(scanEntities: ArrayList<ScanEntity>)

    /**
     * 点击CheckBox时该文件已经被删除
     * 适用场景:在图片选择页面返回桌面打开相册删除某张图片
     * [PhotoViewHolder.photo]
     */
    fun onClickCheckBoxFileNotExist()

    /**
     * 已达到选择最大数
     * [GalleryBundle.multipleMaxCount]
     */
    fun onClickCheckBoxMaxCount()

    /**
     * 点击图片时该文件已被删除
     * 适用场景:在图片选择页面返回桌面打开相册删除某张图片
     * 这个方法优先级高于单选和视频播放，裁剪等功能
     * [ScanFragment.onPhotoItemClick]
     */
    fun onClickItemFileNotExist()

    /**
     * 点击CheckBox时会触发
     * [PhotoViewHolder.photo]
     */
    fun onChangedCheckBox(isSelect: Boolean, scanEntity: ScanEntity)

    /**
     * 横竖屏切换时触发
     * [ScanFragment.onActivityCreated]
     */
    fun onChangedScreen(selectCount: Int)

    /**
     * 刷新预览页数据之后触发
     * [ScanFragment.onResultPreview]
     */
    fun onChangedPrevCount(selectCount: Int)

    /**
     * 如果图片存在,并且不是视频模式,不是单选的情况下触发这个方法
     * [ScanFragment.onPhotoItemClick]
     */
    fun onPhotoItemClick(selectEntities: ArrayList<ScanEntity>, position: Int, parentId: Long)

    /**
     * 拍照返回
     */
    fun onCameraResult(scanEntity: ScanEntity?)

    /**
     * 在[IGalleryInterceptor.onGalleryFragmentResult]为false的情况下会触发
     * 取消拍照
     */
    fun onCameraCanceled()

    /**
     * 拍照之后获取数据失败
     */
    fun onCameraResultError()

    /**
     * 打开相机返回的状态
     * [CameraStatus.SUCCESS] 成功
     * [CameraStatus.ERROR] 失败
     * [CameraStatus.PERMISSION] 权限被拒
     */
    fun onCameraOpenStatus(status: CameraStatus)

    /**
     * 没有扫描到任何数据
     */
    fun onScanSuccessEmpty()

    /**
     * 视频播放异常
     */
    fun onOpenVideoPlayError()

    /**
     * 权限被拒
     * [PermissionCode.READ]
     * [PermissionCode.WRITE]
     */
    fun onPermissionsDenied(type: PermissionCode)
}