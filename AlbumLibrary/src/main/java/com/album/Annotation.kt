package com.album

import androidx.annotation.IntDef


@IntDef(AlbumConstant.TYPE_RESULT_CAMERA, AlbumConstant.TYPE_RESULT_CROP)
@Retention(AnnotationRetention.SOURCE)
annotation class AlbumResultType

@IntDef(AlbumConstant.TYPE_PERMISSIONS_ALBUM, AlbumConstant.TYPE_PERMISSIONS_CAMERA)
@Retention(AnnotationRetention.SOURCE)
annotation class PermissionsType
