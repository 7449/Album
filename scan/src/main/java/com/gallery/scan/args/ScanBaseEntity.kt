package com.gallery.scan.args

open class ScanBaseEntity(
        open val id: Long,

        open val size: Long,
        open val displayName: String = "",
        open val title: String = "",
        open val dateAdded: Long,
        open val dateModified: Long,
        open val mimeType: String = "",
        open val width: Int,
        open val height: Int,

        open val parent: Long,
        open val mediaType: String,

        open val orientation: Int,
        open val bucketId: String,
        open val bucketDisplayName: String,

        open val duration: Long
)