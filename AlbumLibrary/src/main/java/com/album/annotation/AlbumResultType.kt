package com.album.annotation

import androidx.annotation.IntDef
import com.album.AlbumConstant


/**
 * by y on 22/08/2017.
 */

@IntDef(AlbumConstant.TYPE_RESULT_CAMERA, AlbumConstant.TYPE_RESULT_CROP)
@Retention(AnnotationRetention.SOURCE)
annotation class AlbumResultType
