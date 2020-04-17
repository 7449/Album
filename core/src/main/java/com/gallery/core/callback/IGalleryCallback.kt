package com.gallery.core.callback

import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.FrameLayout
import com.gallery.core.CameraStatus
import com.gallery.core.GalleryBundle
import com.gallery.core.PermissionCode
import com.gallery.core.ui.adapter.vh.PhotoViewHolder
import com.gallery.core.ui.fragment.ScanFragment
import com.gallery.scan.ScanEntity
import com.yalantis.ucrop.UCrop

/**
 *
 */
interface IGalleryCallback {

    /**
     * 是否拦截[ScanFragment.onActivityResult]
     * true 拦截
     * 默认不拦截
     */
    fun onGalleryFragmentResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean = false

    /**
     * 拍照返回
     */
    fun onPhotoResult(scanEntity: ScanEntity?)

    /**
     * 单选状态下,点击Item返回的那条数据
     * [ScanFragment.onPhotoItemClick]
     */
    fun onGalleryResource(scanEntities: ArrayList<ScanEntity>)

    /**
     * 加载图片
     * [IGalleryImageLoader.onDisplayGallery]
     */
    fun onDisplayImageView(width: Int, height: Int, galleryEntity: ScanEntity, container: FrameLayout)

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
    fun onScreenChanged(selectCount: Int)

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
     * 自定义图片裁剪
     *
     * true 自定义
     */
    fun onCustomPhotoCrop(uri: Uri): Boolean = false

    /**
     * 无图片或视频时触发,true会自动打开相机
     */
    fun onEmptyPhotoClick(view: View): Boolean = true

    /**
     * 在[onGalleryFragmentResult]为false的情况下会触发
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
    fun onOpenCameraStatus(status: CameraStatus)

    /**
     * 在[onCustomPhotoCrop]为false的情况下会触发
     * [UCrop]的配置
     */
    fun onUCropOptions() = UCrop.Options()

    /**
     * 在[onGalleryFragmentResult]和[onCustomPhotoCrop]为false的情况下会触发
     * 取消裁剪
     */
    fun onUCropCanceled()

    /**
     * 在[onGalleryFragmentResult]和[onCustomPhotoCrop]为false的情况下会触发
     * 裁剪异常
     */
    fun onUCropError(throwable: Throwable?)

    /**
     * 在[onGalleryFragmentResult]和[onCustomPhotoCrop]为false的情况下会触发
     * 裁剪成功
     */
    fun onUCropResources(uri: Uri)

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