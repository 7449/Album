package com.gallery.ui.util

import com.gallery.scan.ScanEntity

internal fun ScanEntity.isGif() = mimeType.contains("gif")

internal fun ScanEntity.isVideo() = mediaType == "3"
