package com.gallery.scan.args

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.loader.content.CursorLoader
import com.gallery.scan.args.audio.ScanAudioArgs
import com.gallery.scan.args.file.ScanFileArgs
import kotlinx.android.parcel.Parcelize

/**
 * 扫描所需参数
 *
 * 这个类比较重要，承载了[CursorLoader]所需的全部参数
 *
 * 文件扫描[ScanFileArgs]
 * 音频扫描[ScanAudioArgs]
 *
 */
@Parcelize
open class CursorLoaderArgs(
        open val scanUri: Uri,
        open val scanProjection: Array<String>? = null,
        open val scanSortOrder: String? = null
) : Parcelable {
    companion object {

        private const val Key = "scanCursorLoaderArgs"

        internal fun Bundle.putCursorLoaderArgs(scanParameter: CursorLoaderArgs): Bundle {
            putParcelable(Key, scanParameter)
            return this
        }

        internal fun Bundle.getCursorLoaderArgs(): CursorLoaderArgs {
            return getParcelable(Key) ?: throw KotlinNullPointerException("scanCursorLoaderArgs == null")
        }

    }

    /**
     * selection可能会变，因此动态获取
     * [args]为CursorLoader传递的Bundle
     */
    open fun createSelection(args: Bundle): String? = null

    /**
     * selectionArgs可能会变，因此动态获取
     * [args]为CursorLoader传递的Bundle
     */
    open fun createSelectionArgs(args: Bundle): Array<String>? = null
}