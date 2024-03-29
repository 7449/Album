package develop.file.gallery.delegate.args

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import develop.file.gallery.delegate.IPrevDelegate
import develop.file.gallery.delegate.IScanDelegate
import develop.file.gallery.entity.ScanEntity
import develop.file.gallery.extensions.ResultCompat.parcelableVersion
import develop.file.media.Types
import kotlinx.parcelize.Parcelize

/**
 *
 * 使用场景：
 *
 *  [IScanDelegate.onSaveInstanceState] //首页横竖屏
 *  或者
 *  [IPrevDelegate.resultBundle] //合并预览和首页的数据
 *
 * 1.
 *  调用[IPrevDelegate.resultBundle]获取需要的参数
 *  其中
 *  [parentId]默认参数 [Types.Id.ALL]
 *  [fileUri]默认参数 [Uri.EMPTY]
 *  [isRefresh]是否需要合并数据并刷新
 *  [selectList]选中的数据
 *  如果需要获取数据可通过[ScanArgs.scanArgs]获取里面的[selectList]
 *
 * 2.
 *  在横竖屏切换时[IScanDelegate.onSaveInstanceState]获取需要的参数
 *  其中
 *  [parentId]当前的parentId
 *  [fileUri]拍照的Uri
 *  [isRefresh]默认参数false
 *  [selectList]当前选中的数据
 */
@Parcelize
data class ScanArgs(
    val parentId: Long,
    val fileUri: Uri,
    val isRefresh: Boolean,
    val selectList: ArrayList<ScanEntity>,
) : Parcelable {
    companion object {
        private const val Key = "scanArgs"

        fun onSaveInstanceState(
            parentId: Long,
            fileUri: Uri,
            selectList: ArrayList<ScanEntity>
        ): ScanArgs {
            return ScanArgs(parentId, fileUri, false, selectList)
        }

        fun newResultInstance(selectList: ArrayList<ScanEntity>, isRefresh: Boolean): ScanArgs {
            return ScanArgs(Types.Id.ALL, Uri.EMPTY, isRefresh, selectList)
        }

        fun ScanArgs.toBundle(bundle: Bundle): Bundle {
            bundle.putParcelable(Key, this)
            return bundle
        }

        val Bundle.scanArgs
            get() = parcelableVersion<ScanArgs>(Key)
    }
}