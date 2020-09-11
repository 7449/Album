package com.gallery.scan.args.audio

import com.gallery.scan.args.CursorLoaderArgs
import kotlinx.android.parcel.Parcelize

/**
 * 音频扫描
 */
@Parcelize
class ScanAudioArgs : CursorLoaderArgs(AudioColumns.uri, AudioColumns.columns)