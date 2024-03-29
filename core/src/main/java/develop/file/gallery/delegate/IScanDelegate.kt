package develop.file.gallery.delegate

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import develop.file.gallery.args.PermissionType
import develop.file.gallery.delegate.args.ScanArgs
import develop.file.gallery.entity.ScanEntity
import develop.file.media.Types
import develop.file.media.impl.file.FileMediaEntity

interface IScanDelegate {

    /*** 根View*/
    val rootView: View

    /*** 当前Activity*/
    val activity: FragmentActivity?

    /*** 当前Activity*/
    val requireActivity: FragmentActivity

    /*** 当前扫描的数据* 已经过滤了CAMERA*/
    val allItem: ArrayList<ScanEntity>

    /*** 当前选中的数据*/
    val selectItem: ArrayList<ScanEntity>

    /*** 当前选中的数据是否为空* true 空*/
    val isSelectEmpty: Boolean
        get() = selectItem.isEmpty()

    /*** 当前选中的数据个数*/
    val selectCount: Int
        get() = selectItem.size

    /*** 当前扫描的数据个数* 已经过滤了CAMERA*/
    val itemCount: Int
        get() = allItem.size

    /*** 当前扫描Id*/
    val currentParentId: Long

    /*** 更新当前扫描Id*/
    fun updateParentId(parentId: Long)

    /*** 横竖屏切换*/
    fun onSaveInstanceState(outState: Bundle)

    /*** 初始化*/
    fun onCreate(savedInstanceState: Bundle?)

    /*** 销毁*/
    fun onDestroy()

    /*** 扫描图库*/
    fun onScanGallery(parent: Long = Types.Id.ALL, isCamera: Boolean = false)

    /*** 扫描单个数据*/
    fun onScanResult(uri: Uri)

    /*** 扫描集合成功*/
    fun onScanMultipleSuccess(scanEntities: ArrayList<FileMediaEntity>)

    /*** 扫描单个文件成功*/
    fun onScanSingleSuccess(scanEntity: FileMediaEntity?)

    /*** 刷新预览之后的数据* [ScanArgs.selectList] 选中的数据* [ScanArgs.isRefresh] 是否刷新数据(合并选中的数据)*/
    fun onUpdateResult(args: ScanArgs)

    /*** 刷新单个Item*/
    fun notifyItemChanged(position: Int)

    /*** 刷新全部数据*/
    fun notifyDataSetChanged()

    /*** 监听滑动*/
    fun addOnScrollListener(listener: OnScrollListener)

    /*** 去掉监听滑动*/
    fun removeOnScrollListener(listener: OnScrollListener)

    /*** 滑动到某个位置*/
    fun scrollToPosition(position: Int)

    /*** 打开相机*/
    fun openSystemCamera()

    /*** 拍照成功*/
    fun takePictureSuccess()

    /*** 取消相机*/
    fun takePictureCanceled()

    /*** 允许权限*/
    fun permissionsGranted(type: PermissionType)

    /*** 权限被拒*/
    fun permissionsDenied(type: PermissionType)

}