package com.gallery.scan.args.picture

import com.gallery.scan.args.CursorLoaderArgs
import kotlinx.android.parcel.Parcelize

/**
 * 图片扫描
 */
@Parcelize
class ScanPictureArgs : CursorLoaderArgs(PictureColumns.uri, PictureColumns.columns)