package com.gallery.core.delegate

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.kotlin.expand.app.scanFileExpand
import androidx.kotlin.expand.app.squareExpand
import androidx.kotlin.expand.content.drawableExpand
import androidx.kotlin.expand.content.findIdByUriExpand
import androidx.kotlin.expand.content.openVideoExpand
import androidx.kotlin.expand.net.deleteExpand
import androidx.kotlin.expand.net.isFileExistsExpand
import androidx.kotlin.expand.view.hideExpand
import androidx.kotlin.expand.view.showExpand
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.gallery.core.GalleryBundle
import com.gallery.core.ScanArgs
import com.gallery.core.ScanArgs.Companion.putScanArgs
import com.gallery.core.ScanArgs.Companion.scanArgs
import com.gallery.core.callback.IGalleryCallback
import com.gallery.core.callback.IGalleryImageLoader
import com.gallery.core.callback.IGalleryInterceptor
import com.gallery.core.crop.ICrop
import com.gallery.core.expand.*
import com.gallery.core.ui.adapter.GalleryAdapter
import com.gallery.core.ui.widget.SimpleDivider
import com.gallery.scan.ScanImpl
import com.gallery.scan.args.ScanEntityFactory
import com.gallery.scan.extensions.*
import com.gallery.scan.result.Result
import com.gallery.scan.types.ScanType
import com.gallery.scan.types.Sort

/**
 * 图库代理
 */
class ScanDelegate(
        private val fragment: Fragment,
        private val recyclerView: RecyclerView,
        private val emptyView: ImageView,
) : IScanDelegate, GalleryAdapter.OnGalleryItemClickListener {

    private var fileUri: Uri = Uri.EMPTY
    var parentId: Long = ScanType.SCAN_ALL

    private val openCameraLauncher: ActivityResultLauncher<CameraUri> = fragment.requestCameraResultLauncherExpand({ cameraCanceled() }) { cameraSuccess() }
    private val cropLauncher: ActivityResultLauncher<Intent> =
            fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { intent -> galleryICrop.onCropResult(this, galleryBundle, intent) }
    private val cameraPermissionLauncher: ActivityResultLauncher<String> =
            fragment.requestPermissionResultLauncherExpand {
                if (it) {
                    permissionsGranted(PermissionCode.READ)
                } else {
                    permissionsDenied(PermissionCode.READ)
                }
            }

    private val writePermissionLauncher: ActivityResultLauncher<String> =
            fragment.requestPermissionResultLauncherExpand {
                if (it) {
                    permissionsGranted(PermissionCode.WRITE)
                } else {
                    permissionsDenied(PermissionCode.WRITE)
                }
            }

    private val scan: ScanImpl<ScanFileEntity> by lazy {
        ViewModelProvider(fragment,
                ScanViewModelFactory(
                        ownerFragment = fragment,
                        factory = ScanEntityFactory.fileExpand(),
                        args = ScanFileArgs(
                                galleryBundle.scanType.map { it.toString() }.toTypedArray(),
                                galleryBundle.scanSortField,
                                galleryBundle.scanSort
                        )
                ))
                .scanFileImpl()
                .registerLiveData(fragment) { result ->
                    if (result is Result.Multiple) {
                        scanMultipleSuccess(result.multipleValue)
                    } else if (result is Result.Single) {
                        scanSingleSuccess(result.singleValue)
                    }
                }
    }
    private val galleryBundle: GalleryBundle by lazy { fragment.galleryArgs }
    private val galleryImageLoader: IGalleryImageLoader by lazy { fragment.galleryImageLoader }
    private val galleryCallback: IGalleryCallback by lazy { fragment.galleryCallback }
    private val galleryInterceptor: IGalleryInterceptor by lazy { fragment.galleryInterceptor }
    private val galleryICrop: ICrop by lazy { fragment.galleryCrop }
    private val galleryAdapter: GalleryAdapter by lazy { GalleryAdapter(activityNotNull.squareExpand(galleryBundle.spanCount), galleryBundle, galleryCallback, galleryImageLoader, this) }

    override val activity: FragmentActivity?
        get() = fragment.activity
    override val activityNotNull: FragmentActivity
        get() = fragment.requireActivity()
    override val currentEntities: ArrayList<ScanEntity>
        get() = galleryAdapter.currentList.filterTo(ArrayList()) { it.parent != GalleryAdapter.CAMERA }
    override val selectEntities: ArrayList<ScanEntity>
        get() = galleryAdapter.currentSelectList

    override fun onSaveInstanceState(outState: Bundle) {
        ScanArgs.newSaveInstance(parentId, fileUri, selectEntities).putScanArgs(outState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val selectList = savedInstanceState?.scanArgs?.let {
            parentId = it.parentId
            fileUri = it.fileUri
            it.selectList
        }
        galleryCallback.onGalleryCreated()
        fragment.view?.setBackgroundColor(galleryBundle.galleryRootBackground)
        emptyView.setImageDrawable(activityNotNull.drawableExpand(galleryBundle.photoEmptyDrawable))
        emptyView.setOnClickListener { v ->
            if (galleryInterceptor.onEmptyPhotoClick(v)) {
                cameraOpen()
            }
        }
        galleryAdapter.addSelectAll(selectList ?: galleryBundle.selectEntities)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = when (galleryBundle.layoutManager) {
            LayoutManager.GRID -> GridLayoutManager(recyclerView.context, galleryBundle.spanCount, galleryBundle.orientation, false)
            LayoutManager.LINEAR -> LinearLayoutManager(recyclerView.context, galleryBundle.orientation, false)
        }
        recyclerView.addItemDecoration(SimpleDivider(galleryBundle.dividerWidth))
        if (recyclerView.itemAnimator is SimpleItemAnimator) {
            (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }
        recyclerView.adapter = galleryAdapter
        onScanGallery(parentId)
    }

    override fun onDestroy() {
    }

    fun scanMultipleSuccess(entities: ArrayList<ScanFileEntity>) {
        if (entities.isEmpty() && parentId.isScanAllExpand()) {
            emptyView.showExpand()
            recyclerView.hideExpand()
            galleryCallback.onScanSuccessEmpty(activity)
            return
        }
        emptyView.hideExpand()
        recyclerView.showExpand()
        if (parentId.isScanAllExpand() && !galleryBundle.hideCamera) {
            entities.add(0, ScanFileEntity(parent = GalleryAdapter.CAMERA))
        }
        val toScanEntity = entities.toScanEntity()
        galleryAdapter.addAll(toScanEntity)
        galleryAdapter.updateEntity()
        galleryCallback.onScanSuccess(currentEntities)
        scrollToPosition(0)
    }

    fun scanSingleSuccess(entity: ScanFileEntity?) {
        entity ?: return galleryCallback.onResultError(activity, galleryBundle)
        val toScanEntity = entity.toScanEntity()
        //拍照或裁剪成功扫描到数据之后根据扫描方式更新数据
        if (parentId.isScanAllExpand() || parentId == entity.parent) {
            if (galleryBundle.scanSort == Sort.DESC) {
                galleryAdapter.addEntity(if (galleryBundle.hideCamera) 0 else 1, toScanEntity)
            } else {
                galleryAdapter.addEntity(toScanEntity)
                scrollToPosition(galleryAdapter.currentList.size - 1)
            }
        }
        notifyDataSetChanged()
        galleryCallback.onResultSuccess(activity, toScanEntity)
    }

    override fun onCameraItemClick(view: View, position: Int, scanEntity: ScanEntity) {
        cameraOpen()
    }

    override fun onPhotoItemClick(view: View, position: Int, scanEntity: ScanEntity) {
        if (!scanEntity.uri.isFileExistsExpand(activityNotNull)) {
            galleryCallback.onClickItemFileNotExist(activityNotNull, scanEntity)
            return
        }
        if (galleryBundle.isVideoScanExpand) {
            activityNotNull.openVideoExpand(scanEntity.uri) {
                galleryCallback.onOpenVideoPlayError(activityNotNull, scanEntity)
            }
            return
        }
        if (galleryBundle.radio) {
            if (galleryBundle.crop) {
                cropLauncher.launch(galleryICrop.openCrop(this, galleryBundle, scanEntity.uri))
            } else {
                galleryCallback.onGalleryResource(activityNotNull, scanEntity)
            }
            return
        }
        galleryCallback.onPhotoItemClick(activityNotNull, galleryBundle, scanEntity, position, parentId)
    }

    override fun cameraOpen() {
        if (!fragment.checkPermissionAndRequestCameraExpand(cameraPermissionLauncher)) {
            galleryCallback.onCameraOpenStatus(activity, CameraStatus.PERMISSION)
            return
        }
        val cameraUriExpand: Uri? = activityNotNull.cameraUriExpand(galleryBundle)
        cameraUriExpand?.let {
            fileUri = it
        } ?: galleryCallback.onCameraOpenStatus(activity, CameraStatus.ERROR)
        val onCustomCamera: Boolean = galleryInterceptor.onCustomCamera(fileUri)
        if (onCustomCamera || cameraUriExpand == null) {
            return
        }
        galleryCallback.onCameraOpenStatus(activity, fragment.checkCameraStatusExpand(CameraUri(galleryBundle.scanType, fileUri)) { openCameraLauncher.launch(it) })
    }

    override fun cameraSuccess() {
        activityNotNull.scanFileExpand(fileUri) { onScanGallery(parentId, true) }
        if (galleryBundle.cameraCrop) {
            cropLauncher.launch(galleryICrop.openCrop(this, galleryBundle, fileUri))
        }
    }

    override fun cameraCanceled() {
        fileUri.deleteExpand(activityNotNull)
        fileUri = Uri.EMPTY
        galleryCallback.onCameraCanceled(activity, galleryBundle)
    }

    override fun permissionsGranted(type: PermissionCode) {
        when (type) {
            PermissionCode.WRITE -> onScanGallery(parentId)
            PermissionCode.READ -> cameraOpen()
        }
    }

    override fun permissionsDenied(type: PermissionCode) {
        galleryCallback.onPermissionsDenied(activity, type)
    }

    override fun onScanGallery(parent: Long, isCamera: Boolean) {
        if (!fragment.checkPermissionAndRequestWriteExpand(writePermissionLauncher)) {
            return
        }
        this.parentId = parent
        // 如果本机没有图片,进来拍照直接走扫描全部的方法可以兼容到hideCamera
        // resultSuccess只有在拍照成功并且之前数据不为空or裁剪成功才会回调
        // 拍照成功之后不需要特殊处理,因为肯定是SCAN_ALL的情况下,直接更新数据即可
        // 裁剪成功分为两种情况
        // 第一种:SCAN_ALL情况下直接更新数据
        // 第二种:parentId为文件夹ID的时候处理数据,如果 parentId == scan.parent
        // 可以直接插入到当前数据,如果不等于,不能插入,因为裁剪之后的图片属于另一个文件夹的数据
        // 文件夹数据更新的时候也需要处理这种情况
        if (isCamera && galleryAdapter.isNotEmpty) {
            scan.scanSingle(activityNotNull.findIdByUriExpand(fileUri).singleScanExpand())
        } else {
            scan.scanMultiple(parent.multipleScanExpand())
        }
    }

    override fun onScanResult(uri: Uri) {
        when (uri.scheme) {
            ContentResolver.SCHEME_CONTENT -> activityNotNull.scanFileExpand(uri) { scan.scanSingle(activityNotNull.findIdByUriExpand(it).singleScanExpand()) }
            ContentResolver.SCHEME_FILE -> activityNotNull.scanFileExpand(uri.path.orEmpty()) { scan.scanSingle(activityNotNull.findIdByUriExpand(it).singleScanExpand()) }
            else -> Log.e("gallery", "unsupported uri")
        }
    }

    override fun onUpdateResult(scanArgs: ScanArgs?) {
        scanArgs ?: return
        if (!scanArgs.isRefresh || selectEntities == scanArgs.selectList) {
            return
        }
        galleryAdapter.addSelectAll(scanArgs.selectList)
        galleryAdapter.updateEntity()
        galleryCallback.onChangedResultCount(selectCount)
    }

    override fun notifyItemChanged(position: Int) {
        galleryAdapter.notifyItemChanged(position)
    }

    override fun notifyDataSetChanged() {
        galleryAdapter.notifyDataSetChanged()
    }

    override fun addOnScrollListener(onScrollListener: RecyclerView.OnScrollListener) {
        recyclerView.addOnScrollListener(onScrollListener)
    }

    override fun scrollToPosition(position: Int) {
        recyclerView.scrollToPosition(position)
    }

}