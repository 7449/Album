package com.gallery.core.delegate.args

import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import com.gallery.core.GalleryBundle
import com.gallery.core.delegate.IPrevDelegate
import com.gallery.core.entity.ScanEntity
import com.gallery.scan.Types
import kotlinx.parcelize.Parcelize

/**
 * 使用场景
 *
 * 1.提供预览页所需的所有数据
 */
@Parcelize
data class PrevArgs(
    /**
     * 当前文件的parentId,用于扫描数据库获取对应的数
     * 如果是预览进入的话可传[Types.Scan.SCAN_NONE]，
     * 这样会跳过扫描数据库，直接调用[IPrevDelegate.updateEntity]
     * 这个时候展示的数据就是[selectList]
     */
    val parentId: Long,
    /**
     * 当前选中的数据,如果是预览进入则认为所有数据==[selectList]
     */
    val selectList: ArrayList<ScanEntity>,
    /**
     * [GalleryBundle] 参数
     */
    val config: GalleryBundle?,
    /**
     * position 需要跳转的位置
     */
    val position: Int,
    /**
     * [scanAlone] 是否是单独扫描某些类型的数据
     * 如果[parentId] == [Types.Scan.SCAN_NONE] 则认为点击的是预览而不是item,则未选中数据和选中数据应该一致
     * 所以调用该属性的前提是[parentId] != [Types.Scan.SCAN_NONE]
     * 判断[scanAlone] == [MediaStore.Files.FileColumns.MEDIA_TYPE_NONE]] ，
     * 如果不是，则使用 [scanAlone],如果是，则扫描 [GalleryBundle.scanType]类型的数据
     * 如果使用自定义 scanType,则[parentId]传 [Types.Scan.SCAN_ALL] 比较合适
     */
    val scanAlone: Int,
) : Parcelable {
    companion object {

        private const val Key = "prevArgs"

        /**
         * 预览横竖屏切换保存数据
         * 只需要当前位置和当前选中的数据
         */
        fun newSaveInstance(position: Int, selectList: ArrayList<ScanEntity>): PrevArgs {
            return PrevArgs(
                Types.Scan.SCAN_ALL,
                selectList,
                null,
                position,
                MediaStore.Files.FileColumns.MEDIA_TYPE_NONE
            )
        }

        fun PrevArgs.putPrevArgs(bundle: Bundle = Bundle()): Bundle {
            bundle.putParcelable(Key, this)
            return bundle
        }

        val Bundle.prevArgs
            get() = getParcelable<PrevArgs>(Key)

        val Bundle.prevArgsOrDefault
            get() = prevArgs
                ?: PrevArgs(
                    Types.Scan.SCAN_ALL,
                    arrayListOf(),
                    GalleryBundle(),
                    0,
                    MediaStore.Files.FileColumns.MEDIA_TYPE_NONE
                )

        val PrevArgs.configOrDefault
            get() = config ?: GalleryBundle()
    }
}