package com.gallery.core.delegate.args

import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import com.gallery.core.GalleryConfigs
import com.gallery.core.delegate.IPrevDelegate
import com.gallery.core.entity.ScanEntity
import com.gallery.core.extensions.parcelable
import com.gallery.scan.Types
import kotlinx.parcelize.Parcelize

@Parcelize
data class PrevArgs(
    /**
     * 当前文件的parentId,用于扫描数据库获取对应的数据
     * 如果是预览进入的话可传[Types.Id.NONE]用作区分，这样会跳过扫描数据库，直接调用[IPrevDelegate.updateEntity]
     * 这个时候展示的数据就是[selectList]
     */
    val parentId: Long,
    /**
     * 当前选中的数据,如果是预览进入则认为所有数据==[selectList]
     */
    val selectList: ArrayList<ScanEntity>,
    /**
     * [GalleryConfigs] 参数
     */
    val config: GalleryConfigs?,
    /**
     * position 需要跳转的位置
     */
    val position: Int,
    /**
     * [scanSingleType] 是否单独扫描某些类型的数据
     * 如果[parentId] == [Types.Id.NONE] 则认为点击的是预览而不是item,则未选中数据和选中数据应该一致
     * 所以调用该属性的前提是[parentId] != [Types.Id.NONE]
     * 判断[scanSingleType] == [MediaStore.Files.FileColumns.MEDIA_TYPE_NONE]，
     * 如果不是，则使用[scanSingleType],如果是，则扫描[GalleryConfigs.type]类型的数据
     * 如果使用自定义 scanType,则[parentId]传[Types.Id.ALL]比较合适
     * TIP:好像是为了扫描全部视频加的参数,具体可见 weChat Library
     */
    val scanSingleType: Int,
) : Parcelable {
    companion object {

        private const val Key = "prevArgs"

        /*** 预览横竖屏切换保存数据* 只需要当前位置和当前选中的数据*/
        fun onSaveInstanceState(position: Int, selectList: ArrayList<ScanEntity>): PrevArgs {
            return PrevArgs(
                Types.Id.ALL,
                selectList,
                null,
                position,
                MediaStore.Files.FileColumns.MEDIA_TYPE_NONE
            )
        }

        fun PrevArgs.toBundle(bundle: Bundle): Bundle {
            bundle.putParcelable(Key, this)
            return bundle
        }

        val Bundle.prevArgs
            get() = parcelable<PrevArgs>(Key)

        val Bundle.prevArgsOrDefault
            get() = prevArgs ?: PrevArgs(
                Types.Id.ALL,
                arrayListOf(),
                GalleryConfigs(),
                0,
                MediaStore.Files.FileColumns.MEDIA_TYPE_NONE
            )

        val PrevArgs.configOrDefault
            get() = config ?: GalleryConfigs()

    }
}