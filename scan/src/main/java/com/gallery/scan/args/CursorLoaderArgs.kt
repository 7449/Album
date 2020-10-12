package com.gallery.scan.args

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.loader.content.CursorLoader
import com.gallery.scan.ScanImpl
import com.gallery.scan.extensions.ScanAudioArgs
import com.gallery.scan.extensions.ScanFileArgs
import com.gallery.scan.extensions.ScanPictureArgs
import kotlinx.android.parcel.Parcelize

/**
 * 扫描所需参数
 *
 * 承载[CursorLoader]所需的全部参数
 *
 * 文件扫描[ScanFileArgs]
 * 音频扫描[ScanAudioArgs]
 * 图片扫描[ScanPictureArgs]
 */
@Parcelize
open class CursorLoaderArgs(
        open val uri: Uri,
        open val projection: Array<String>? = null,
        open val sortOrder: String? = null,
) : Parcelable {
    companion object {

        private const val Key = "scanCursorLoaderArgs"

        internal fun Bundle.putCursorLoaderArgs(scanParameter: CursorLoaderArgs): Bundle {
            putParcelable(Key, scanParameter)
            return this
        }

        internal fun Bundle.getCursorLoaderArgs(): CursorLoaderArgs {
            return getParcelable(Key)
                    ?: throw KotlinNullPointerException("scanCursorLoaderArgs == null")
        }

    }

    /**
     * selection可能会变，因此动态获取
     * [args]为CursorLoader传递的Bundle
     * 里面包含了
     * [ScanImpl.scanMultiple]
     * [ScanImpl.scanSingle]
     * 传递的数据，可自行获取需要的数据
     */
    open fun createSelection(args: Bundle): String? = null

    /**
     * selectionArgs可能会变，因此动态获取
     * [args]为CursorLoader传递的Bundle
     * 里面包含了
     * [ScanImpl.scanMultiple]
     * [ScanImpl.scanSingle]
     * 传递的数据，可自行获取需要的数据
     */
    open fun createSelectionArgs(args: Bundle): Array<String>? = null

}