package com.album

import android.view.View
import android.widget.FrameLayout
import com.album.ui.fragment.AlbumBaseFragment
import com.album.ui.widget.AlbumImageView
import com.album.ui.widget.TouchImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import org.jetbrains.annotations.NotNull
import java.io.File

interface AlbumParentListener {
    /**
     * 点击进入预览页,依赖的DialogFragment或者Activity继承即可
     */
    fun onAlbumItemClick(multiplePreviewList: ArrayList<AlbumEntity>, position: Int, bucketId: String)
}

interface AlbumPreviewParentListener {
    /**
     * 点击checkbox时调用
     */
    fun onChangedCount(currentCount: Int)

    /**
     * 滑动时调用，可改变toolbar数据
     */
    fun onChangedToolbarCount(currentPos: Int, maxPos: Int)
}

interface AlbumImageLoader {
    /**
     * 首页图片加载
     */
    fun displayAlbum(width: Int, height: Int, albumEntity: AlbumEntity, container: FrameLayout): View

    /**
     * 目录图片加载
     */
    fun displayAlbumThumbnails(finderEntity: FinderEntity, container: FrameLayout): View

    /**
     * 预览页图片加载
     */
    fun displayPreview(albumEntity: AlbumEntity, container: FrameLayout): View
}

interface AlbumCustomCameraListener {
    /**
     * 自定义相机
     *
     * @param fragment [com.album.ui.fragment.AlbumFragment.openCamera]
     */
    fun startCamera(fragment: AlbumBaseFragment)
}

interface OnEmptyClickListener {
    /**
     * true 打开相机，false自定义点击事件
     */
    fun click(@NotNull view: View): Boolean
}

interface AlbumListener {
    /**
     * activity被销毁
     */
    fun onAlbumActivityFinish()

    /**
     * back返回
     */
    fun onAlbumActivityBackPressed()

    /**
     * activity 文件目录为空
     */
    fun onAlbumFinderEmpty()

    /**
     * 预览页点击确定但是没有选中图片
     */
    fun onAlbumPreviewSelectEmpty()

    /**
     * 选择图片
     */
    fun onAlbumResources(list: List<AlbumEntity>)

    /////////////////////////////////////////////////////

    /**
     * 获取预览但是未选择图片
     */
    fun onAlbumPreviewEmpty()

    /**
     * 权限被拒
     */
    fun onAlbumPermissionsDenied(type: Int)

    /**
     * 预览滑动图片已经被删
     */
    fun onAlbumPreviewFileNotExist()

    /**
     * 点击选择但是未选择图片
     */
    fun onAlbumSelectEmpty()

    /**
     * 图片已经被删
     */
    fun onAlbumFileNotExist()

    /**
     * 多选图片已经被删
     */
    fun onAlbumCheckFileNotExist()

    /**
     * 取消裁剪
     */
    fun onAlbumCropCanceled()

    /**
     * 取消拍照
     */
    fun onAlbumCameraCanceled()

    /**
     * 裁剪错误
     */
    fun onAlbumUCropError(data: Throwable?)

    /**
     * 裁剪成功
     */
    fun onAlbumUCropResources(scannerFile: File)

    /**
     * 已达多选最大数
     */
    fun onAlbumMaxCount()

    /**
     * 打开相机错误
     */
    fun onAlbumOpenCameraError()

    /**
     * 没有图片
     */
    fun onAlbumEmpty()

    /**
     * 没有更多图片
     */
    fun onAlbumNoMore()

    /**
     * 拍照返回错误
     */
    fun onAlbumResultCameraError()

    /**
     * 视频播放错误
     */
    fun onVideoPlayError()

    /**
     * 每次checkbox
     */
    fun onCheckBoxAlbum(count: Int, maxCount: Int)
}


class SimpleAlbumImageLoader : AlbumImageLoader {

    private val requestOptions: RequestOptions = RequestOptions().placeholder(R.drawable.ic_album_default_loading).error(R.drawable.ic_album_default_loading).centerCrop()

    override fun displayAlbum(width: Int, height: Int, albumEntity: AlbumEntity, container: FrameLayout): View {
        val albumImageView = AlbumImageView(container.context)
        Glide.with(container.context).load(albumEntity.path).apply(requestOptions.override(width, height)).into(albumImageView)
        return albumImageView
    }

    override fun displayAlbumThumbnails(finderEntity: FinderEntity, container: FrameLayout): View {
        val albumImageView = AlbumImageView(container.context)
        Glide.with(container.context).load(finderEntity.thumbnailsPath).apply(requestOptions).into(albumImageView)
        return albumImageView
    }

    override fun displayPreview(albumEntity: AlbumEntity, container: FrameLayout): View {
        val albumImageView = TouchImageView(container.context)
        Glide.with(container.context).load(albumEntity.path).apply(requestOptions).into(albumImageView)
        return albumImageView
    }
}

open class SimpleAlbumListener : AlbumListener {
    override fun onAlbumActivityFinish() {}
    override fun onAlbumResultCameraError() {}
    override fun onAlbumPermissionsDenied(type: Int) {}
    override fun onAlbumPreviewFileNotExist() {}
    override fun onAlbumFinderEmpty() {}
    override fun onAlbumPreviewEmpty() {}
    override fun onAlbumSelectEmpty() {}
    override fun onAlbumFileNotExist() {}
    override fun onAlbumPreviewSelectEmpty() {}
    override fun onAlbumCheckFileNotExist() {}
    override fun onAlbumCropCanceled() {}
    override fun onAlbumCameraCanceled() {}
    override fun onAlbumUCropError(data: Throwable?) {}
    override fun onAlbumResources(list: List<AlbumEntity>) {}
    override fun onAlbumUCropResources(scannerFile: File) {}
    override fun onAlbumMaxCount() {}
    override fun onAlbumActivityBackPressed() {}
    override fun onAlbumOpenCameraError() {}
    override fun onAlbumEmpty() {}
    override fun onAlbumNoMore() {}
    override fun onVideoPlayError() {}
    override fun onCheckBoxAlbum(count: Int, maxCount: Int) {}
}
