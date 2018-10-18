package com.album.annotation

import androidx.annotation.IntDef
import com.album.AlbumConstant

/**
 * by y on 22/08/2017.
 */

@IntDef(AlbumConstant.TYPE_PERMISSIONS_ALBUM, AlbumConstant.TYPE_PERMISSIONS_CAMERA)
@Retention(AnnotationRetention.SOURCE)
annotation class PermissionsType
