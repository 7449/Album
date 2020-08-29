package com.gallery.core

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import com.gallery.scan.SCAN_ALL
import com.gallery.scan.ScanEntity
import com.gallery.scan.ScanType
import com.gallery.scan.annotation.ScanTypeDef
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ScanArgs(
        val parentId: Long,
        val fileUri: Uri,
        val isRefresh: Boolean,
        val selectList: ArrayList<ScanEntity>
) : Parcelable {
    companion object {
        private const val Key = "scanArgs"

        internal fun newSaveInstance(parentId: Long, fileUri: Uri, selectList: ArrayList<ScanEntity>): ScanArgs {
            return ScanArgs(parentId, fileUri, false, selectList)
        }

        internal fun newResultInstance(selectList: ArrayList<ScanEntity>, isRefresh: Boolean): ScanArgs {
            return ScanArgs(SCAN_ALL, Uri.EMPTY, isRefresh, selectList)
        }

        fun ScanArgs.putArgs(bundle: Bundle = Bundle()): Bundle {
            bundle.putParcelable(Key, this)
            return bundle
        }

        val Bundle.scanArgs
            get() = getParcelable<ScanArgs>(Key)
    }
}

@Parcelize
data class PrevArgs(
        /**
         * 当前文件件的parentId,用于扫描数据库获取对应的数 如果是预览进入的话可传[GalleryConfig.DEFAULT_PARENT_ID]
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
         * aloneScan 是否是单独扫描某些类型的数据 [ScanType.IMAGE] [ScanType.VIDEO] [ScanType.MIX]
         * 如果[parentId] == [GalleryConfig.DEFAULT_PARENT_ID] 则认为点击的是预览而不是item,则未选中数据和选中数据应该一致
         * 如果不是，则判断[aloneScan] == [GalleryConfig.DEFAULT_SCAN_ALONE_TYPE] ，如果不是，则使用 [aloneScan],如果是，则扫描 [GalleryBundle.scanType]类型的数据
         * 如果使用自定义 scanType,则parentId传 SCAN_ALL 比较合适
         */
        @ScanTypeDef
        val aloneScan: Int
) : Parcelable {
    companion object {

        private const val Key = "prevArgs"

        internal fun newSaveInstance(position: Int, selectList: ArrayList<ScanEntity>): PrevArgs {
            return PrevArgs(SCAN_ALL, selectList, null, position, ScanType.IMAGE)
        }

        fun PrevArgs.putArgs(bundle: Bundle = Bundle()): Bundle {
            bundle.putParcelable(Key, this)
            return bundle
        }

        val Bundle.prevArgs
            get() = getParcelable<PrevArgs>(Key)

        val PrevArgs.configOrDefault
            get() = config ?: GalleryBundle()

    }
}