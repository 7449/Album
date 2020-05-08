package com.gallery.core.ext

import com.gallery.scan.ScanEntity

fun ScanEntity.isGif() = mimeType.contains("gif")

fun ScanEntity.isVideo() = mediaType == "3"